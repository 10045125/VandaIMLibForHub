package com.wzl.wzl_vanda.viewtypelibrary.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class AppApplication extends Application{

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
