package demo.xm.com.demo.view.custom;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;

import demo.xm.com.demo.R;

public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        final StepView stepView = findViewById(R.id.viewStep);
        final CircleProgressView circleProgressView = findViewById(R.id.circleProgressView);
        startStepViewAni(stepView);
        startCircleProgressViewAni(circleProgressView);
    }

    private void startCircleProgressViewAni(final CircleProgressView circleProgressView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleProgressView.setProgress((float)animation.getAnimatedValue());
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void startStepViewAni(final StepView stepView){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                stepView.setProgress((float)animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}
