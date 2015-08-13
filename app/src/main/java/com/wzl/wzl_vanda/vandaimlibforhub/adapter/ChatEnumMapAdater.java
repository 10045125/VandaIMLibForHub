package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;
import android.util.Log;

import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatImageViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherImageViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherSoundBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherTextViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatSoundBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatTextViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapBindAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by wzl_vanda on 15/8/7.
 * 作者：wzl_vanda on 15/8/7 18:41
 * 邮箱：806594174@qq.com
 */
public class ChatEnumMapAdater extends EnumMapBindAdapter<IMMsg, ChatEnumMapAdater.ChatViewType> {

    public String conversationId;
    public String faceUrlForMe;
    public String nickNameForMe;

    /**
     * size
     */
    private final static int DEFAULT_LIMIT_PER_LOAD = 10;

    /**
     * 消息按照id从小到大排序
     */
    private DBHelper mDbHelper;
    private long dbDataPosition;
    private boolean isLoadComplete = false;

    enum ChatViewType {
        ComeText, ComeImage, ComeAudio, ComeLocation, ToText, ToImage, ToAudio, ToLocation, Null
    }

    /**
     * @param context
     * @param conversationId
     */
    public ChatEnumMapAdater(Context context, String conversationId) {
        super(context);
        this.conversationId = conversationId;
        mDbHelper = DBHelper.getCurrentUserInstance();
        putBinder(ChatViewType.ToText, new ChatTextViewBinder(this));
        putBinder(ChatViewType.ComeText, new ChatOtherTextViewBinder(this));
        putBinder(ChatViewType.ToImage, new ChatImageViewBinder(this));
        putBinder(ChatViewType.ComeImage, new ChatOtherImageViewBinder(this));
        putBinder(ChatViewType.ToAudio, new ChatSoundBinder(this));
        putBinder(ChatViewType.ComeAudio, new ChatOtherSoundBinder(this));

    }

    boolean isComeMsg(IMMsg msg) {
        return !MessageHelper.fromMeForIMMsg(msg);
    }

    /**
     * @param position
     * @return
     */
    @Override
    public ChatViewType getEnumFromPosition(int position) {

        IMMsg msg = get(position);
        ChatViewType mSampleViewType = null;
        IMMsgType msgType = msg.type;

        switch (msgType) {
            case TEXT_MINE://文本消息 自己发送的消息
                mSampleViewType = ChatViewType.ToText;
                break;
            case TEXT_OTHERS: // text message,others send message
                mSampleViewType = ChatViewType.ComeText;
                break;
            case IMAGE_MINE: // image message,for me
                mSampleViewType = ChatViewType.ToImage;
                break;
            case IMAGE_OTHERS:
                mSampleViewType = ChatViewType.ComeImage;
                break;
            case AUDIO_MINE:
                mSampleViewType = ChatViewType.ToAudio;
                break;
            case AUDIO_OTHERS:
                mSampleViewType = ChatViewType.ComeAudio;
                break;
        }

        return mSampleViewType;
    }

    /**
     * @param ordinal
     * @return
     */
    @Override
    public ChatViewType getEnumFromOrdinal(int ordinal) {
        return ChatViewType.values()[ordinal];
    }


    /**
     * @return 是否全部加载完成
     */
    public boolean loadMoreMsgsFromDB() {
        long fromRecordId = 0;
        if (list.size() > 0) fromRecordId = dbDataPosition;
        List<IMMsg> loadedMsgs = null;
        if (!isLoadComplete) {
            loadedMsgs = this.mDbHelper.loadMsgs(conversationId, fromRecordId, DEFAULT_LIMIT_PER_LOAD);
            if (loadedMsgs.size() == 0) {
                isLoadComplete = true;
            } else {
                dbDataPosition = loadedMsgs.get(loadedMsgs.size() - 1).recordId;
            }

            if (BuildConfig.DEBUG) {
                String logMsg = String.format("convId:%s, fromRecordId:%s, loadedMsgs.size:%s",
                        conversationId, fromRecordId, loadedMsgs.size());
                Log.d("IM", "loadMoreMsgsFromDB, " + logMsg);
            }

            Collections.reverse(loadedMsgs);
            addAll(0, loadedMsgs);
//            notifyItemRangeInserted(0, loadedMsgs.size());
//            list.addAll(loadedMsgs);
        }

        return isLoadComplete;
    }

    /**
     * @param msg
     */
    public void addNewMsg(IMMsg msg) {
        this.list.add(msg);
    }


    /**
     * @return 无数据时返回 -1
     */
    public int getLastItemPosition() {
        return this.list.size() - 1;
    }

    private IMMsg getMsgByPosition(int position) {
        int size = list.size();
        if (position < 0 || position >= size) return null;

        return list.get(position);
    }

}
