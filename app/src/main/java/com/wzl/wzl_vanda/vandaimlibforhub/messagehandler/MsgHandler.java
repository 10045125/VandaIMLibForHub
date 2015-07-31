package com.wzl.wzl_vanda.vandaimlibforhub.messagehandler;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MsgHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        // 请按自己需求改写
//        switch(message.getMessageType()) {
//            case AVIMReservedMessageType.TextMessageType:
//                AVIMTextMessage textMsg = (AVIMTextMessage)message;
//
////                Logger.d("收到文本消息:" + textMsg.getText() + ", msgId:" + textMsg.getMessageId());
//                break;
//
//            case AVIMReservedMessageType.FileMessageType:
//                AVIMFileMessage fileMsg = (AVIMFileMessage)message;
////                Logger.id("收到文件消息。msgId=" + fileMsg.getMessageId() + ", url=" + fileMsg.getFileUrl() + ", size=" + fileMsg.getSize());
//                break;
//
//            case AVIMReservedMessageType.ImageMessageType:
//                AVIMImageMessage imageMsg = (AVIMImageMessage)message;
////                Logger.id("收到图片消息。msgId=" + imageMsg.getMessageId() + ", url=" + imageMsg.getFileUrl() + ", width=" + imageMsg.getWidth() + ", height=" + imageMsg.getHeight());
//                break;
//
//            case AVIMReservedMessageType.AudioMessageType:
//                AVIMAudioMessage audioMsg = (AVIMAudioMessage)message;
////                Logger.id("收到音频消息。msgId=" + audioMsg.getMessageId() + ", url=" + audioMsg.getFileUrl() + ", duration=" + audioMsg.getDuration());
//                break;
//
//            case AVIMReservedMessageType.VideoMessageType:
//                AVIMVideoMessage videoMsg = (AVIMAudioMessage)message;
////                Logger.id("收到视频消息。msgId=" + videoMsg.getMessageId() + ", url=" + videoMsg.getFileUrl() + ", duration=" + videoMsg.getDuration());
//                break;
//
//            case AVIMReservedMessageType.LocationMessageType:
//                AVIMLocationMessage locMsg = (AVIMLocationMessage)message;
////                Logger.id("收到位置消息。msgId=" + locMsg.getMessageId() + ", latitude=" + locMsg.getLocation().getLatitude() + ", longitude=" + locMsg.getLocation().getLongitude());
//                break;
//        }
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        // 请加入你自己需要的逻辑...
    }
}
