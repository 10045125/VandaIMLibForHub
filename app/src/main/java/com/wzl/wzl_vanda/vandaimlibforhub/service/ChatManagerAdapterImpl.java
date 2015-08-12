package com.wzl.wzl_vanda.vandaimlibforhub.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.bumptech.glide.Glide;
import com.wzl.wzl_vanda.vandaimlibforhub.MainActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManagerAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehelp.MessageHelp;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.model.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lzw on 15/5/13.
 */
public class ChatManagerAdapterImpl implements ChatManagerAdapter {
    private static final long NOTIFY_PERIOD = 1000;
    private static final int REPLY_NOTIFY_ID = 1;
    private static long lastNotifyTime = 0;
    private Context context;
    private MessageHelp messageHelp;

    public ChatManagerAdapterImpl(Context context) {
        this.context = context;
        messageHelp = new MessageHelp();
    }

    @Override
    public UserInfo getUserInfoById(String userId) {
        AVUser user = CacheService.lookupUser(userId);
        if (user == null) {
            return null;
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setAvatarUrl(User.getAvatarUrl(user));
            return userInfo;
        }
    }

    @Override
    public void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception {
        CacheService.cacheUsers(userIds);
    }

    @Override
    public void shouldShowNotification(final Context context, String selfId, final AVIMConversation conversation, final IMMsg message) {
        if (showNotificationWhenNewMessageCome(selfId)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        CacheService.cacheUserIfNone(message.senderId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }


                @Override
                protected void onPostExecute(Void aVoid) {
                    showMessageNotification(context, conversation, message);
                }
            }.execute();
        }
    }

    private void showMessageNotification(final Context context, AVIMConversation conv, IMMsg msg) {
        if (System.currentTimeMillis() - lastNotifyTime < NOTIFY_PERIOD) {
            return;
        } else {
            lastNotifyTime = System.currentTimeMillis();
        }
        final int icon = context.getApplicationInfo().icon;
        Intent intent = new Intent(context, MainActivity.class);
        CacheService.registerConvIfNone(conv);
//        ChatManager.getInstance().registerConversation(conv);
        intent.putExtra("ConvId", conv.getConversationId());
        intent.putExtra("Notification",true);

        //why Random().nextInt()
        //http://stackoverflow.com/questions/13838313/android-onnewintent-always-receives-same-intent
        final PendingIntent pend = PendingIntent.getActivity(context, new Random().nextInt(),
                intent, 0);
        final Notification.Builder builder = new Notification.Builder(context);
        final CharSequence notifyContent = MessageHelper.outlineOfMsg(msg);
//    CharSequence username = "username";
//    UserInfo from = getUserInfoById(msg.getFrom());

        Map<String, Object> map = msg.getAttrs();
        if (map != null && map.get(MessageAgent.MAPKEY) != null) {
            Log.e("map", "" + map.get(MessageAgent.MAPKEY));
            UserService.findUserInConversationAllInfo((String) map.get(MessageAgent.MAPKEY), new FindCallback<AVObject>() {

                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list != null && list.size() > 0) {
                        CharSequence username = list.get(0).getString(User.NICKNAME);
                        builder.setContentIntent(pend)
                                .setSmallIcon(icon)
//                                .setLargeIcon()
                                .setWhen(System.currentTimeMillis())
                                .setTicker(username + "\n" + notifyContent)
                                .setContentTitle(username)
                                .setContentText(notifyContent)
                                .setAutoCancel(true);
                        NotificationManager man = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = builder.getNotification();
                        PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(context);
                        if (preferenceMap.isVoiceNotify()) {
                            notification.defaults |= Notification.DEFAULT_SOUND;
                        }
                        if (preferenceMap.isVibrateNotify()) {
                            notification.defaults |= Notification.DEFAULT_VIBRATE;
                        }
                        man.notify(REPLY_NOTIFY_ID, notification);
                    }
                }
            });
        }
    }


    private boolean showNotificationWhenNewMessageCome(String selfId) {
        PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(context);
        return preferenceMap.isNotifyWhenNews();
    }


    public void cancelNotification() {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(REPLY_NOTIFY_ID);
    }
}
