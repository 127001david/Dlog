package com.rightpoint.dlogsimple.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rightpoint.dlog.Dlog;
import com.rightpoint.dlogsimple.TagConstant;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 自定义接收器，接收上传日志指令
 * 收到msg为1的指令时上传日志文件
 *
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Dlog.d(new String[]{TagConstant.JPUSH_REC, TagConstant.LOG_UPLOAD},
                    "receive msg at " + Dlog.getProcessName(context));
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            if ("1".equals(msg)) {
                Toast.makeText(context, "收到自定义消息", Toast.LENGTH_SHORT).show();

                // 准备上传文件 Dlog.prepareLogFiles() 方法返回的是日志文件的文件路径
                List<String> filesPath = Dlog.prepareLogFiles();

                Dlog.d(new String[]{"flushDlog", "logUpload"}, "flush all complete.");

                if (null == filesPath || 0 == filesPath.size()) {
                    Dlog.d(TagConstant.LOG_UPLOAD, "log files path is null");
                    return;
                }

                final OkHttpClient client = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

                for (String filePath : filesPath) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        Dlog.d(TagConstant.LOG_UPLOAD, file.getPath());
                        builder.addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("multipart/form-data"), file));
                        // TODO: 2020/5/28 test
                        break;
                    }
                }

                // TODO: 2020/6/1 替换为日志服务器url
                String url = "test";

                RequestBody requestBody = builder.build();
                final Request request = new Request.Builder()
                        .header("Authorization", "ClientID" + UUID.randomUUID())
                        .url(url)
                        .post(requestBody)
                        .build();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();

                            if (null != request && !response.isSuccessful()) {
                                Dlog.d("logUpload", "upload fail!");
                            } else {
                                Dlog.d("logUpload", "upload success");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Dlog.d("logUpload", "upload error:" + e.getMessage());
                        }
                    }
                }.start();
            }
        }
    }
}
