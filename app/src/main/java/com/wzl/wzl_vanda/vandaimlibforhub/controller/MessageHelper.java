package com.wzl.wzl_vanda.vandaimlibforhub.controller;

import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConvType;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;
import com.wzl.wzl_vanda.vandaimlibforhub.model.UserInfo;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.PathUtils;

import java.util.List;

/**
 * Created by lzw on 15/2/13.
 */
public class MessageHelper {

    public static String getFilePath(AVIMTypedMessage msg) {
        return PathUtils.getChatFilePath(msg.getMessageId());
    }

    public static String getFilePathForIMMsg(IMMsg msg) {
        return PathUtils.getChatFilePath(msg.msgId);
    }

    public static boolean fromMe(AVIMTypedMessage msg) {
        ChatManager chatManager = ChatManager.getInstance();
        String selfId = chatManager.getSelfId();
        return msg.getFrom().equals(selfId);
    }

    public static boolean fromMeForIMMsg(IMMsg msg) {
        ChatManager chatManager = ChatManager.getInstance();
        String selfId = chatManager.getSelfId();
        return msg.senderId.equals(selfId);
    }

    private static String bracket(String s) {
        return String.format("[%s]", s);
    }

    public static CharSequence outlineOfMsg(IMMsg msg) {
//        AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
        IMMsgType imConvType = msg.type;
        switch (imConvType) {
            case TEXT_OTHERS:
                return msg.text;
//            EmotionHelper.replace(ChatManager.getContext(), ((AVIMTextMessage) msg).getText())
            case IMAGE_OTHERS:
                return bracket("image");
//            case :
//                AVIMLocationMessage locMsg = (AVIMLocationMessage) msg;
//                String address = locMsg.getText();
//                if (address == null) {
//                    address = "";
//                }
//                return bracket(address);
            case AUDIO_OTHERS:
                return bracket("audio");
        }
        return null;
    }

    public static String nameByUserIds(List<String> userIds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userIds.size(); i++) {
            String id = userIds.get(i);
            if (i != 0) {
                sb.append(",");
            }
            sb.append(nameByUserId(id));
        }
        return sb.toString();
    }

    public static String nameByUserId(String id) {
        UserInfo user = ChatManager.getInstance().getChatManagerAdapter().getUserInfoById(id);
        if (user != null) {
            return user.getUsername();
        } else {
            return id;
        }
    }
}
