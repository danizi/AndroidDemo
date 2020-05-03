package com.wushu.tomato;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 学习倒计时
 */
public class TodoActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private TodoService.TodoBinder todoServiceBinder;
    private ServiceConnection conn;
    private CircleProgressView circleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Log.d(TAG, "onCreate() activity:" + this);
        circleProgressView = findViewById(R.id.circleProgressView);
        //启动一个服务
        Intent todoServiceIntent = new Intent(this, TodoService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                todoServiceBinder = (TodoService.TodoBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
            }
        };
        bindService(todoServiceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != todoServiceBinder) {
            todoServiceBinder.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        if (null != todoServiceBinder && todoServiceBinder.isComplete()) {
            todoServiceBinder.onDestroy();
        }
    }
}
