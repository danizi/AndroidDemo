package demo.xm.com.demo.activity.frg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import demo.xm.com.demo.R;
import demo.xm.com.demo.activity.LifeActivity;

public class ExampleActivity extends LifeActivity implements ExampleFragment.ExampleListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        final Fragment exampleFragment = new ExampleFragment();
        fragmentTransaction.add(R.id.fragment, exampleFragment);
        fragmentTransaction.commitAllowingStateLoss();

        findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exampleFragment!=null){
                    ((ExampleFragment) exampleFragment).request();
                }else{
                    showLog("call request is fail,exampleFragment is null");
                }
            }
        });
    }

    @Override
    protected void showLog(String msg) {
        Log.d(ExampleActivity.class.getSimpleName(), msg);
    }

    @Override
    public void onResult(int result) {
        showLog("得到结果:" + String.valueOf(result));
    }
}
