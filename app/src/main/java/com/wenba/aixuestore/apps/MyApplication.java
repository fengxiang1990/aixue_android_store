package com.wenba.aixuestore.apps;

import android.app.Application;

import com.wenba.aixuestore.network.OkHttpKotlinHelper;

/**
 * Created by wenba on 2017/10/19.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpKotlinHelper.init(this);
    }
}
