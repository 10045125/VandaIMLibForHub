package com.wzl.wzl_vanda.vandaimlibforhub.messagehelp;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.MainBaseActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConvType;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgStatus;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.MessageFragment;

import java.util.Map;

/**
 * Created by wzl_vanda on 15/8/7.
 * 作者：wzl_vanda on 15/8/7 17:51
 * 邮箱：806594174@qq.com
 */
public class MessageHelp {

    public static MessageFragment getMessageFragment(){
        return MainBaseActivity.instance != null ? MainBaseActivity.instance.mMessageFragment:null;
    }


    public IMConv genConvData(AVIMConversation avimConv, IMMsg newMsg) {
        IMConv conv = new IMConv();
        conv.convId = newMsg.convId;
        conv.type = IMConvType.CHAT;
        conv.latestMsgTime = newMsg.sendTime;
        conv.putAttr(IMConv.ATTR_IM_MSG_TYPE, newMsg.type.ordinal());
        conv.putAttr(IMConv.ATTR_SENDER_ID, newMsg.senderId);
        conv.putAttr(IMConv.ATTR_TITLE, avimConv.getName());
        conv.putAttr(IMConv.ATTR_TEXT, newMsg.text);
        conv.putAttr(MessageAgent.MAPKEY, newMsg.getAttr(MessageAgent.MAPKEY));

        return conv;
    }

    public IMMsg convert2IMMsg(AVIMClient client, AVIMTypedMessage typedMsg) {
        IMMsg newMsg = new IMMsg(typedMsg.getConversationId());
        newMsg.msgId = typedMsg.getMessageId();
        newMsg.readCount = 0;
        newMsg.sendTime = typedMsg.getTimestamp();
        newMsg.senderId = typedMsg.getFrom();

        boolean isMyMsg = client.getClientId().equals(typedMsg.getFrom());
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
                if (BuildConfig.DEBUG)
                    Log.e("IM", "CustomMessageHandler unhandle message type:" + typedMsg.getMessageType());
                newMsg.type = IMMsgType.UNKNOWN;
        }

        if (newMsg.text == null) newMsg.text = "";
        return newMsg;
    }

    private void initTextMsg(AVIMTextMessage txtMsg, IMMsg imMsg) {
        imMsg.text = txtMsg.getText();
        Map<String, Object> map = txtMsg.getAttrs();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                imMsg.putAttr(key, value);
            }
            imMsg.putAttr(MessageAgent.MAPKEY, map.get(MessageAgent.MAPKEY));
        }
    }

    private void initImageMsg(AVIMImageMessage imageMsg, IMMsg imMsg) {
        imMsg.text = imageMsg.getText(); // 不为null?
        imMsg.putAttr(IMMsg.ATTR_FILE_URL, imageMsg.getFileUrl());
        imMsg.putAttr(IMMsg.ATTR_FILE_SIZE_IN_BYTES, imageMsg.getSize());

        Map<String, Object> map = imageMsg.getAttrs();
        if (map != null) {

            for (Map.Entry entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                imMsg.putAttr(key, value);
            }
            imMsg.putAttr(MessageAgent.MAPKEY, map.get(MessageAgent.MAPKEY));
        }

    }

    private void initAudioMsg(AVIMAudioMessage audioMsg, IMMsg imMsg) {
        imMsg.text = audioMsg.getText(); // 不为null?
        imMsg.putAttr(IMMsg.ATTR_FILE_URL, audioMsg.getFileUrl());
        imMsg.putAttr(IMMsg.ATTR_AV_DURATION, audioMsg.getDuration());
        imMsg.putAttr(IMMsg.ATTR_FILE_SIZE_IN_BYTES, audioMsg.getSize());

        Map<String, Object> map = audioMsg.getAttrs();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                imMsg.putAttr(key, value);
            }
            imMsg.putAttr(MessageAgent.MAPKEY, map.get(MessageAgent.MAPKEY));
        }
    }

    private void initVideoMsg(AVIMVideoMessage videoMsg, IMMsg imMsg) {
        imMsg.text = videoMsg.getText();
        imMsg.putAttr(IMMsg.ATTR_FILE_URL, videoMsg.getFileUrl());
        imMsg.putAttr(IMMsg.ATTR_AV_DURATION, videoMsg.getDuration());
        imMsg.putAttr(IMMsg.ATTR_FILE_SIZE_IN_BYTES, videoMsg.getSize());

        Map<String, Object> map = videoMsg.getAttrs();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                imMsg.putAttr(key, value);
            }
            imMsg.putAttr(MessageAgent.MAPKEY, map.get(MessageAgent.MAPKEY));
        }
    }


}
