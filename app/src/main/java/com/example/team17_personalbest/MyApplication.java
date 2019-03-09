package com.example.team17_personalbest;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Class that should fix error with firebase
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
