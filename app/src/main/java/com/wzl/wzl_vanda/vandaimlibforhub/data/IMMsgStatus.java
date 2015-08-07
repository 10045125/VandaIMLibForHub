package com.wzl.wzl_vanda.vandaimlibforhub.data;

/**
 * 即使通信消息状态
 *
 * <br/>Created by Jam on 03/08/2015.
 */
public enum IMMsgStatus {
    /**
     * 消息状态 - 未知
     */
    UNKNOWN,

    /**
     * 消息状态 - 发送中
     */
    SENDING,

    /**
     * 消息状态 - 发送成功
     */
    SENT,

    /**
     * 消息状态 - 发送失败
     */
    FAILED,

    /**
     * 消息状态 - 接收成功
     */
    RECEIVED,

    /**
     * 消息状态 - 已读
     */
    READ,

    /**
     * 消息状态 - 已读且已读信息已同步到服务端
     */
    NOTIFIED,

    /**
     * 消息状态 - 已删
     */
    DELETED,
    ;

    public static IMMsgStatus valueOf(int value) {
        IMMsgStatus[] values = IMMsgStatus.values();

        if (value < 0 || value >= values.length) return UNKNOWN;

        return values[value];
    }
}
