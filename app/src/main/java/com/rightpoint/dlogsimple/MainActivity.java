package com.rightpoint.dlogsimple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rightpoint.dlog.Dlog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_log_d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dlog.d(new String[]{TagConstant.TAG_PROCESS, "s"}, "");
            }
        });

        findViewById(R.id.btn_remote_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RemoteActivity.class));
            }
        });

        findViewById(R.id.btn_remote_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, RemoteService.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Dlog.appenderClose();
    }
}
