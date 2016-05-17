package com.example.tangxb.giftestdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Tangxb on 2016/5/17.
 */
public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }

}
