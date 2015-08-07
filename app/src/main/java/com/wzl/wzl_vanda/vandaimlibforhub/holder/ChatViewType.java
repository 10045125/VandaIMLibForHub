package com.wzl.wzl_vanda.vandaimlibforhub.holder;

import android.view.ViewGroup;

import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;

/**
 * Created by Jam on 04/08/2015.
 */
public enum ChatViewType {

    UNDEFINED {
        public ChatViewBaseHolder buildChatViewHolder(ViewGroup viewGroup) {
            throw new RuntimeException("building UNDEFINED ChatViewType");
        }
    },

    TEXT_MINE {
        public ChatViewBaseHolder buildChatViewHolder(ViewGroup viewGroup) {
            return TextChatViewHolder.build(viewGroup);
        }
    },

    TEXT_OTHERS {
        public ChatViewBaseHolder buildChatViewHolder(ViewGroup viewGroup) {
            return TextOthersChatViewHolder.build(viewGroup);
        }
    },

    IMAGE_MINE {
        @Override
        public ChatViewBaseHolder buildChatViewHolder(ViewGroup viewGroup) {
            return ImageChatViewHolder.build(viewGroup);
        }
    },

    IMAGE_OTHERS {
        @Override
        public ChatViewBaseHolder buildChatViewHolder(ViewGroup viewGroup) {
            return ImageOthersChatViewHolder.build(viewGroup);
        }
    },

    // SOUND, SOUND_OTHERS
    ;

    public abstract ChatViewBaseHolder buildChatViewHolder(ViewGroup viewGroup);

    public static ChatViewType valueOf(IMMsgType msgType) {
        switch (msgType) {
            case TEXT_MINE: return ChatViewType.TEXT_MINE;
            case TEXT_OTHERS: return ChatViewType.TEXT_OTHERS;
            case IMAGE_MINE: return ChatViewType.IMAGE_MINE;
            case IMAGE_OTHERS: return ChatViewType.IMAGE_OTHERS;
            default: return ChatViewType.UNDEFINED;
        }
    }

    public static ChatViewType valueOf(int value) {
        ChatViewType[] values = ChatViewType.values();
        if (value < 0 || value >= values.length) return UNDEFINED;

        return values[value];
    }
}
