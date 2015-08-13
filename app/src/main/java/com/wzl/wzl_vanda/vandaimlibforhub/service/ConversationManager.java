package com.wzl.wzl_vanda.vandaimlibforhub.service;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ConversationHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Constant;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ConversationType;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ForMeConversationInfo;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.ColoredBitmapProvider;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class ConversationManager {

    private static ConversationManager conversationManager;

    /*
    *
    * 对话成员变化响应接口
    *
    * */

    private static AVIMConversationEventHandler eventHandler = new AVIMConversationEventHandler(){

        @Override
        public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
            Log.e("onMemberLeft -> ",""+avimConversation.getName());
//            CacheService.registerConv(avimConversation);
//            getInstance().postConvChanged(avimConversation);
            EventBus.getDefault().post(new TextView(ChatManager.getContext()));
        }

        @Override
        public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
            Log.e("onMemberJoined -> ",""+avimConversation.getName());
//            CacheService.registerConv(avimConversation);
//            getInstance().postConvChanged(avimConversation);

            EventBus.getDefault().post(new TextView(ChatManager.getContext()));
        }

        @Override
        public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

        }

        @Override
        public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

        }
    };


    public ConversationManager() {
    }

    public static synchronized ConversationManager getInstance() {
        if (conversationManager == null) {
            conversationManager = new ConversationManager();
        }
        return conversationManager;
    }

    public static AVIMConversationEventHandler getEventHandler() {
        return eventHandler;
    }

    /*
    * 它维护一个计数器，等待这个CountDownLatch的线程必须等到计数器为0时才可以继续。
    * */

    public static AVIMTypedMessage getLastMessage(final AVIMConversation conversation) throws AVException, InterruptedException {
//        final CountDownLatch latch = new CountDownLatch(1);
        final AVException[] es = new AVException[1];
        final List<AVIMTypedMessage> foundMessages = new ArrayList<>();
        conversation.queryMessages(1, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messages, AVException e) {
                es[0] = e;
                if (e == null) {
                    if (messages == null) {
                        Logger.d("null conversationId=" + conversation.getConversationId());
                    }
                    for (AVIMMessage message : messages) {
                        if (message instanceof AVIMTypedMessage) {
                            Log.e("message ->>> ",""+message.getContent());
                            foundMessages.add((AVIMTypedMessage) message);
                        }
                    }
                }
//                latch.countDown();
            }
        });
//        latch.await();
        if (es[0] != null) {
            throw es[0];
        }
        if (foundMessages.size() > 0) {
            return foundMessages.get(0);
        } else {
            return null;
        }

    }

    public void updateName(final AVIMConversation conv, String newName, final AVIMConversationCallback callback) {
        conv.setName(newName);
        conv.updateInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    callback.done(e);
                } else {
                    postConvChanged(conv);
                    callback.done(null);
                }
            }
        });
    }

    public void postConvChanged(AVIMConversation conv) {
        if (CacheService.getCurConv() != null && CacheService.getCurConv().getConversationId().equals(conv.getConversationId())) {
            CacheService.setCurConv(conv);
        }
        ConversationChangeEvent conversationChangeEvent = new ConversationChangeEvent(conv);
        EventBus.getDefault().post(conversationChangeEvent);
    }

    public void findGroupConversationsIncludeMe(AVIMConversationQueryCallback callback) {
        AVIMConversationQuery q = ChatManager.getInstance().getQuery();
        q.containsMembers(Arrays.asList(ChatManager.getInstance().getSelfId()));
        q.whereEqualTo(ConversationType.ATTR_TYPE_KEY, ConversationType.Group.getValue());
        q.orderByDescending(Constant.UPDATED_AT);
        q.findInBackground(callback);
    }

    public void findConversationsByConversationIds(List<String> ids, AVIMConversationQueryCallback callback) {
        if (ids.size() > 0) {
            AVIMConversationQuery q = ChatManager.getInstance().getQuery();
            q.whereContainsIn(Constant.OBJECT_ID, ids);
            q.findInBackground(callback);
        } else {
            callback.done(new ArrayList<AVIMConversation>(), null);
        }
    }

    public void createGroupConversation(List<String> members, final AVIMConversationCreatedCallback callback) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ConversationType.TYPE_KEY, ConversationType.Group.getValue());
        final String name = MessageHelper.nameByUserIds(members);
        map.put(ConversationType.NAME_KEY, name);
        ChatManager.getInstance().getImClient().createConversation(members, map, callback);
    }

    public static Bitmap getConversationIcon(AVIMConversation conversation) {
        return ColoredBitmapProvider.getInstance().createColoredBitmapByHashString(conversation.getConversationId());
    }

    public void sendWelcomeMessage(String toUserId) {
        ChatManager.getInstance().fetchConversationWithUserId(toUserId,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVException e) {
                        if (e == null) {
                           ForMeConversationInfo forMeConversationInfo = CacheService.getMeConversationObjectId(avimConversation.getConversationId());
                            MessageAgent agent = new MessageAgent(avimConversation,forMeConversationInfo != null ? forMeConversationInfo.objectId:"");
//                            agent.sendText(App.ctx.getString(R.string.message_when_agree_request));
                        }
                    }
                });
    }


}
