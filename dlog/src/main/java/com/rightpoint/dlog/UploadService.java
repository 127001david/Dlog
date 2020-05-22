package com.rightpoint.dlog;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.tencent.mars.xlog.Log;

/**
 * 用于上传日志文件到服务器
 * @author DavidWang
 */
public class UploadService extends IntentService {
    private final String TAG = "UploadService";

    public UploadService() {
        super("");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Dlog.d(TAG, "startId: " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Dlog.d(TAG, "process: " + Dlog.getProcessName(this) + " start upload");

        // 将缓存中的日志输出到文件
        Log.appenderFlush(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Dlog.d(TAG, "onDestroy");
    }
}
