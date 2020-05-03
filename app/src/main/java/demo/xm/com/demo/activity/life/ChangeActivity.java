package demo.xm.com.demo.activity.life;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import demo.xm.com.demo.R;
import demo.xm.com.demo.activity.LifeActivity;

/**
 * 屏幕横竖切屏查看生命周期
 */
public class ChangeActivity extends LifeActivity {

    private static final boolean LOG_FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
    }

    /**
     * 请自行旋转手机
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showLog("onConfigurationChanged : 横屏");
        } else {
            showLog("onConfigurationChanged : 竖屏");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        showLog("onSaveInstanceState()");
        outState.putString("save", "我是保存的数据");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showLog("onRestoreInstanceState()");
        if (savedInstanceState != null) {
            String save = savedInstanceState.getString("save");
            showLog("save:" + save);
        }
    }

    protected void showLog(String msg) {
        if (LOG_FLAG) {
            Log.d(ChangeActivity.class.getSimpleName(), msg);
        }
    }
}
