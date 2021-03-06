package com.wzl.wzl_vanda.viewtypelibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;


import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;
import com.wzl.wzl_vanda.viewtypelibrary.db.database.Column;
import com.wzl.wzl_vanda.viewtypelibrary.db.database.SQLiteTable;

import java.util.ArrayList;
import java.util.List;

public  class ItemsDataHelper extends BaseDataHelper implements DBInterface<DemoItem> {

    public ItemsDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.ALL_ITEMS_CONTENT_URI;
    }

    protected Uri getContentUri(int offset, int limit) {
        return Uri.parse(DataProvider.ALL_ITEMS_CONTENT_URI.toString()+"?offset="+offset+"&limit="+limit);
    }

    @Override
    protected String getTableName() {
        return ItemsDBInfo.TABLE_NAME;
    }

    @Override
    public DemoItem query(String id) {
        DemoItem item = null;
        Cursor cursor;
        cursor = query(null, ItemsDBInfo.ID + "= ?",
                new String[]{
                        id
                }, null);
        if (cursor.moveToFirst()) {
            item = DemoItem.fromCursor(cursor);
        }
        cursor.close();
        return item;
    }

    @Override
    public int clearAll() {
        synchronized (DataProvider.obj) {
            DataProvider.DBHelper mDBHelper = DataProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            return db.delete(ItemsDBInfo.TABLE_NAME, null, null);
        }
    }

    @Override
    public void bulkInsert(List<DemoItem> listData) {
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        for (DemoItem item : listData) {
            ContentValues values = getContentValues(item);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    @Override
    public ContentValues getContentValues(DemoItem data) {
        ContentValues values = new ContentValues();
        values.put(ItemsDBInfo.ID, data.id);
        values.put(ItemsDBInfo.TITLE, data.title);
        values.put(ItemsDBInfo.TYPE, data.type);
        values.put(ItemsDBInfo.URL, data.url);
        return values;
    }

    @Override
    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, ItemsDBInfo.ID + " ASC");
    }

    public CursorLoader getCursorLoader(int offset,int limit) {
        return new CursorLoader(getContext(), getContentUri(offset,limit), null, null, null, ItemsDBInfo.ID + " ASC");
    }

    public static final class ItemsDBInfo implements BaseColumns {
        private ItemsDBInfo() {
        }

        public static final String TABLE_NAME = "items";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String URL = "url";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(TITLE, Column.DataType.TEXT)
                .addColumn(TYPE, Column.DataType.INTEGER)
                .addColumn(URL, Column.DataType.TEXT);
    }
}
