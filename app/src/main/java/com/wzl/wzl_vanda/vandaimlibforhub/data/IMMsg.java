package com.wzl.wzl_vanda.vandaimlibforhub.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jam on 03/08/2015.
 */
public class IMMsg {
    /**
     * 消息属性 - 文件url
     */
    public final static String ATTR_FILE_URL = "url";
    /**
     * 消息属性 - 文件大小，单位字节
     */
    public final static String ATTR_FILE_SIZE_IN_BYTES = "sizeInBytes";
    /**
     * 消息属性 - 音视频长度，单位秒
     */
    public final static String ATTR_AV_DURATION = "duration";

    /**
     * 消息所属会话id
     */
    public String convId;

    /**
     * 数据库记录id
     */
    public long recordId;

    /**
     * 消息id
     */
    public String msgId;

    public String senderId;

    /**
     * 消息状态
     */
    public IMMsgStatus status;

    /**
     * 消息类型
     */
    public IMMsgType type;

    /**
     * 消息文字内容
     */
    public String text;

    /**
     * 消息发送时间
     */
    public long sendTime;

    /**
     * 消息已读数
     */
    public int readCount;

    /**
     * 消息其他属性
     */
    public HashMap<String, Object> attrs;

    public IMMsg(String convId) {
        this.convId = convId;
        this.attrs = new HashMap<>();
    }

    public void putAttr(String attrName, Object value) {
        this.attrs.put(attrName, value);
    }

    public Object getAttr(String attrName) {
        return this.attrs.get(attrName);
    }

    public Map<String,Object> getAttrs(){
        return attrs;
    }

    public String getUrl() {
        return (String) this.attrs.get(ATTR_FILE_URL);
    }

    @Override
    public String toString() {
        return "IMMsg{" +
                "convId='" + convId + '\'' +
                ", recordId=" + recordId +
                ", msgId='" + msgId + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", sendTime=" + sendTime +
                ", readCount=" + readCount +
                ", attrs=" + attrs +
                '}';
    }
}
