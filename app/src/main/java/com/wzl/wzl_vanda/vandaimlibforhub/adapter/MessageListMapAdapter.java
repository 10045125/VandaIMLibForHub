package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;

import com.wzl.wzl_vanda.vandaimlibforhub.binder.MessageBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapBindAdapter;


/**
 * Created by wzl_vanda on 15/7/27.
 */
public class MessageListMapAdapter extends EnumMapBindAdapter<Room, MessageListMapAdapter.SampleViewType> {


    enum SampleViewType {
        SAMPLE1, SAMPLE2, SAMPLE3, SAMPLE4, SAMPLE5, SAMPLE6, SAMPLE0
    }

    public MessageListMapAdapter(Context context) {
        super(context);
        putBinder(SampleViewType.SAMPLE1, new MessageBinder(this));
//        putBinder(SampleViewType.SAMPLE2, new ChatImageViewBinder2(this));
//        putBinder(SampleViewType.SAMPLE3, new ChatOtherTextViewBinder2(this));
//        putBinder(SampleViewType.SAMPLE4, new ChatOtherImageViewBinder2(this));
//        putBinder(SampleViewType.SAMPLE5, new ChatSoundBinder2(this));
//        putBinder(SampleViewType.SAMPLE6, new ChatOtherSoundBinder2(this));
//        putBinder(SampleViewType.SAMPLE0, new NullViewBinder2(this));

    }

    @Override
    public SampleViewType getEnumFromPosition(int position) {

//        int type = get(position).getLastMessage().getMessageType();
        SampleViewType mSampleViewType = SampleViewType.SAMPLE1;

//        switch (type) {
//            case -1:
//                mSampleViewType = SampleViewType.SAMPLE1;
//                break;
//            case 2:
//                mSampleViewType = SampleViewType.SAMPLE2;
//                break;
//            case 3:
//                mSampleViewType = SampleViewType.SAMPLE3;
//                break;
//            case 4:
//                mSampleViewType = SampleViewType.SAMPLE4;
//                break;
//            case 5:
//                mSampleViewType = SampleViewType.SAMPLE5;
//                break;
//            case 6:
//                mSampleViewType = SampleViewType.SAMPLE6;
//                break;
//            case 0:
//                mSampleViewType = SampleViewType.SAMPLE0;
//                break;
//        }

        return mSampleViewType;
    }

    @Override
    public SampleViewType getEnumFromOrdinal(int ordinal) {
        return SampleViewType.values()[ordinal];
    }



}
