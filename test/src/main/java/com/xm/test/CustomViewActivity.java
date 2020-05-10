package com.xm.test;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.xm.componet.CircleProgressView;
import com.xm.componet.ExpandTabView;
import com.xm.componet.StepView;

public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        final String tabs[] = {"tab1", "tab2", "tab3", "tab4"};
        ExpandTabView expandTabView = findViewById(R.id.expanded_tab_view);
        expandTabView.setAdapter(new ExpandTabView.Adapter() {
            @Override
            public int getCount() {
                return tabs.length;
            }

            @Override
            public View getTabView(int position, ViewGroup parentView) {
                return LayoutInflater.from(getApplicationContext()).inflate(R.layout.expand_tab_view, parentView, false);
            }

            @Override
            public String getTabViewDes(int position) {
                return tabs[position];
            }

            @Override
            public View getMenuContentView(int position, ViewGroup parentView) {
                return LayoutInflater.from(getApplicationContext()).inflate(R.layout.expand_menu_content_view, parentView, false);
            }

            @Override
            public void openMenu(int position, View tabView) {
                ((TextView) tabView).setTextColor(Color.RED);
            }

            @Override
            public void closeMenu(int position, View tabView) {
                ((TextView) tabView).setTextColor(Color.BLACK);
            }
        });
        final StepView stepView = findViewById(R.id.viewStep);
        final CircleProgressView circleProgressView = findViewById(R.id.circleProgressView);
        startStepViewAni(stepView);
        startCircleProgressViewAni(circleProgressView);
    }

    private void startCircleProgressViewAni(final CircleProgressView circleProgressView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleProgressView.setProgress((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void startStepViewAni(final StepView stepView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                stepView.setProgress((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}
