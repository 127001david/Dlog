package com.rightpoint.dlogsimple;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rightpoint.dlog.Dlog;

public class RemoteService extends Service {

    public RemoteService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Dlog.d(TagConstant.TAG_PROCESS, getClass().getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
