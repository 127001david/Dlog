package com.rightpoint.dlogsimple;

import android.app.Application;

import com.rightpoint.dlog.Dlog;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/5/20 3:03 PM 
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Dlog.init(this);
        Dlog.d(TagConstant.TAG_PROCESS, Dlog.getProcessName(this));
    }
}
