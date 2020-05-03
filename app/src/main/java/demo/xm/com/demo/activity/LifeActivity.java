package demo.xm.com.demo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import demo.xm.com.demo.R;
import demo.xm.com.demo.activity.frg.ExampleActivity;
import demo.xm.com.demo.activity.frg.ExampleFragment;
import demo.xm.com.demo.activity.life.A_activity;
import demo.xm.com.demo.activity.life.ChangeActivity;

/**
 * （一） Activity 生命周期分为常规和非常规
 * 1 启动Activity、点击home、点击back属于常规
 * 2 Activity 被系统回收、横竖切屏属于非常规
 * <p>
 * （二）Activity状态
 * 前台可见有焦点、前台可见无焦点、后台不可见状态
 * <p>
 * （三）Activity七个声明周期函数
 * onCreate   窗口创建。
 * onDestroy  窗口销毁。
 * onStart    窗口正在启动，但不可见。
 * onStop     窗口正在停止，不可见。
 * onPause    窗口处于后台暂停。
 * onResume   窗口处于前台显示，可与用于交互。注意：不要做耗时操作，会影响页面显示
 * onRestart  窗口从后台转变成前台。
 *
 * <p>
 * （四）个场景生命周期调用
 * 1 启动窗口点击Home，窗口从可见到后台
 * 启动窗口     : onCreate -> onStart -> onResume
 * 点击Home     : onPause -> onStop
 * 点击应用图标 : onRestart -> onStart()->onResume
 * <p>
 * 2 启动窗口点击back，窗口销毁
 * 启动窗口     : onCreate -> onStart -> onResume
 * 点击back     : onPause -> onStop ->onDestroy
 * <p>
 * 3 启动窗口并弹框
 * 弹框         : 窗口的声明周期没有发生改变
 * <p>
 * 4 启动窗口A，再启动窗口B，再点击回退
 * 启动窗口A       :(A)onCreate -> (A)onStart -> (A)onResume
 * 再启动窗口B     :(A)onPause -> (B)onCreate -> (B)onStart -> (B)onResume -> (A)onStop
 * 窗口B点击back键 :(B)onPause -> (A)onRestart() -> (A)onStart -> (A)onResume ->(B)onStop -> (B)onDestroy
 * <p>
 * 5 窗口切换横竖屏幕
 *
 * （五）android:configChanges属性
 *  1 不设置configChanges属性，生命周期重新调用，切横屏时会执行一次，切竖屏时会执行两次。
 *  2 设置configChanges属性为“orientation”，生命周期重新调用，切横竖屏都会执行一次。
 *  3 设置configChanges属性为“keyboardHidden|orientation|screenSize”，生命周期“不”重新调用，只会执行onConfigurationChanged方法。
 *
 * （六）onSaveInstanceState、onRestoreInstanceState
 *       setResult、finish
 */
public class LifeActivity extends AppCompatActivity {

    private static final boolean LOG_FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        showLog("onCreate savedInstanceState:" + savedInstanceState);

        findViewById(R.id.btn_startA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifeActivity.this, A_activity.class));
            }
        });
        findViewById(R.id.btn_show_dlg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlg();
            }
        });
        findViewById(R.id.btn_screen_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifeActivity.this, ChangeActivity.class));
            }
        });
        findViewById(R.id.btn_frg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LifeActivity.this,ExampleActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showLog("onDestroy()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLog("onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showLog("onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showLog("onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLog("onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLog("onResume()");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showLog("onNewIntent()");
    }

    @Override
    public boolean hasWindowFocus() {
        boolean hasWindowFocus = super.hasWindowFocus();
        showLog("hasWindowFocus : " + hasWindowFocus);
        return hasWindowFocus;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        showLog("onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showLog("onRestoreInstanceState()");
    }

    protected void showLog(String msg) {
        if (LOG_FLAG) {
            Log.d(LifeActivity.class.getSimpleName(), msg);
        }
    }

    protected void showDlg() {
        new AlertDialog.Builder(this).setTitle("弹框").create().show();
    }
}
