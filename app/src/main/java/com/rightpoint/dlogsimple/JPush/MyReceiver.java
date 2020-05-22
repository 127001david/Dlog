package com.rightpoint.dlogsimple.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rightpoint.dlog.Dlog;
import com.rightpoint.dlogsimple.TagConstant;

import cn.jpush.android.api.JPushInterface;

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
            Dlog.d(TagConstant.JPUSH_REC, "receive msg at " + Dlog.getProcessName(context));
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            if ("1".equals(msg)) {
                Toast.makeText(context, "收到自定义消息", Toast.LENGTH_SHORT).show();
                Dlog.upload(context);
            }
        }
    }
}
