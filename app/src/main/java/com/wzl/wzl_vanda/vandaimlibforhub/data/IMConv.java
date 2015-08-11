package com.wzl.wzl_vanda.vandaimlibforhub.data;

import java.util.HashMap;

/**
 * Created by Jam on 07/08/2015.
 */
public class IMConv {
    /**
     * 默认会话id - 申请
     */
    public final static String CONV_ID_APPLICATIONS = "applications";
    /**
     * 默认会话id - 提醒通知
     */
    public final static String CONV_ID_NOTICES = "notice";

    public final static String ATTR_ICON = "icon";
    public final static String ATTR_IM_MSG_TYPE = "immsg_type";
    public final static String ATTR_TITLE = "title";
    public final static String ATTR_TEXT = "text";
    public final static String ATTR_SENDER_ID = "sender_id";

    public String convId;
    public IMConvType type;
    public int unreadCount;
    public int orderPriority;
    public long latestMsgTime;

    public HashMap<String, Object> attrs;

    public IMConv() {
        this.attrs = new HashMap<>();
    }

    public void putAttr(String attrName, Object value) {
        this.attrs.put(attrName, value);
    }

    public Object getAttr(String attrName) {
        return this.attrs.get(attrName);
    }

    @Override
    public String toString() {
        return "IMConv{" +
                "convId='" + convId + '\'' +
                ", type=" + type +
                ", unreadCount=" + unreadCount +
                ", orderPriority=" + orderPriority +
                ", latestMsgTime=" + latestMsgTime +
                ", attrs=" + attrs +
                '}';
    }
}
