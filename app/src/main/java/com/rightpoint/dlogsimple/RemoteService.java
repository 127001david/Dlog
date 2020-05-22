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

        new Thread() {
            @Override
            public void run() {
                super.run();

                for (int i = 0; ; i++) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Dlog.d(TagConstant.TAG_PROCESS, i + "");
                }
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
