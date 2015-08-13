package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatImageViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherImageViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherSoundBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherTextViewBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatSoundBinder;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatTextViewBinder;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapCursorBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;


/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatEnumCursorMapAdapter extends EnumMapCursorBindAdapter<ChatEnumCursorMapAdapter.SampleViewType> {


    enum SampleViewType {
        SAMPLE1, SAMPLE2, SAMPLE3, SAMPLE4, SAMPLE5, SAMPLE6, SAMPLE0
    }

    public ChatEnumCursorMapAdapter(Context context) {
        super(context, null);
//        putBinder(SampleViewType.SAMPLE1, new ChatTextViewBinder(this));
//        putBinder(SampleViewType.SAMPLE2, new ChatImageViewBinder(this));
//        putBinder(SampleViewType.SAMPLE3, new ChatOtherTextViewBinder(this));
//        putBinder(SampleViewType.SAMPLE4, new ChatOtherImageViewBinder(this));
//        putBinder(SampleViewType.SAMPLE5, new ChatSoundBinder(this));
//        putBinder(SampleViewType.SAMPLE6, new ChatOtherSoundBinder(this));

    }

    @Override
    public SampleViewType getEnumFromPosition(int position) {
        DemoItem item = DemoItem.fromCursor((Cursor) getItem(position));//(DemoItem) getItem(position);
        int type = item.type;
// = get(position).type;
        Log.e("type", "" + type);
        Log.e("title->", "" + item.title);
        Log.e("url->", "" + item.url);
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
        }

        return mSampleViewType;
    }

    @Override
    public SampleViewType getEnumFromOrdinal(int ordinal) {
        return SampleViewType.values()[ordinal];
    }
}
