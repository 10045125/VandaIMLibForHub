package com.wzl.wzl_vanda.vandaimlibforhub.model;

import com.avos.avoscloud.AVObject;

/**
 * Created by wzl_vanda on 15/8/6.
 */
public class ForMeConversationInfo {

    public static final String CONVERSATIONID = "conversationId";
    public static final String NICKNAME = "nickname";
    public static final String USERID = "userId";
    public static final String OBJECTID = "objectId";


    public String url;
    public String conversationId;
    public String nickname;
    public String userId;
    public String objectId;



    public ForMeConversationInfo(AVObject avObject){
        init(avObject);
    }

    private void init(AVObject avObject){
        url = avObject.getAVFile(User.AVATAR).getUrl();
        conversationId = avObject.getString(CONVERSATIONID);
        nickname = avObject.getString(NICKNAME);
        userId = avObject.getString(USERID);
        objectId = avObject.getObjectId();
    }
}
