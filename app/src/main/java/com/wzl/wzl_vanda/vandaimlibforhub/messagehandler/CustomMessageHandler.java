package com.wzl.wzl_vanda.vandaimlibforhub.messagehandler;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class CustomMessageHandler extends AVIMMessageHandler {
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        // 新消息到来了。在这里增加你自己的处理代码。
        String msgContent = message.getContent();
        System.out.println(conversation.getConversationId() + " 收到一条新消息：" + msgContent);
    }
}
