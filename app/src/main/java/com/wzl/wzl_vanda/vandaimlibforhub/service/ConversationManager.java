package com.wzl.wzl_vanda.vandaimlibforhub.service;

import android.graphics.Bitmap;
import android.util.Log;

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
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Constant;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ConversationType;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.ColoredBitmapProvider;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
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
            CacheService.registerConv(avimConversation);
            getInstance().postConvChanged(avimConversation);
        }

        @Override
        public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
            CacheService.registerConv(avimConversation);
            getInstance().postConvChanged(avimConversation);
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
        final CountDownLatch latch = new CountDownLatch(1);
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
                            foundMessages.add((AVIMTypedMessage) message);
                        }
                    }
                }
                latch.countDown();
            }
        });
        latch.await();
        if (es[0] != null) {
            throw es[0];
        }
        if (foundMessages.size() > 0) {
            return foundMessages.get(0);
        } else {
            return null;
        }

    }

    public List<Room> findAndCacheRooms() throws AVException, InterruptedException {
        final List<Room> rooms = ChatManager.getInstance().findRecentRooms();
        Log.e("rooms ->> ",""+rooms.size());
      /*  AVIMConversationQuery q = ChatManager.getInstance().getQuery();
        List<String> ids = new ArrayList<>();

        for (Room room: rooms){
            ids.add(room.getConversationId());
        }

        q.whereContainsIn(Constant.OBJECT_ID, ids);
        q.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVException e) {

                if (list != null){
                    for (int i=0; i < list.size();i++){
                        AVIMConversation mAVIMConversation = list.get(i);
                        rooms.get(i).setConversation(mAVIMConversation);
                        try {
                            rooms.get(i).setLastMessage(getLastMessage(mAVIMConversation));
                        } catch (AVException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

            }
        });

        for (Room room : rooms) {
            AVIMConversation conversation = CacheService.lookupConv(room.getConversationId());
            room.setConversation(conversation);
            room.setLastMessage(getLastMessage(conversation));
//            if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Group) {
//                userIds.add(ConversationHelper.otherIdOfConversation(conversation));
//            }
        }
        List<String> convids = new ArrayList<>();
        for (Room room : rooms) {
            convids.add(room.getConversationId());
        }
        final CountDownLatch latch = new CountDownLatch(1);
        final AVException[] es = new AVException[1];
        CacheService.cacheConvs(convids, new AVIMConversationCallback() {
            @Override
            public void done(AVException e) {
                es[0] = e;
                latch.countDown();
            }
        });
        latch.await();
        if (es[0] != null) {
            throw es[0];
        }
        List<String> userIds = new ArrayList<>();
        for (Room room : rooms) {
            AVIMConversation conversation = CacheService.lookupConv(room.getConversationId());
            room.setConversation(conversation);
            room.setLastMessage(getLastMessage(conversation));
            if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Group) {
                userIds.add(ConversationHelper.otherIdOfConversation(conversation));
            }
        }
        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room lhs, Room rhs) {
                if (lhs.getLastMessage() != null && rhs.getLastMessage() != null) {
                    long value = lhs.getLastMessage().getTimestamp() - rhs.getLastMessage().getTimestamp();
                    if (value > 0) {
                        return -1;
                    } else if (value < 0) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        CacheService.cacheUsers(new ArrayList<>(userIds));*/
        return rooms;
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
                            MessageAgent agent = new MessageAgent(avimConversation);
//                            agent.sendText(App.ctx.getString(R.string.message_when_agree_request));
                        }
                    }
                });
    }


}
