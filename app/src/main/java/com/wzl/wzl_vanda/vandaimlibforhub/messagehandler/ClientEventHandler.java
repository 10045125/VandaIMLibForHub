package com.wzl.wzl_vanda.vandaimlibforhub.messagehandler;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;

/**
 * Created by Jam on 05/08/2015.
 */
public class ClientEventHandler extends AVIMClientEventHandler {
    @Override
    public void onConnectionPaused(AVIMClient avimClient) {
        Log.e("IM", "onConnectionPaused, clientId:" + avimClient.getClientId());
    }

    @Override
    public void onConnectionResume(AVIMClient avimClient) {
        Log.e("IM", "onConnectionResume, clientId:" + avimClient.getClientId());
    }
}
