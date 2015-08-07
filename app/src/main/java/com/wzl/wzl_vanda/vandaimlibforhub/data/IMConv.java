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
    public final static String ATTR_TITLE = "title";
    public final static String ATTR_TEXT = "text";
    public final static String ATTR_SENDER = "sender";

    public String convId;
    public IMConvType type;
    public int unreadCount;

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

}
