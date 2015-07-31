package com.wzl.wzl_vanda.vandaimlibforhub.controller;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.wzl.wzl_vanda.vandaimlibforhub.model.UserInfo;

import java.util.List;

public interface ChatManagerAdapter {

  UserInfo getUserInfoById(String userId);

  void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception;

  //某一对话来了一条消息，却并未正在此对话聊着天

  void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message);
}
