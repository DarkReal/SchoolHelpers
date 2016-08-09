package com.xxw.student.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Administrator on 2016/5/20 08:00.
 * Alt + Shift + Up/Down 上下移动代码
 * Alt + Ctrl + O 清除无效包引用
 * Ctrl + Alt + L 格式化代码
 * Ctrl + Y 删除行
 * Ctrl + Shift + / 注释多行代码
 * Ctrl + / 注释单行代码
 * Alt+回车 导入包,自动修正
 * Ctrl +Alt +M 提取方法
 * Ctrl +Alt +F 提取全局变量
 * Ctrl +Alt +V 提取局部变量
 * 全局的一个handler
 */
public class getHandler extends Application {

    public static Handler mHandler = null;
    private static Context mContext = null;
    private static int mMainThreadId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mMainThreadId = android.os.Process.myTid();
        mHandler=new Handler();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }
}
