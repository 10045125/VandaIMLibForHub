package com.wzl.wzl_vanda.vandaimlibforhub.application;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.model.AddRequest;
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
        boolean isTest = false;
        String appId = isTest ? "x3o016bxnkpyee7e9pa5pre6efx2dadyerdlcez0wbzhw25g" : "vh4pa8ly3s9gwt2wksxlg7pefkzmrpppktl6niooy2kcmbvb";
        String appKey = isTest ? "057x24cfdzhffnl3dzk14jh9xo2rq6w1hy1fdzt5tv46ym78" : "ke5rav8dife2ljg7h1xunj82b3aypjfp6wsk6bpipols8ajq";
        AVOSCloud.initialize(this, appId, appKey);
        PushManager.getInstance().init(mContext);
        AVObject.registerSubclass(AddRequest.class);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(false);
        initChatManager();
    }

    private void initChatManager() {

        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        chatManager.setConversationEventHandler(ConversationManager.getEventHandler());
        ChatManagerAdapterImpl chatManagerAdapter = new ChatManagerAdapterImpl(getContext());
        chatManager.setChatManagerAdapter(chatManagerAdapter);
        ChatManager.setDebugEnabled(true);
        if (AVUser.getCurrentUser() != null) {
            if (BuildConfig.DEBUG)
                Log.e("CurrentUser() ->> ", "not null " + AVUser.getCurrentUser().getUsername());
            chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
            chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
        } else {
            if (BuildConfig.DEBUG)
                Log.e("CurrentUser() ->> ", "null");
        }
    }
}
