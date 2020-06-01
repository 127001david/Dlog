package com.rightpoint.dlogsimple;

import android.app.Application;

import com.rightpoint.dlog.Dlog;

import cn.jpush.android.api.JPushInterface;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/5/20 3:03 PM 
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Dlog.getInstance().init(this);
        Dlog.d(TagConstant.TAG_PROCESS, Dlog.getProcessName(this));

        // JPush仅会在主进程初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        Dlog.d("registerID", JPushInterface.getRegistrationID(this));
    }
}
