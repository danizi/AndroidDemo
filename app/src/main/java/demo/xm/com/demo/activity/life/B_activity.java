package demo.xm.com.demo.activity.life;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import demo.xm.com.demo.R;
import demo.xm.com.demo.activity.LifeActivity;

public class B_activity extends LifeActivity {

    private static final boolean LOG_FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_activity);
    }

    protected void showLog(String msg) {
        if (LOG_FLAG) {
            Log.d(LifeActivity.class.getSimpleName(), "B - "+msg);
        }
    }
}
