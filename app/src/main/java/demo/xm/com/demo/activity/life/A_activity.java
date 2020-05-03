package demo.xm.com.demo.activity.life;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import demo.xm.com.demo.R;
import demo.xm.com.demo.activity.LifeActivity;

public class A_activity extends LifeActivity {

    private static final boolean LOG_FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_activity);
        findViewById(R.id.btn_start_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(A_activity.this,B_activity.class));
            }
        });
    }

    protected void showLog(String msg) {
        if (LOG_FLAG) {
            Log.d(LifeActivity.class.getSimpleName(), "A - "+msg);
        }
    }
}
