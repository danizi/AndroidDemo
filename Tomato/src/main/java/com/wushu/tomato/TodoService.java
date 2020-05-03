package com.wushu.tomato;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * 开启一个服务
 */
public class TodoService extends Service {

    private static final String TAG = "TodoService";
    public static final int MSG_ACTIVITY_ON_PAUSE = 0x00;
    public static final int MSG_ACTIVITY_ON_DESTROY = 0x01;
    public static final int MSG_ACTIVITY_ON_TICK = 0x02;
    public static final int MSG_ACTIVITY_ON_TICK_FINISH = 0x03;
    private TodoBinder todoBinder;
    private TodoService.MyCountDownTimer myCountDownTimer;
    private Context context;
    private long millisUntilFinished;
    private long countDownInterval = 1000;
    private long millisInFuture = 1000 * 10;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ACTIVITY_ON_PAUSE:
                    if (!todoBinder.isComplete()) {
                        startTodoActivity();
                    }
                    break;
                case MSG_ACTIVITY_ON_DESTROY:
                    stopSelf();
                    break;
                case MSG_ACTIVITY_ON_TICK:
                    todoBinder.setComplete(false);
                    millisUntilFinished = (long) msg.obj / 1000;
                    Log.d(TAG, "计时器所剩时间:" + millisUntilFinished);
                    break;
                case MSG_ACTIVITY_ON_TICK_FINISH:
                    todoBinder.setComplete(true);
                    Log.d(TAG, "计时器onFinish");
                    break;
            }
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.context = base;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return todoBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        todoBinder = new TodoBinder(handler);
        myCountDownTimer = new MyCountDownTimer(millisInFuture, countDownInterval, handler);
        todoBinder.setComplete(false);
        myCountDownTimer.start();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCountDownTimer();
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void startCountDownTimer() {
        if (null != myCountDownTimer && !todoBinder.isComplete() && millisUntilFinished == 1) {
            myCountDownTimer.start();
            Log.d(TAG, "myCountDownTimer start");
        }
    }

    private void startTodoActivity() {
        if (null != context) {
            Intent intent1 = new Intent(context, TodoActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            Log.d(TAG, "reStartTodoActivity");
        }
    }

    static class TodoBinder extends Binder {

        private boolean isComplete = false;
        private Handler handler;

        TodoBinder(Handler handler) {
            this.handler = handler;
        }

        void onPause() {
            if (null != handler) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_ACTIVITY_ON_PAUSE;
                handler.sendMessage(msg);
            }
        }

        void onDestroy() {
            if (null != handler) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_ACTIVITY_ON_DESTROY;
                handler.sendMessage(msg);
            }
        }

        boolean isComplete() {
            return isComplete;
        }

        void setComplete(boolean isComplete) {
            this.isComplete = isComplete;
        }
    }

    static class MyCountDownTimer extends CountDownTimer {

        private Handler handler;

        MyCountDownTimer(long millisInFuture, long countDownInterval, Handler handler) {
            super(millisInFuture, countDownInterval);
            this.handler = handler;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (null != handler) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_ACTIVITY_ON_TICK;
                msg.obj = millisUntilFinished;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onFinish() {
            if (null != handler) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_ACTIVITY_ON_TICK_FINISH;
                handler.sendMessage(msg);
            }
        }
    }
}
