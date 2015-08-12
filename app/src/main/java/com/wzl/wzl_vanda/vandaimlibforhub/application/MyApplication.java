package com.wzl.wzl_vanda.vandaimlibforhub.application;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehandler.ClientEventHandler;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehandler.CustomMessageHandler;
import com.wzl.wzl_vanda.vandaimlibforhub.model.AddRequest;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ChatManagerAdapterImpl;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ConversationManager;
import com.wzl.wzl_vanda.vandaimlibforhub.service.PushManager;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Utils;
import com.wzl.wzl_vanda.viewtypelibrary.application.AppApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MyApplication extends AppApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        boolean isTest = false;
        String appId = isTest ? "x3o016bxnkpyee7e9pa5pre6efx2dadyerdlcez0wbzhw25g":"vh4pa8ly3s9gwt2wksxlg7pefkzmrpppktl6niooy2kcmbvb";
        String appKey = isTest ? "057x24cfdzhffnl3dzk14jh9xo2rq6w1hy1fdzt5tv46ym78":"ke5rav8dife2ljg7h1xunj82b3aypjfp6wsk6bpipols8ajq";
        AVOSCloud.initialize(this, appId, appKey);
        PushManager.getInstance().init(mContext);
        AVObject.registerSubclass(AddRequest.class);
//        AVObject.registerSubclass(UpdateInfo.class);
        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(false);
//        AVIMClient.setClientEventHandler(new ClientEventHandler());
//        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new CustomMessageHandler());
//        AVObject.registerSubclass(AddRequest.class);

//        login("Androidstudiowzl", "10045125");

//        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
//        final AVIMClient imClient = AVIMClient.getInstance(Utils.getLocalMacAddress(this));
        initChatManager();
//        ChatManager.getInstance().openClientWithSelfId(Utils.getLocalMacAddress(getContext()),null);
      /*  ChatManager.getInstance().getImClient().open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVException e) {
                if (null != e) {
                    // 出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试。
                    // 此时聊天服务不可用。
                    Log.e("AVException", "" + e.toString());
                    e.printStackTrace();
                } else {
                    // 成功登录，可以开始进行聊天了（假设为 MainActivity）。
//                    Intent intent = new Intent(currentActivity, MainActivity.class);
//                    currentActivity.startActivity(intent);
                    Log.e("AVException ->>> ", "ok");
                    final List<String> clientIds = new ArrayList<String>();
                    clientIds.add(AVUser.getCurrentUser().getObjectId());
//                    clientIds.add("34f60e9ba326fef10cf31ac188be9f27");
                    clientIds.add("Androidstudiowzl");

// 我们给对话增加一个自定义属性 type，表示单聊还是群聊
// 常量定义：
                    int ConversationType_OneOne = 1; // 两个人之间的单聊
// int ConversationType_Group = 1;  // 多人之间的群聊
                    Map<String, Object> attr = new HashMap<String, Object>();
                    attr.put("type", ConversationType_OneOne);

                    ChatManager.getInstance().getImClient().createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(final AVIMConversation conversation, AVException e) {
                            if (null != conversation) {
                                // 成功了，这时候可以显示对话的 Activity 页面（假定为 ChatActivity）了。

                                conversation.addMembers(clientIds, new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (null != e) {
                                            // 加入失败，报错.
                                        } else {
                                            // 发出邀请，此后新成员就可以看到这个对话中的所有消息了。

                                        }

                                        AVIMMessage message = new AVIMMessage();
                                        message.setContent("hello");
                                        conversation.sendMessage(message, AVIMConversation.NONTRANSIENT_MESSAGE_FLAG, new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVException e) {

                                            }
                                        });
                                    }

                                });
                            }
                        }
                    });

                }
                ;
            }
        });*/


    }


    private void login(String name, String password) {
        AVUser.logInInBackground(name, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
//                dialog.dismiss();
//                if (filterException(e)) {
//                    UserService.updateUserLocation();
//                    MainActivity.goMainActivityFromActivity(EntryLoginActivity.this);
//                }
                Log.e("logInInBackground ->>> ", "logInInBackground");
                ChatManager chatManager = ChatManager.getInstance();
                chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
                chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
            }
        });
    }


    private void initChatManager() {

        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        if (AVUser.getCurrentUser() != null) {
            Log.e("CurrentUser() ->> ","not null "+ AVUser.getCurrentUser().getUsername());
            chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
        }else{
            Log.e("CurrentUser() ->> ","null");
        }
        chatManager.setConversationEventHandler(ConversationManager.getEventHandler());
        ChatManagerAdapterImpl chatManagerAdapter = new ChatManagerAdapterImpl(getContext());
        chatManager.setChatManagerAdapter(chatManagerAdapter);
        ChatManager.setDebugEnabled(true);
    }
}
