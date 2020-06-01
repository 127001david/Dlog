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
            Dlog.d(new String[]{"flushDlog", "logUpload"}, "flush all complete.");
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
        File f = new File(getApplication().getFilesDir() + "/xlog/");
        List<String> filesPath = new ArrayList<>();

        if (f.exists()) {
            File[] files = f.listFiles();
            if (null != files) {
                for (File file : files) {
                    File[] files1 = file.listFiles();
                    if (null != files1) {
                        for (File listFile : files1) {
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
