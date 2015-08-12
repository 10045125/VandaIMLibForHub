package com.wzl.wzl_vanda.vandaimlibforhub.data;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Jam on 03/08/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VER = 6;

    private static DBHelper instance;

    private HashSet<String> createdTables;
    private HashSet<String> insertedConvIds;

    public synchronized static DBHelper getCurrentUserInstance() {
        String selfId = ChatManager.getInstance().getSelfId();
        if (selfId == null) {
            throw new NullPointerException("selfId is null");
        }
        if (instance == null) {
            instance = new DBHelper(ChatManager.getContext(), selfId);
        }
        return instance;
    }


    /**
     * 初始化DBHelper实例
     *
     * @param context
     * @param userId
     */
    public static void init(Context context, String userId) {
        instance = new DBHelper(context, userId);
        Log.i("IM", "DB inited, userId:" + userId);
    }

    /**
     * 获取单例前需要先调用{@link #init(Context, String)}方法初始化单例
     *
     * @return
     */
    public static DBHelper getInstance() {
        if (instance == null) {
            throw new NullPointerException("DBHelper not inited");
        }

        return instance;
    }


    private DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    private DBHelper(Context context, String userId) {
        this(context, "im_" + userId + ".db3", DB_VER);
        this.createdTables = new HashSet<>();
        this.insertedConvIds = new HashSet<>();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("IM", "DBHelper.onCreate");

        this.createConversationTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("IM", "DBHelper.onUpgrade");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        String logMsg = String.format("db.name:%s, db.path:%s", this.getDatabaseName(), db.getPath());
        Log.d("IM", "DB.onOpen, " + logMsg);

        this.queryCreatedTables(db);
        this.queryInsertedConvIds(db);
    }

    private void queryCreatedTables(SQLiteDatabase db) {
        String query = "SELECT name FROM sqlite_master WHERE type = 'table'";
        Cursor cursor = db.rawQuery(query, new String[0]);
        while (cursor.moveToNext()) {
            createdTables.add(cursor.getString(0));
        }

        String logMsg = String.format("db:%s, createdTables:%s", this.getDatabaseName(), createdTables);
        Log.i("IM", "queryCreatedTables, " + logMsg);
    }

    private void queryInsertedConvIds(SQLiteDatabase db) {
        String query = "SELECT " + TConversation.COLUMN_CONV_ID + " FROM " + TConversation.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            this.insertedConvIds.add(cursor.getString(0));
        }
    }

    public synchronized void close() {
        Log.d("IM", "db.close, db.name:" + this.getDatabaseName());

        super.close();
        this.createdTables.clear();
        this.insertedConvIds.clear();
        instance = null;
    }

    public ArrayList<IMConv> loadConvs() {
        ArrayList<IMConv> convs = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String orderBy = TConversation.COLUMN_ORDER_PRIORITY + " DESC, " + TConversation.COLUMN_LATEST_MSG_TIME + " DESC";
        Cursor cursor = db.query(TConversation.TABLE_NAME, null, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            IMConv conv = new IMConv();
            conv.convId = cursor.getString(cursor.getColumnIndex(TConversation.COLUMN_CONV_ID));
            conv.type = IMConvType.valueOf(cursor.getInt(cursor.getColumnIndex(TConversation.COLUMN_CONV_TYPE)));
            conv.unreadCount = cursor.getInt(cursor.getColumnIndex(TConversation.COLUMN_UNREAD_COUNT));
            conv.orderPriority = cursor.getInt(cursor.getColumnIndex(TConversation.COLUMN_ORDER_PRIORITY));
            conv.latestMsgTime = cursor.getLong(cursor.getColumnIndex(TConversation.COLUMN_LATEST_MSG_TIME));

            String attrs = cursor.getString(cursor.getColumnIndex(TConversation.COLUMN_ATTRS));
            conv.attrs = JsonUtils.fromJsonString(attrs, HashMap.class);

            convs.add(conv);
        }

        return convs;
    }

    public void insertAndIncrtUnread(IMConv conv, boolean isResetUnreadCount) {
        String attrs = JsonUtils.toJsonString(conv.attrs);
        SQLiteDatabase db = this.getWritableDatabase();

        if (insertedConvIds.contains(conv.convId)) {
            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(TConversation.TABLE_NAME).append(" set ");
            if (isResetUnreadCount) {
                sql.append(TConversation.COLUMN_UNREAD_COUNT).append(" = 0,");
            } else {
                sql.append(TConversation.COLUMN_UNREAD_COUNT).append(" = 1 + ")
                        .append(TConversation.COLUMN_UNREAD_COUNT).append(", ");
            }
            sql.append(TConversation.COLUMN_LATEST_MSG_TIME).append(" = ?, ")
                    .append(TConversation.COLUMN_ATTRS).append(" = ? ")
                    .append(" WHERE ").append(TConversation.COLUMN_CONV_ID).append(" = ?");
            db.execSQL(sql.toString(), new Object[]{conv.latestMsgTime, attrs, conv.convId});
        } else {
            ContentValues insertValues = new ContentValues();
            insertValues.put(TConversation.COLUMN_CONV_ID, conv.convId);
            insertValues.put(TConversation.COLUMN_UNREAD_COUNT, 1);
            insertValues.put(TConversation.COLUMN_LATEST_MSG_TIME, conv.latestMsgTime);
            insertValues.put(TConversation.COLUMN_ORDER_PRIORITY, conv.orderPriority);
            ;
            insertValues.put(TConversation.COLUMN_CONV_TYPE, conv.type.ordinal());
            insertValues.put(TConversation.COLUMN_ATTRS, attrs);
            db.insert(TConversation.TABLE_NAME, null, insertValues);

            this.insertedConvIds.add(conv.convId);
        }
    }

    public void resetConvUnreadCount(String convId) {
        ContentValues values = new ContentValues();
        values.put(TConversation.COLUMN_UNREAD_COUNT, 0);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TConversation.TABLE_NAME, values, TConversation.COLUMN_CONV_ID + " = ?", new String[]{convId});
    }

    /**
     * @param convId       会话id
     * @param fromRecordId 开始记录id，返回比该id小的消息。值大于0时参数才生效。
     * @param limit        最多返回记录数量。值大于0时参数才生效。
     * @return 不为空。数据按照recordId从大到小排序，即最新的消息排在前面
     */
    public List<IMMsg> loadMsgs(String convId, long fromRecordId, int limit) {
        SQLiteDatabase db = this.getReadableDatabase(); // 确保数据库已打开，即onOpen已调用，确保createdTables已初始化

        ArrayList<IMMsg> msgs = new ArrayList<>();

        String tableName = this.genMsgHistoryTableName(convId);
        if (!this.createdTables.contains(tableName)) return msgs;

        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(tableName);

        if (fromRecordId > 0)
            sql.append(" where " + TMsgHistory.COLUMN_RECORD_ID + " < ").append(fromRecordId);

        sql.append(" order by ").append(TMsgHistory.COLUMN_RECORD_ID).append(" desc");

        if (limit > 0) sql.append(" limit ").append(limit);

        if (BuildConfig.DEBUG) Log.d("IM", "loadMsgs, sql:" + sql.toString());

        Cursor cursor = db.rawQuery(sql.toString(), null); // 直接sql效率没传入参数的好，就这样
        while (cursor.moveToNext()) {
            IMMsg msg = new IMMsg(convId);
//            msgs.add(msg);

            msg.recordId = cursor.getLong(cursor.getColumnIndex(TMsgHistory.COLUMN_RECORD_ID));
            msg.msgId = cursor.getString(cursor.getColumnIndex(TMsgHistory.COLUMN_MSG_ID));
            msg.senderId = cursor.getString(cursor.getColumnIndex(TMsgHistory.COLUMN_SENDER_ID));
            msg.type = IMMsgType.valueOf(cursor.getInt(cursor.getColumnIndex(TMsgHistory.COLUMN_MSG_TYPE)));
            msg.status = IMMsgStatus.valueOf(cursor.getInt(cursor.getColumnIndex(TMsgHistory.COLUMN_MSG_STATUS)));
            msg.sendTime = cursor.getLong(cursor.getColumnIndex(TMsgHistory.COLUMN_SEND_TIME));
            msg.text = cursor.getString(cursor.getColumnIndex(TMsgHistory.COLUMN_MSG_TEXT));
            msg.readCount = cursor.getInt(cursor.getColumnIndex(TMsgHistory.COLUMN_READ_COUNT));

            String attrs = cursor.getString(cursor.getColumnIndex(TMsgHistory.COLUMN_ATTRS));
            msg.attrs = JsonUtils.fromJsonString(attrs, HashMap.class);

            msgs.add(msg);
        }

        return msgs;
    }

    public void insertMsg(IMMsg msg) {

        this.ensureMsgHistoryTable(msg.convId);

        String tableName = this.genMsgHistoryTableName(msg.convId);
        ContentValues values = new ContentValues();
        values.put(TMsgHistory.COLUMN_MSG_ID, msg.msgId);
        values.put(TMsgHistory.COLUMN_SENDER_ID, msg.senderId);
        values.put(TMsgHistory.COLUMN_MSG_TYPE, msg.type.ordinal());
        values.put(TMsgHistory.COLUMN_MSG_STATUS, msg.status.ordinal());
        values.put(TMsgHistory.COLUMN_SEND_TIME, msg.sendTime);
        values.put(TMsgHistory.COLUMN_MSG_TEXT, msg.text);
        values.put(TMsgHistory.COLUMN_READ_COUNT, msg.readCount);
        values.put(TMsgHistory.COLUMN_ATTRS, JsonUtils.toJsonString(msg.attrs));

        SQLiteDatabase db = this.getWritableDatabase();
        msg.recordId = db.insert(tableName, null, values);
    }

    private void ensureMsgHistoryTable(String convId) {
        String tableName = this.genMsgHistoryTableName(convId);
        if (this.createdTables.contains(tableName)) return;

        synchronized (this) {
            if (this.createdTables.contains(tableName)) return;

            this.createMsgHistoryTable(convId);
        }
    }

    private void createMsgHistoryTable(String convId) {
        Log.d("IM", "createMsgHistoryTable, convId:" + convId);

        String tableName = this.genMsgHistoryTableName(convId);
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + TMsgHistory.COLUMN_RECORD_ID + " INTEGER PRIMARY KEY,"
                + TMsgHistory.COLUMN_MSG_ID + " CHAR(31) NOT NULL UNIQUE,"
                + TMsgHistory.COLUMN_SENDER_ID + " CHAR(31) NOT NULL, "
                + TMsgHistory.COLUMN_MSG_TYPE + " INT, "
                + TMsgHistory.COLUMN_MSG_STATUS + " INT, "
                + TMsgHistory.COLUMN_SEND_TIME + " BIGINT, "
                + TMsgHistory.COLUMN_MSG_TEXT + " TEXT, "
                + TMsgHistory.COLUMN_READ_COUNT + " INT DEFAULT 0, "
                + TMsgHistory.COLUMN_ATTRS + " TEXT "
                + ")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(createTableSql);
        this.createdTables.add(tableName);

        // TODO index
    }

    private String genMsgHistoryTableName(String convId) {
        return TMsgHistory.TABLE_PREFIX + convId;
    }

    private void createConversationTable(SQLiteDatabase db) {
        Log.d("IM", "createConversationTable");

        String createTableSql = "CREATE TABLE IF NOT EXISTS " + TConversation.TABLE_NAME + "("
                + TConversation.COLUMN_CONV_ID + " CHAR(63) PRIMARY KEY,"
                + TConversation.COLUMN_CONV_TYPE + " INT, "
                + TConversation.COLUMN_UNREAD_COUNT + " INT, "
                + TConversation.COLUMN_LATEST_MSG_TIME + " BIGINT, "
                + TConversation.COLUMN_ORDER_PRIORITY + " INT, "
                + TConversation.COLUMN_ATTRS + " TEXT "
                + ")";
        db.execSQL(createTableSql);
        this.createdTables.add(TConversation.TABLE_NAME);
    }

    public static class TMsgHistory {
        public final static String TABLE_PREFIX = "msgs_";
        public final static String COLUMN_RECORD_ID = "id";
        public final static String COLUMN_MSG_ID = "msg_id";
        public final static String COLUMN_SENDER_ID = "sender_id";
        public final static String COLUMN_MSG_TYPE = "type";
        public final static String COLUMN_MSG_STATUS = "status";
        public final static String COLUMN_SEND_TIME = "send_time";
        public final static String COLUMN_MSG_TEXT = "text";
        public final static String COLUMN_READ_COUNT = "read_count";
        public final static String COLUMN_ATTRS = "attrs";
    }

    public static class TConversation {
        public final static String TABLE_NAME = "conversations";
        public final static String COLUMN_CONV_ID = "conv_id";
        public final static String COLUMN_CONV_TYPE = "type";
        public final static String COLUMN_UNREAD_COUNT = "unread_count";
        public final static String COLUMN_ORDER_PRIORITY = "order_priority";
        public final static String COLUMN_LATEST_MSG_TIME = "latest_msg_time";
        public final static String COLUMN_ATTRS = "attrs";
    }

}
