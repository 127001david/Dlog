package com.rightpoint.dlogsimple;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rightpoint.dlog.Dlog;

public class RemoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_activity);
        Dlog.d(TagConstant.TAG_PROCESS, getClass().getSimpleName());
    }
}
