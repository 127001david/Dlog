package com.rightpoint.dlog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.tencent.mars.xlog.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DlogManagerService extends Service {

    private MyBinder mBinder;

    private RemoteCallbackList<IOnNeedFlushListener> mListeners =
            new RemoteCallbackList<>();

    public DlogManagerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new MyBinder();
        return mBinder;
    }

    class MyBinder extends IDlogManager.Stub {

        @Override
        public void registerListener(IOnNeedFlushListener listener) throws RemoteException {
            mListeners.register(listener);
        }

        @Override
        public void unRegisterListener(IOnNeedFlushListener listener) throws RemoteException {
            mListeners.unregister(listener);
        }

        @Override
        public List<String> prepareLogFiles() throws RemoteException {
            flush();
            return getLogFilesPath();
        }
    }

    private void flush() throws RemoteException {
        // 将缓存中的日志输出到文件，先刷新当前进程的，再刷新其他进程。
        Log.appenderFlush(true);

        int N = mListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNeedFlushListener listener = mListeners.getBroadcastItem(i);

            if (null != listener) {
                listener.onNeedFlushLogCache();
            }
        }
        mListeners.finishBroadcast();
    }

    private List<String> getLogFilesPath() {
        File logPath = new File(getApplication().getFilesDir() + "/xlog/log");
        List<String> filesPath = new ArrayList<>();

        if (logPath.exists()) {
            File[] files = logPath.listFiles();
            if (null != files) {
                for (File file : files) {
                    File[] processLogPath = file.listFiles();
                    if (null != processLogPath) {
                        for (File listFile : processLogPath) {
                            if (listFile.getName().endsWith(".xlog")) {
                                filesPath.add(listFile.getPath());
                            }
                        }
                    }
                }
            }
        }

        return filesPath;
    }
}
