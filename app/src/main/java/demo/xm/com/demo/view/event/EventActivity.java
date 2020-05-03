package demo.xm.com.demo.view.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import demo.xm.com.demo.R;

/**
 * 什么是事件
 * Android 提供事件的对象是MotionEvent它包含了
 * MotionEvent.ACTION_DOWN  ; 手指按下
 * MotionEvent.ACTION_UP    ; 手指释放
 * MotionEvent.ACTION_MOVE  ; 手指移动
 * MotionEvent.ACTION_CANCEL; 取消事件
 *
 * 事件监听函数
 * setOnClickListener
 * setOnTouchListener
 * setOnLongClickListener
 *
 * 事件分发的函数包括如下：
 * dispatchTouchEvent   事件由此开始分发
 * InterceptTouchEvent  事件拦截ViewGroup特有的，可以在子View通过调用requestDisallowInterceptTouchEvent(false)进行拦截
 * onTouchEvent         事件处理
 *
 * 生命例子 产品总代理 销售商 零售商
 * 1 如果销售链还没有形成，零售商不能找总代理直接得到事件的销售权
 * 2 销售链形成后，再次来了时间，会直接沿着销售链走，不会再次询问
 * 3 当销售链形成守，底层对上层有返乡制约的权利
 * 4 拥有两次选择的机会
 *
 * Activity中放入一个装有子View的父容器
 * 不做任何操作Down事件分发函数执行顺序是
 * Activity    dispatchTouchEvent
 * ParentView  dispatchTouchEvent
 * ParentView  onInterceptTouchEvent
 * ChildView   dispatchTouchEvent
 * ChildView   onTouchEvent
 * ParentView  onTouchEvent
 * Activity    onTouchEvent
 *
 * childView同时设置setOnClickListener，setOnTouchListener返回false
 *
 * childView同时设置setOnClickListener，setOnTouchListener返回true
 * Activity   dispatchTouchEvent
 * ParentView dispatchTouchEvent
 * ParentView onInterceptTouchEvent
 * ChildView  dispatchTouchEvent
 * ChildView  OnTouchListener.onTouch 事件被消费
 *
 */
public class EventActivity extends AppCompatActivity {
    private ViewGroup parentView;
    private View childView;
    final String TAG = EventActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        parentView = findViewById(R.id.view_parent);
        childView = findViewById(R.id.view_child);
        setChildViewLister();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "Activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "Activity onTouchEvent");
        return super.onTouchEvent(event);
    }

    private void setChildViewLister(){
        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ChildView","ChildView onClick");
            }
        });
        /**
         * View.dispatchTouchEvent的源碼
         *  if (li != null && li.mOnTouchListener != null
         *                     && (mViewFlags & ENABLED_MASK) == ENABLED
         *                     && li.mOnTouchListener.onTouch(this, event)) {
         *                 result = true;
         *  }
         *
         *  if (!result && onTouchEvent(event)) {
         *                 result = true;
         *  }
         *  由源码可知如果Touch返回true那么onTouchEvent不会执行，在onTouchEvent有处理OnClickListener接口，所以“返回true click不会被触发”
         */
        childView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("ChildView", "ChildView setOnTouchListener");
                //返回true事件消费
                return false;
            }
        });
    }
}
