package com.wzl.wzl_vanda.vandaimlibforhub.service;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by wzl_vanda on 15/7/31.
 */
public class EAVIMConversation extends AVIMConversation {


    public EAVIMConversation(AVIMClient client, String conversationId){
        super(client,conversationId);
    }

}
