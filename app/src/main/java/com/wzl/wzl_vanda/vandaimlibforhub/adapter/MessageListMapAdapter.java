package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;

import com.wzl.wzl_vanda.vandaimlibforhub.binder.MessageBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapBindAdapter;


/**
 * Created by wzl_vanda on 15/7/27.
 */
public class MessageListMapAdapter extends EnumMapBindAdapter<IMConv, MessageListMapAdapter.MessageViewType> {

    private DBHelper mDbHelper;

    enum MessageViewType {
        SAMPLE1, SAMPLE2, SAMPLE3, SAMPLE4, SAMPLE5, SAMPLE6, SAMPLE0
    }

    public MessageListMapAdapter(Context context) {
        super(context);
        mDbHelper = DBHelper.getCurrentUserInstance();
        putBinder(MessageViewType.SAMPLE1, new MessageBinder(this));
    }

    @Override
    public MessageViewType getEnumFromPosition(int position) {
        MessageViewType mSampleViewType = MessageViewType.SAMPLE1;
        return mSampleViewType;
    }

    @Override
    public MessageViewType getEnumFromOrdinal(int ordinal) {
        return MessageViewType.values()[ordinal];
    }

    public DBHelper getmDbHelper(){
        return mDbHelper;
    }


    public void refreshFromDatabase() {
        clear();
        addAll(this.mDbHelper.loadConvs());
    }
}
