package com.wzl.wzl_vanda.vandaimlibforhub.adapter;


import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatImageViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherImageViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherSoundBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatOtherTextViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatSoundBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatTextViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.binder.NullViewBinder2;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageItem;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;


/**
 * List 适配
 */
public class SampleEnumMapAdapter extends EnumMapBindAdapter<DemoItem, SampleEnumMapAdapter.SampleViewType> {

    public String conversationId;
    public String faceUrlForMe;
    public String nickNameForMe;

    enum SampleViewType {
        ComeText, ComeImage, ComeAudio, ComeLocation, ToText, ToImage, ToAudio,ToLocation,Null
    }

    public SampleEnumMapAdapter(Context context,String conversationId) {
        super(context);
        this.conversationId = conversationId;
        putBinder(SampleViewType.ComeText, new ChatTextViewBinder2(this));
        putBinder(SampleViewType.ComeImage, new ChatImageViewBinder2(this));
        putBinder(SampleViewType.ToText, new ChatOtherTextViewBinder2(this));
        putBinder(SampleViewType.ToImage, new ChatOtherImageViewBinder2(this));
        putBinder(SampleViewType.ComeAudio, new ChatSoundBinder2(this));
        putBinder(SampleViewType.ToAudio, new ChatOtherSoundBinder2(this));
        putBinder(SampleViewType.Null, new NullViewBinder2(this));

    }

    boolean isComeMsg(AVIMTypedMessage msg) {
        return !MessageHelper.fromMe(msg);
    }

    @Override
    public SampleViewType getEnumFromPosition(int position) {

        AVIMTypedMessage msg = ((MessageItem)get(position)).avimTypedMessage;
        SampleViewType mSampleViewType = null;
        boolean isCome = isComeMsg(msg);

        int type;
        AVIMReservedMessageType msgType = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
        switch (msgType) {
            case TextMessageType://文本消息
                mSampleViewType = !isCome ? SampleViewType.ComeText : SampleViewType.ToText;
                break;
            case ImageMessageType:
                mSampleViewType = !isCome ? SampleViewType.ComeImage : SampleViewType.ToImage;
                break;
            case AudioMessageType:
                mSampleViewType = !isCome ? SampleViewType.ComeAudio : SampleViewType.ToAudio;
                break;
            case LocationMessageType:
                mSampleViewType = !isCome ? SampleViewType.ComeLocation : SampleViewType.ToLocation;
                break;
        }

        return mSampleViewType;
    }

    @Override
    public SampleViewType getEnumFromOrdinal(int ordinal) {
        return SampleViewType.values()[ordinal];
    }
}
