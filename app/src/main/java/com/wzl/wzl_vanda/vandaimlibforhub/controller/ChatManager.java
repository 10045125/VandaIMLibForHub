package com.wzl.wzl_vanda.vandaimlibforhub.controller;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.MainActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.db.RoomsTable;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehelp.MessageHelp;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ConversationType;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class ChatManager extends AVIMClientEventHandler {

    public static final String KEY_UPDATED_AT = "updatedAt";
    public static final String LOGTAG = "HUB";
    private static ChatManager chatManager;

    private static Context context;
    private MessageHelp messageHelp;

    private static ConnectionListener defaultConnectListener = new ConnectionListener() {
        @Override
        public void onConnectionChanged(boolean connect) {
            if (ChatManager.isDebugEnabled()) {
//                Utils.log();
            }
        }
    };

    private ConnectionListener connectionListener = defaultConnectListener;
    private static boolean setupDatabase = false;
    private Map<String, AVIMConversation> cachedConversations = new HashMap<>();
    private AVIMClient imClient; // 客户端实例
    private String selfId;
    private boolean connect = false;
    private MessageHandler messageHandler;  //接受消息的handler
    private RoomsTable roomsTable;
    private EventBus eventBus = EventBus.getDefault();
    private ChatManagerAdapter chatManagerAdapter;
    private static boolean debugEnabled;

    private ChatManager() {
    }

    public static synchronized ChatManager getInstance() {
        if (chatManager == null) {
            chatManager = new ChatManager();
        }
        return chatManager;
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static void setDebugEnabled(boolean debugEnabled) {
        ChatManager.debugEnabled = debugEnabled;
    }

    // fetchConversation
    public void fetchConversationWithUserId(String userId, final AVIMConversationCreatedCallback callback) {
        final List<String> members = new ArrayList<>();
        members.add(userId);
        members.add(selfId);
        AVIMConversationQuery query = imClient.getQuery();
        query.withMembers(members);
        query.whereEqualTo(ConversationType.ATTR_TYPE_KEY, ConversationType.Single.getValue());
        query.orderByDescending(KEY_UPDATED_AT);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    callback.done(null, e);
                } else {
                    if (conversations.size() > 0) {
                        callback.done(conversations.get(0), null);
                    } else {
                        Map<String, Object> attrs = new HashMap<>();
                        attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
                        imClient.createConversation(members, attrs, callback);
                    }
                }
            }
        });
    }


    public void init(Context context) {
        this.context = context;
        messageHandler = new MessageHandler();
        messageHelp = new MessageHelp();
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, messageHandler);
        AVIMClient.setClientEventHandler(this);
        //签名
        //AVIMClient.setSignatureFactory(new SignatureFactory());
    }

    public void setConversationEventHandler(AVIMConversationEventHandler eventHandler) {
        AVIMMessageManager.setConversationEventHandler(eventHandler);
    }

    public void setupDatabaseWithSelfId(String selfId) {
        this.selfId = selfId;
        if (setupDatabase) {
            return;
        }
        setupDatabase = true;
//        roomsTable = RoomsTable.getCurrentUserInstance();
        DBHelper.getCurrentUserInstance();
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public AVIMClient getImClient() {
        return imClient;
    }

    public String getSelfId() {
        return selfId;
    }

    public void openClientWithSelfId(String selfId, final AVIMClientCallback callback) {
        if (this.selfId == null) {
            throw new IllegalStateException("please call setupDatabaseWithSelfId() first");
        }
        if (!this.selfId.equals(selfId)) {
            throw new IllegalStateException("setupDatabaseWithSelfId and openClient's selfId should be equal");
        }
        imClient = AVIMClient.getInstance(selfId);
        imClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVException e) {
                if (e != null) {
                    connect = false;
                    if (connectionListener != null)
                        connectionListener.onConnectionChanged(connect);
                } else {
                    connect = true;
                    if (connectionListener != null)
                        connectionListener.onConnectionChanged(connect);
                }
                if (callback != null) {
                    callback.done(client, e);
                }
            }
        });
    }

    private void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conv) {
        if (message.getMessageId() == null) {
            throw new NullPointerException("message id is null");
        }
//        MessageEvent messageEvent = new MessageEvent(message, MessageEvent.Type.Receipt);
//        eventBus.post(messageEvent);
//        DBHelper.getInstance().insertMsg(imMsg);
    }

    private void onMessage(final AVIMTypedMessage message, final AVIMConversation conversation) {
        if (BuildConfig.DEBUG)
            Utils.log("receive message=" + message.getContent());
        if (message.getMessageId() == null) {
            if (BuildConfig.DEBUG) {
                Utils.log("message id is null");
            }
            return;
        }
        if (!ConversationHelper.isValidConversation(conversation)) {
            if (BuildConfig.DEBUG) {
                Utils.log("receive msg from invalid conversation");
            }
            return;
        }
        if (lookUpConversationById(conversation.getConversationId()) == null) {
            registerConversation(conversation);
        }

        CacheService.registerConvIfNone(conversation);

        IMMsg imMsg = messageHelp.convert2IMMsg(imClient, message);

        if (BuildConfig.DEBUG)
            Log.i("IM", "onMessage, imMsg:" + imMsg);

        DBHelper.getInstance().insertMsg(imMsg);

        IMConv conv = messageHelp.genConvData(conversation, imMsg);

        if (selfId != null && MainActivity.getCurrentChattingConvid() == null || !MainActivity.getCurrentChattingConvid().equals(message
                .getConversationId())) {
            chatManagerAdapter.shouldShowNotification(context, selfId, conversation, imMsg);
            DBHelper.getInstance().insertAndIncrtUnread(conv, false);
        } else {
            DBHelper.getInstance().insertAndIncrtUnread(conv, true);
        }

        if (MessageHelp.getMessageFragment() != null) {
            MessageHelp.getMessageFragment().refreshData();
        }
        eventBus.post(imMsg);
    }


    public void closeWithCallback(final AVIMClientCallback callback) {
        imClient.close(new AVIMClientCallback() {

            @Override
            public void done(AVIMClient client, AVException e) {
                if (e != null) {
                    Utils.logThrowable(e);
                }
                if (callback != null) {
                    callback.done(client, e);
                }
            }
        });
        imClient = null;
        selfId = null;
    }

    public AVIMConversationQuery getQuery() {
        return imClient.getQuery();
    }

    @Override
    public void onConnectionPaused(AVIMClient client) {
        connect = false;
        connectionListener.onConnectionChanged(connect);
    }

    @Override
    public void onConnectionResume(AVIMClient client) {
        connect = true;
        connectionListener.onConnectionChanged(connect);
    }

    public boolean isConnect() {
        return connect;
    }

    //cache
    public void registerConversation(AVIMConversation conversation) {
        cachedConversations.put(conversation.getConversationId(), conversation);
    }

    public AVIMConversation lookUpConversationById(String conversationId) {
        return cachedConversations.get(conversationId);
    }

    public ChatManagerAdapter getChatManagerAdapter() {
        return chatManagerAdapter;
    }

    public void setChatManagerAdapter(ChatManagerAdapter chatManagerAdapter) {
        this.chatManagerAdapter = chatManagerAdapter;
    }

    //ChatUser
    public List<Room> findRecentRooms() {
        return RoomsTable.getCurrentUserInstance().selectRooms();
    }

    public interface ConnectionListener {
        void onConnectionChanged(boolean connect);
    }

    private static class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

        @Override
        public void onMessage(AVIMTypedMessage message, AVIMConversation conversation,
                              AVIMClient client) {
            Utils.log("message content ->> " + message.getContent());
            if (client.getClientId().equals(chatManager.getSelfId())) {
                chatManager.onMessage(message, conversation);
            } else {
                // 收到其它的client的消息，可能是上一次别的client登录未正确关闭，这里关边掉。
                client.close(null);
            }
        }

        @Override
        public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation,
                                     AVIMClient client) {
            Utils.log("onMessageReceipt content ->> " + message.getContent());
            if (client.getClientId().equals(chatManager.getSelfId())) {
                chatManager.onMessageReceipt(message, conversation);
            } else {
                client.close(null);
            }
        }
    }

    /**
     * msgId 、time 共同使用，防止某毫秒时刻有重复消息
     */
    public void queryMessages(AVIMConversation conversation, String msgId, long time, int limit,
                              final AVIMTypedMessagesArrayCallback callback) {
        conversation.queryMessages(msgId, time, limit, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> imMessages, AVException e) {
                if (e != null) {
                    callback.done(new ArrayList<AVIMTypedMessage>(), e);
                } else {
                    List<AVIMTypedMessage> resultMessages = new ArrayList<>();
                    for (AVIMMessage msg : imMessages) {
                        if (msg instanceof AVIMTypedMessage) {
                            resultMessages.add((AVIMTypedMessage) msg);
                        }
                    }
                    callback.done(resultMessages, null);
                }
            }
        });
    }


}
