package com.wushu.tomato.module.todo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wushu.tomato.R;
import com.wushu.tomato.utils.DateUtil;

import static com.wushu.tomato.constant.Constant.INTENT_PUT_EXTRA_TODO_TOMATO_BEAN;

/**
 * 学习倒计时
 */
public class TodoActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private TodoService.TodoBinder todoServiceBinder;
    private ServiceConnection conn;
    private CircleProgressView circleProgressView;
    private TodoTomatoBean todoTomatoBean;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Log.d(TAG, "onCreate() activity:" + this);
        try {
            //findView
            circleProgressView = findViewById(R.id.circleProgressView);
            todoTomatoBean = (TodoTomatoBean) getIntent().getSerializableExtra(INTENT_PUT_EXTRA_TODO_TOMATO_BEAN);
            if (null != todoTomatoBean) {
                Log.d(TAG, "获取传递过来的参数 : " + todoTomatoBean.toString());
                circleProgressView.setMax(todoTomatoBean.getProgressMax());
                circleProgressView.setProgress(todoTomatoBean.getProgress());
                circleProgressView.setProgressDes(todoTomatoBean.getProgressDes());
            }

            //启动一个服务
            Intent todoServiceIntent = new Intent(this, TodoService.class);
            if (null != todoTomatoBean) {
                todoServiceIntent.putExtra(INTENT_PUT_EXTRA_TODO_TOMATO_BEAN, todoTomatoBean);
            }
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.d(TAG, "onServiceConnected");
                    todoServiceBinder = (TodoService.TodoBinder) service;
                    todoServiceBinder.setDownTimerListener(new TodoService.CountDownTimerListener() {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            circleProgressView.setProgress(millisUntilFinished);
                            circleProgressView.setProgressDes(DateUtil.msToDate((int) millisUntilFinished));
                            circleProgressView.postInvalidate();
                        }

                        @Override
                        public void onFinish() {
                            circleProgressView.setProgressDes(DateUtil.msToDate(0));
                            circleProgressView.setProgress(0);
                            circleProgressView.postInvalidate();
                        }
                    });
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.d(TAG, "onServiceDisconnected");
                }
            };
            bindService(todoServiceIntent, conn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (null != conn) {
            unbindService(conn);
        }
        if (null != todoServiceBinder && todoServiceBinder.isComplete()) {
            todoServiceBinder.onDestroy();
        }
    }
}
