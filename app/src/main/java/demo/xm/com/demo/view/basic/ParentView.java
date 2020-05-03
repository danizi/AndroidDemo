package demo.xm.com.demo.view.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ParentView extends FrameLayout {
    private final String TAG = "ParentView";

    public ParentView(Context context) {
        super(context);
    }

    public ParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"->> ParentView中各参考系的坐标");
                Log.d(TAG, "getRawX : " + event.getRawX() + "getRawY : " + event.getRawY());
                Log.d(TAG, "getX    : " + event.getX() + "   getY : " + event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }
}
