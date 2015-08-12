package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;

import com.wzl.wzl_vanda.vandaimlibforhub.binder.ChatSetBinder;
import com.wzl.wzl_vanda.viewtypelibrary.EnumMapBindAdapter;

/**
 * Created by wzl_vanda on 15/8/10.
 * 作者：wzl_vanda on 15/8/10 17:36
 * 邮箱：806594174@qq.com
 */
public class ChatSetAdapter extends EnumMapBindAdapter<String, ChatSetAdapter.ChatViewType> {


    enum ChatViewType {
        SAMPLE1
    }

    public ChatSetAdapter(Context context) {
        super(context);
        putBinder(ChatViewType.SAMPLE1, new ChatSetBinder(this));
    }

    @Override
    public ChatViewType getEnumFromPosition(int position) {
       ChatViewType chatViewType = ChatViewType.SAMPLE1;
        return chatViewType;
    }

    @Override
    public ChatViewType getEnumFromOrdinal(int ordinal) {
        return ChatViewType.values()[ordinal];
    }
}
