package com.wzl.wzl_vanda.vandaimlibforhub.adapter;


import android.content.Context;

import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatImageViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherImageViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherSoundBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherTextViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatSoundBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatTextViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.NullViewBinder2;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;


/**
 * Created by yqritc on 2015/03/20.
 */
public class SampleEnumMapAdapter extends EnumMapBindAdapter<DemoItem, SampleEnumMapAdapter.SampleViewType> {


    enum SampleViewType {
        SAMPLE1, SAMPLE2, SAMPLE3, SAMPLE4, SAMPLE5, SAMPLE6, SAMPLE0
    }

    public SampleEnumMapAdapter(Context context) {
        super(context);
//        putBinder(SampleViewType.SAMPLE1, new Sample1Binder(this));
//        putBinder(SampleViewType.SAMPLE2, new Sample2Binder(this));
//        putBinder(SampleViewType.SAMPLE3, new Sample3Binder(this));
        putBinder(SampleViewType.SAMPLE1, new ChatTextViewBinder2(this));
        putBinder(SampleViewType.SAMPLE2, new ChatImageViewBinder2(this));
        putBinder(SampleViewType.SAMPLE3, new ChatOtherTextViewBinder2(this));
        putBinder(SampleViewType.SAMPLE4, new ChatOtherImageViewBinder2(this));
        putBinder(SampleViewType.SAMPLE5, new ChatSoundBinder2(this));
        putBinder(SampleViewType.SAMPLE6, new ChatOtherSoundBinder2(this));
        putBinder(SampleViewType.SAMPLE0, new NullViewBinder2(this));

    }

    @Override
    public SampleViewType getEnumFromPosition(int position) {

        int type = get(position).type;
        SampleViewType mSampleViewType = null;

        switch (type) {
            case 1:
                mSampleViewType = SampleViewType.SAMPLE1;
                break;
            case 2:
                mSampleViewType = SampleViewType.SAMPLE2;
                break;
            case 3:
                mSampleViewType = SampleViewType.SAMPLE3;
                break;
            case 4:
                mSampleViewType = SampleViewType.SAMPLE4;
                break;
            case 5:
                mSampleViewType = SampleViewType.SAMPLE5;
                break;
            case 6:
                mSampleViewType = SampleViewType.SAMPLE6;
                break;
            case 0:
                mSampleViewType = SampleViewType.SAMPLE0;
                break;
//            case 6:
//                mSampleViewType = SampleViewType.SAMPLE7;
//                break;
//            case 7:
//                mSampleViewType = SampleViewType.SAMPLE8;
//                break;
//            case 8:
//                mSampleViewType = SampleViewType.SAMPLE9;
//                break;
        }

        return mSampleViewType;
    }

    @Override
    public SampleViewType getEnumFromOrdinal(int ordinal) {
        return SampleViewType.values()[ordinal];
    }
}
