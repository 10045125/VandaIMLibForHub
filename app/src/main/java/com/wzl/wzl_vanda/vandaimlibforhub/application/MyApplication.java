package com.wzl.wzl_vanda.vandaimlibforhub.application;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMOnlineClientsCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehandler.ClientEventHandler;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehandler.CustomMessageHandler;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ChatManagerAdapterImpl;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ConversationManager;
import com.wzl.wzl_vanda.vandaimlibforhub.service.PushManager;
import com.wzl.wzl_vanda.viewtypelibrary.application.AppApplication;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MyApplication extends AppApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AVOSCloud.initialize(this, "vh4pa8ly3s9gwt2wksxlg7pefkzmrpppktl6niooy2kcmbvb", "ke5rav8dife2ljg7h1xunj82b3aypjfp6wsk6bpipols8ajq");
        //AVOSCloud.initialize(this, "pbwl3akrcecuw5v1vot1g0dpdlczo9yvh5hkuwogg6yhczao", "xoavqop80sjjd5ae9o3728zen6fhvaghbpdq1hslsf22p737");
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new CustomMessageHandler());
        AVIMClient.setClientEventHandler(new ClientEventHandler());
        PushManager.getInstance().init(mContext);
//        AVObject.registerSubclass(AddRequest.class);

//        login("Androidstudiowzl", "10045125");

//        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
//        final AVIMClient imClient = AVIMClient.getInstance(Utils.getLocalMacAddress(this));
      //  initChatManager();
//        ChatManager.getInstance().openClientWithSelfId(Utils.getLocalMacAddress(getContext()),null);
        /*imClient.open(new AVIMClientCallback() {
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
                    clientIds.add(Utils.getLocalMacAddress(mContext));
//                    clientIds.add("34f60e9ba326fef10cf31ac188be9f27");
                    clientIds.add("55b8a95800b0196faa27f8ae");

// 我们给对话增加一个自定义属性 type，表示单聊还是群聊
// 常量定义：
                    int ConversationType_OneOne = 1; // 两个人之间的单聊
// int ConversationType_Group = 1;  // 多人之间的群聊
                    Map<String, Object> attr = new HashMap<String, Object>();
                    attr.put("type", ConversationType_OneOne);

                    imClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
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
