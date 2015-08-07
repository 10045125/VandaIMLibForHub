package com.wzl.wzl_vanda.vandaimlibforhub.data;

/**
 * Created by Jam on 07/08/2015.
 */
public enum IMConvType {


    /**
     * 会话类型 - 未知
     */
    UNKNOWN,

    /**
     * 会话类型 - 聊天会话
     */
    CHAT,

    /**
     * 会话类型 - 进圈子申请
     */
    APPLICATION,

    /**
     * 会话类型 - 通知
     */
    NOTICE,

    ;

    public static IMConvType valueOf(int value) {
        IMConvType[] values = IMConvType.values();

        if (value < 0 || value >= values.length) return UNKNOWN;

        return values[value];
    }
}
