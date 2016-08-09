package com.xxw.student.utils;

import android.app.Application;
import android.os.Handler;

/**
 * Created by DarkReal on 2016/7/28.
 */
public class Commonhandler extends Application {
    public static Handler comHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setComHandler(Handler comHandler) {
        Commonhandler.comHandler = comHandler;
    }


    public static Handler getComHandler() {
        return comHandler;
    }
}
