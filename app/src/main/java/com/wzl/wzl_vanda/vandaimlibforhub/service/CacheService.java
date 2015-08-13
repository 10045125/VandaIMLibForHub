package com.wzl.wzl_vanda.vandaimlibforhub.service;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Constant;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ForMeConversationInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lzw on 14/12/19.
 */
public class CacheService {

    private static Map<String, AVIMConversation> cachedConvs = new HashMap<String, AVIMConversation>();
    private static Map<String, AVUser> cachedUsers = new HashMap<String, AVUser>();
    private static List<String> friendIds = new ArrayList<String>();

    private static Map<String, ForMeConversationInfo> cachedForMeConversationInfo = new HashMap<String, ForMeConversationInfo>();

    private static AVIMConversation curConv;


    public static ForMeConversationInfo getMeConversationObjectId(String conversationId) {

        ForMeConversationInfo forMeConversationInfo = cachedForMeConversationInfo.get(conversationId);

        if (BuildConfig.DEBUG)
            Log.e("map -> ", "" + cachedForMeConversationInfo.toString());

        if (forMeConversationInfo == null) {
            forMeConversationInfo = cachedForMeConversationInfo.get("0");
        }

        return forMeConversationInfo;
    }

    public static void registerForMeConversationInfo(String userId) {
        cachedForMeConversationInfo.clear();
        UserService.findUserInConversationAllInfoFromUserID(userId, new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null && list != null) {

                    for (int i = 0; i < list.size(); i++) {
                        ForMeConversationInfo mForMeConversationInfo = new ForMeConversationInfo(list.get(i));
                        if (BuildConfig.DEBUG) {
                            Log.e("Cache Service list ->", "" + list.size() + " " + mForMeConversationInfo.conversationId);
                        }
                        cachedForMeConversationInfo.put(mForMeConversationInfo.conversationId, mForMeConversationInfo);
                    }
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e("Cache Service e ->", "" + e.toString());
                    }
                }
            }
        });
    }


    public static AVUser lookupUser(String userId) {
        return cachedUsers.get(userId);
    }

    public static void registerUser(AVUser user) {
        cachedUsers.put(user.getObjectId(), user);
    }

    public static void registerUsers(List<AVUser> users) {
        for (AVUser user : users) {
            registerUser(user);
        }
    }

    public static AVIMConversation lookupConv(final String convid) {

        AVIMConversation avimConversation = cachedConvs.get(convid);
        if (avimConversation == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(convid);
            ConversationManager.getInstance().findConversationsByConversationIds(list, new AVIMConversationQueryCallback() {
                @Override
                public void done(List<AVIMConversation> list, AVException e) {
                    if (list != null)
                        for (AVIMConversation conv : list) {
                            registerConv(conv);
                        }
                }
            });
        }
        return cachedConvs.get(convid);
    }

    public static void lookupConvCallBack(final String convid, AVIMConversationQueryCallback callback) {

        AVIMConversation avimConversation = cachedConvs.get(convid);
        if (avimConversation == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(convid);
            ConversationManager.getInstance().findConversationsByConversationIds(list, callback);
        }
    }

    public static void registerConvs(List<AVIMConversation> convs) {
        for (AVIMConversation conv : convs) {
            registerConv(conv);
        }
    }

    public static void registerConv(AVIMConversation conv) {
        cachedConvs.put(conv.getConversationId(), conv);
    }

    public static void registerConvIfNone(AVIMConversation conv) {
        if (lookupConv(conv.getConversationId()) == null) {
            registerConv(conv);
        }
    }

    public static List<String> getFriendIds() {
        return friendIds;
    }

    public static void setFriendIds(List<String> friendIds) {
        CacheService.friendIds = Collections.unmodifiableList(friendIds);
    }

    public static AVIMConversation getCurConv() {
        return curConv;
    }

    public static void setCurConv(AVIMConversation curConv) {
        CacheService.curConv = curConv;
    }

    public static boolean isCurConvid(String convid) {
        return curConv != null && curConv.getConversationId().equals(convid);
    }

    public static boolean isCurConv(AVIMConversation conv) {
        if (getCurConv() != null && getCurConv().getConversationId().equals(conv.getConversationId())) {
            return true;
        } else {
            return false;
        }
    }

    public static void cacheUsers(List<String> ids) throws AVException {
        Set<String> uncachedIds = new HashSet<String>();
        for (String id : ids) {
            if (lookupUser(id) == null) {
                uncachedIds.add(id);
            }
        }
        List<AVUser> foundUsers = findUsers(new ArrayList<String>(uncachedIds));
        registerUsers(foundUsers);
    }

    public static List<AVUser> findUsers(List<String> userIds) throws AVException {
        if (userIds.size() <= 0) {
            return new ArrayList<AVUser>();
        }
        AVQuery<AVUser> q = AVUser.getQuery();
        q.whereContainedIn(Constant.OBJECT_ID, userIds);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        return q.find();
    }

    public static void cacheConvs(List<String> ids, final AVIMConversationCallback callback) throws AVException {
        Set<String> uncachedIds = new HashSet<String>();
        for (String id : ids) {
            if (lookupConv(id) == null) {
                uncachedIds.add(id);
            }
        }
        ConversationManager.getInstance().findConversationsByConversationIds(new ArrayList<String>(uncachedIds), new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    callback.done(e);
                } else {
                    registerConvs(conversations);
                    callback.done(null);
                }
            }
        });
    }

    public static void cacheUserIfNone(String userId) throws AVException {
        if (lookupUser(userId) == null) {
            registerUser(UserService.findUser(userId));
        }
    }
}
