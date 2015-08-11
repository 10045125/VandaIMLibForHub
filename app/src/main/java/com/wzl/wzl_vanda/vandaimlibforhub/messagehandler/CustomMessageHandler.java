package com.wzl.wzl_vanda.vandaimlibforhub.messagehandler;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.GlobalData;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConvType;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgStatus;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;

import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class CustomMessageHandler extends AVIMTypedMessageHandler {
    private EventBus eventBus = EventBus.getDefault();

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        IMMsg imMsg = this.convert2IMMsg(client, message);
        DBHelper.getInstance().insertMsg(imMsg);

        IMConv conv = this.genConvData(conversation, imMsg);
        DBHelper.getInstance().insertAndIncrtUnread(conv);

        this.eventBus.post(imMsg);
        this.eventBus.post(conv);
    }

    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        String logMsg = String.format("convId:%s, msg.id:%s, msg.content:%s",
                message.getConversationId(), message.getMessageId(), message.getContent());
        Log.i("IM", "onMessageReceipt, " + logMsg);
    }

    IMConv genConvData(AVIMConversation avimConv, IMMsg newMsg) {
        IMConv conv = new IMConv();
        conv.convId = newMsg.convId;
        conv.type = IMConvType.CHAT;
        conv.latestMsgTime = newMsg.sendTime;
        conv.putAttr(IMConv.ATTR_IM_MSG_TYPE, newMsg.type.ordinal());
        conv.putAttr(IMConv.ATTR_SENDER_ID, newMsg.senderId);
        conv.putAttr(IMConv.ATTR_TITLE, avimConv.getName());
        conv.putAttr(IMConv.ATTR_TEXT, newMsg.text);

        return conv;
    }

    IMMsg convert2IMMsg(AVIMClient client, AVIMTypedMessage typedMsg) {
        IMMsg newMsg = new IMMsg(typedMsg.getConversationId());
        newMsg.msgId = typedMsg.getMessageId();
        newMsg.readCount = 0;
        newMsg.sendTime = typedMsg.getTimestamp();
        newMsg.senderId = typedMsg.getFrom();

        boolean isMyMsg = client.getClientId().equalsIgnoreCase(typedMsg.getFrom());
        newMsg.status = isMyMsg ? IMMsgStatus.SENT : IMMsgStatus.RECEIVED; //FIXME 状态不正确，需要根据实际状态处理， 先这样
        switch (typedMsg.getMessageType()) {
            case AVIMMessageType.TEXT_MESSAGE_TYPE:
                this.initTextMsg((AVIMTextMessage) typedMsg, newMsg);
                newMsg.type = isMyMsg ? IMMsgType.TEXT_MINE : IMMsgType.TEXT_OTHERS;
                break;

            case AVIMMessageType.IMAGE_MESSAGE_TYPE:
                this.initImageMsg((AVIMImageMessage) typedMsg, newMsg);
                newMsg.type = isMyMsg ? IMMsgType.IMAGE_MINE : IMMsgType.IMAGE_OTHERS;
                break;

            case AVIMMessageType.AUDIO_MESSAGE_TYPE:
                this.initAudioMsg((AVIMAudioMessage) typedMsg, newMsg);
                newMsg.type = isMyMsg ? IMMsgType.AUDIO_MINE : IMMsgType.AUDIO_OTHERS;
                break;

            case AVIMMessageType.VIDEO_MESSAGE_TYPE:
                this.initVideoMsg((AVIMVideoMessage) typedMsg, newMsg);
                newMsg.type = isMyMsg ? IMMsgType.VIDEO_MINE : IMMsgType.VIDEO_OTHERS;
                break;

            default:
                Log.e("IM", "CustomMessageHandler unhandle message type:" + typedMsg.getMessageType());
                newMsg.type = IMMsgType.UNKNOWN;
        }

        if (newMsg.text == null) newMsg.text = "";
        return newMsg;
    }

    private void initTextMsg(AVIMTextMessage txtMsg, IMMsg imMsg) {
        imMsg.text = txtMsg.getText();
    }

    private void initImageMsg(AVIMImageMessage imageMsg, IMMsg imMsg) {
        imMsg.text = imageMsg.getText(); // 不为null?
        imMsg.putAttr(IMMsg.ATTR_FILE_URL, imageMsg.getFileUrl());
        imMsg.putAttr(IMMsg.ATTR_FILE_SIZE_IN_BYTES, imageMsg.getSize());
    }

    private void initAudioMsg(AVIMAudioMessage audioMsg, IMMsg imMsg) {
        imMsg.text = audioMsg.getText(); // 不为null?
        imMsg.putAttr(IMMsg.ATTR_FILE_URL, audioMsg.getFileUrl());
        imMsg.putAttr(IMMsg.ATTR_AV_DURATION, audioMsg.getDuration());
        imMsg.putAttr(IMMsg.ATTR_FILE_SIZE_IN_BYTES, audioMsg.getSize());
    }

    private void initVideoMsg(AVIMVideoMessage videoMsg, IMMsg imMsg) {
        imMsg.text = videoMsg.getText();
        imMsg.putAttr(IMMsg.ATTR_FILE_URL, videoMsg.getFileUrl());
        imMsg.putAttr(IMMsg.ATTR_AV_DURATION, videoMsg.getDuration());
        imMsg.putAttr(IMMsg.ATTR_FILE_SIZE_IN_BYTES, videoMsg.getSize());
    }
}
