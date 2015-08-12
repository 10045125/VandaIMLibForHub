package com.wzl.wzl_vanda.vandaimlibforhub.controller;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.MainBaseActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.db.RoomsTable;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehelp.MessageHelp;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.PathUtils;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.PhotoUtils;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lzw on 14/11/23.
 */
public class MessageAgent {

    public static final String MAPKEY = "infoID";
    public static final String NICKNAME = "nickname";

    private Map<String, Object> map = new HashMap<>();
    private String objectId;
    private AVIMConversation conversation;
    private ChatManager chatManager;

    private MessageHelp messageHelp;

    private SendCallback sendCallback = new SendCallback() {
        @Override
        public void onError(IMMsg message, Exception e) {

        }

        @Override
        public void onSuccess(IMMsg message) {

        }
    };

    public MessageAgent(AVIMConversation conversation, String objectId) {
        this.conversation = conversation;
        this.objectId = objectId;
        messageHelp = new MessageHelp();
        map.clear();
        if (objectId != null && !objectId.equals("")) {
            Log.e("objectId -> ", "" + objectId);
            map.put(MAPKEY, (Object) this.objectId);
        }
        chatManager = ChatManager.getInstance();
    }

    public void addNicknameMap(String nickname) {
        map.remove(NICKNAME);
        map.put(NICKNAME, nickname);
    }

    public void setSendCallback(SendCallback sendCallback) {
        this.sendCallback = sendCallback;
    }

    private void sendMsg(final AVIMTypedMessage msg, final String originPath, final SendCallback callback) {
        if (!chatManager.isConnect()) {
            Utils.log("im not connect");
        }
        conversation.sendMessage(msg, AVIMConversation.RECEIPT_MESSAGE_FLAG, new AVIMConversationCallback() {
            @Override
            public void done(AVException e) {
                if (e == null && originPath != null) {
                    File tmpFile = new File(originPath);
                    File newFile = new File(PathUtils.getChatFilePath(msg.getMessageId()));
                    boolean result = tmpFile.renameTo(newFile);
                    if (!result) {
                        throw new IllegalStateException("move file failed, can't use local cache");
                    }
                }
                if (callback != null) {
                    IMMsg imMsg = messageHelp.convert2IMMsg(ChatManager.getInstance().getImClient(), msg);
                    if (BuildConfig.DEBUG)
                        Log.i("IM", "onMessage, imMsg:" + imMsg);
                    DBHelper.getInstance().insertMsg(imMsg);
                    IMConv conv = messageHelp.genConvData(conversation, imMsg);
                    DBHelper.getInstance().insertAndIncrtUnread(conv, true);
                    if(MessageHelp.getMessageFragment() != null){
                        MessageHelp.getMessageFragment().refreshData();
                    }
                    if (e != null) {
                        callback.onError(imMsg, e);
                    } else {
                        callback.onSuccess(imMsg);
                    }
                }
            }
        });
    }

    public void resendMessage(final AVIMTypedMessage msg, final SendCallback sendCallback) {
        conversation.sendMessage(msg, AVIMConversation.RECEIPT_MESSAGE_FLAG, new AVIMConversationCallback() {
            @Override
            public void done(AVException e) {
                IMMsg imMsg = messageHelp.convert2IMMsg(ChatManager.getInstance().getImClient(), msg);
                if (BuildConfig.DEBUG)
                    Log.i("IM", "onMessage, imMsg:" + imMsg);
                DBHelper.getInstance().insertMsg(imMsg);
                if (e != null) {
                    sendCallback.onError(imMsg, e);
                } else {
                    sendCallback.onSuccess(imMsg);
                }
            }
        });
    }

    public void sendText(String content) {
        AVIMTextMessage textMsg = new AVIMTextMessage();
        textMsg.setText(content);
        textMsg.setAttrs(map);
        sendMsg(textMsg, null, sendCallback);
    }

    public void sendImage(String imagePath) {
        final String newPath = PathUtils.getChatFilePath(Utils.uuid());
        PhotoUtils.compressImage(imagePath, newPath);
        try {
            AVIMImageMessage imageMsg = new AVIMImageMessage(newPath);
            imageMsg.setAttrs(map);
            sendMsg(imageMsg, newPath, sendCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLocation(double latitude, double longitude, String address) {
        AVIMLocationMessage locationMsg = new AVIMLocationMessage();
        AVGeoPoint geoPoint = new AVGeoPoint(latitude, longitude);
        locationMsg.setLocation(geoPoint);
        locationMsg.setText(address);
        locationMsg.setAttrs(map);
        sendMsg(locationMsg, null, sendCallback);
    }

    public void sendAudio(String audioPath) {
        try {
            AVIMAudioMessage audioMsg = new AVIMAudioMessage(audioPath);
            audioMsg.setAttrs(map);
            sendMsg(audioMsg, audioPath, sendCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface SendCallback {

        void onError(IMMsg message, Exception e);

        void onSuccess(IMMsg message);

    }
}
