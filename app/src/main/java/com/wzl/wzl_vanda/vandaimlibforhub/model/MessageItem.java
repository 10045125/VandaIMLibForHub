package com.wzl.wzl_vanda.vandaimlibforhub.model;

import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;

/**
 * Created by wzl_vanda on 15/8/3.
 */
public class MessageItem extends DemoItem{

    public AVIMTypedMessage avimTypedMessage;


    public MessageItem() {
        super();
    }

    public MessageItem(AVIMTypedMessage message) {
        super();
        this.avimTypedMessage = message;
    }

}
