package com.wzl.wzl_vanda.vandaimlibforhub.data;

/**
 * 消息类型
 *
 * <br/> Created by Jam on 03/08/2015.
 */
public enum IMMsgType {

    /**
     * 消息类型 - 未知
     */
    UNKNOWN,

    /**
     * 消息类型 - 我发送的文本信息
     */
    TEXT_MINE,
    /**
     * 消息类型 - 其他人发送的文本信息
     */
    TEXT_OTHERS,

    /**
     * 消息类型 - 我发送的图片信息
     */
    IMAGE_MINE,
    /**
     * 消息类型 - 其他人发送的图片信息
     */
    IMAGE_OTHERS,

    /**
     * 消息类型 - 我发送的音频信息
     */
    AUDIO_MINE,
    /**
     * 消息类型 - 其他人发送的音频信息
     */
    AUDIO_OTHERS,

    /**
     * 消息类型 - 我发送的视频信息
     */
    VIDEO_MINE,
    /**
     * 消息类型 - 其他人发送的视频信息
     */
    VIDEO_OTHERS,
    ;

    public static IMMsgType valueOf(int value) {
        IMMsgType[] values = IMMsgType.values();

        if (value < 0 || value >= values.length) return UNKNOWN;

        return values[value];
    }
}
