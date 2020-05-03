package demo.xm.com.demo.view.basic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import demo.xm.com.demo.R;

/**
 * View坐标系
 * 由状态栏、标题栏、和内容页面contentView组成。屏幕坐标系的“起始点在屏幕左上顶点”视图坐标系是以父容器作为参考的。
 * <p>
 * View的基本信息
 * event.getX()             以“自身View”作为作为参考物，左边距距离
 * event.getY()             以“自身View”作为作为参考物，上边距距离
 * event.getRawX()          以“手机屏幕”作为作为参考物，左边界距离
 * event.getRawY()          以“手机屏幕”作为作为参考物，上边界距离
 * view.getLeft()           距离父容器左边距离
 * view.getTop()            距离父容器顶部距离
 * view.getRight()          距离父容器右边距离
 * view.getBottom()         距离父容器底部距离
 * view.getTranslationX()   view在x轴的偏移量，初始值为0
 * view.getTranslationY()   view在y轴的偏移量，初始值为0
 * view.getWidth()          view的宽度
 * view.getHeight()         view的高度
 * view.getMeasuredWidth()  view的测量宽度
 * view.getMeasuredHeight() view的测量高度
 * <p>
 * 他们之间的关系
 * getX() = getLeft() + getTranslationX()
 * getY() = getTop() + getTranslationY()
 */
public class BasicActivity extends AppCompatActivity {
    private final String TAG = "BasicActivity";
    private ViewGroup parentView;
    private View childView;
    private float startX = 0, startY = 0;
    private float endX = 0, endY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        parentView = findViewById(R.id.view_parent);
        childView = findViewById(R.id.view_child);
        Log.d(TAG, "->>onCreate中获取View的宽高");
        Log.d(TAG, "->>getWidth : " + parentView.getWidth() + " getHeight   :" + parentView.getHeight());
        Log.d(TAG, "->>getMeasuredWidth : " + parentView.getMeasuredWidth() + " getMeasuredHeight :" + parentView.getMeasuredHeight());
        findViewById(R.id.btn_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofFloat(parentView, "translationX", 100f);
                animation.setDuration(1000);
                animation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        Log.d(TAG, "->>动画 移动前偏移量" + "getTranslationX:" + parentView.getTranslationX() + " - " + "getTranslationY:" + parentView.getTranslationY());
                        Log.d(TAG, "->>getLeft  : " + parentView.getLeft() + "  getTop :" + parentView.getTop());
                        Log.d(TAG, "->>getX     : " + parentView.getX() + "     getY   :" + parentView.getY());
                        Log.d(TAG, "->>left     : " + parentView.getLeft() + "  top    : " + parentView.getTop() + " right : " + parentView.getRight() + " bottom : " + parentView.getBottom());
                        Log.d(TAG, "->>getWidth : " + parentView.getWidth() + " getHeight   :" + parentView.getHeight());
                        Log.d(TAG, "->>getMeasuredWidth : " + parentView.getMeasuredWidth() + " getMeasuredHeight :" + parentView.getMeasuredHeight());
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        /**
                         * 他们之间的关系验证
                         * getX() = getLeft() + getTranslationX()
                         * getY() = getTop() + getTranslationY()
                         */
                        Log.d(TAG, "->>动画 移动后偏移量" + "getTranslationX:" + parentView.getTranslationX() + " - " + "getTranslationY:" + parentView.getTranslationY());
                        Log.d(TAG, "->>getLeft  : " + parentView.getLeft() + "  getTop :" + parentView.getTop());
                        Log.d(TAG, "->>getX     : " + parentView.getX() + "     getY   :" + parentView.getY());
                        Log.d(TAG, "->>left     : " + parentView.getLeft() + "  top    : " + parentView.getTop() + " right : " + parentView.getRight() + " bottom : " + parentView.getBottom());
                        Log.d(TAG, "->>getWidth : " + parentView.getWidth() + " getHeight   :" + parentView.getHeight());
                        Log.d(TAG, "->>getMeasuredWidth : " + parentView.getMeasuredWidth() + " getMeasuredHeight :" + parentView.getMeasuredHeight());
                    }
                });
                animation.start();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "->>onWindowFocusChanged中获取View的宽高");
        Log.d(TAG, "->>getWidth : " + parentView.getWidth() + " getHeight   :" + parentView.getHeight());
        Log.d(TAG, "->>getMeasuredWidth : " + parentView.getMeasuredWidth() + " getMeasuredHeight :" + parentView.getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                Log.d(TAG,"->> Activity中各参考系的坐标");
                Log.d(TAG,"getRawX : "+event.getRawX() + "getRawY : "+event.getRawY());
                Log.d(TAG,"getX    : "+event.getX() + "   getY : "+event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                endX = event.getRawX() - startX;
                endY = event.getRawY() - startY;
                //Log.d(TAG, "endX:" + endX + " endY:" + endY);
                parentView.setLeft((int) (parentView.getLeft() + endX));
                parentView.setTop((int) (parentView.getTop() + endY));
                parentView.setRight((int) (parentView.getRight() + endX));
                parentView.setBottom((int) (parentView.getBottom() + endY));
                startX = event.getRawX();
                startY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                startX = 0;
                startY = 0;
                break;
        }
        return super.onTouchEvent(event);
    }
}
