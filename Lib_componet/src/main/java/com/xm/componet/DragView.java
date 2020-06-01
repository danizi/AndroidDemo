package com.xm.componet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

/**
 * 贝塞尔 拖拽控件
 * 1 绘制两个圆 一个固定圆 一个移动的圆 目标bitmap
 * 2 随着两圆距离的变大 固定圆逐渐变小直到消失
 * 3 计算贝塞尔路径、贝塞尔控制点
 * 4 动画效果当手指释放，移动距离小于最小控制距离回弹 ，移动距离大于最小控制距离爆炸效果。
 */
public class DragView extends View {

    /**
     * 拖拽移动点,绘制的大圆原点坐标
     */
    private PointF dragPointF;
    /**
     * 拖拽固定点,绘制的小圆原点坐标
     */
    private PointF fixationPointF;
    /**
     * 大圆半径
     */
    private float dragRadius = 50;
    /**
     * 小圆半径
     */
    private float fixationRadius = 10;
    /**
     * 大圆与小圆最大距离，即是拖拽消失的最大距离
     */
    private final float maxDistance = 50;
    /**
     * 画笔
     */
    private Paint dragPaint;
    private Paint fixationPaint;
    private DragViewListener listener;
    /**
     * 被拖拽的目标View
     */
    private View target;
    private Bitmap targetBmp;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragPointF = new PointF();
        fixationPointF = new PointF();
        dragPaint = new Paint();
        dragPaint.setAntiAlias(true);
        dragPaint.setStyle(Paint.Style.FILL);
        dragPaint.setColor(Color.RED);
        fixationPaint = dragPaint;
    }

    /**
     * 绑定目标拖拽View
     *
     * @param targetView 目标拖拽View
     * @param listener   拖拽监听
     */
    public static void attach(View targetView, DragViewListener listener) {
        targetView.setOnTouchListener(new TargetViewListener(targetView, listener));
    }

    /**
     * 拖拽View监听
     */
    public interface DragViewListener {
        /**
         * 开始拖拽
         */
        void onStart();

        /**
         * 拖拽中
         */
        void onDrag();

        /**
         * 释放拖拽
         */
        void onDismiss();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制一个小圆
        Path bezeierPath = getBezeierPath();
        if (null != bezeierPath) {
            canvas.drawCircle(fixationPointF.x, fixationPointF.y, fixationRadius, fixationPaint);
            canvas.drawPath(bezeierPath, dragPaint);
        }
        // 绘制一个大圆
        canvas.drawCircle(dragPointF.x, dragPointF.y, dragRadius, dragPaint);
        // 绘制一个目标图片
        if (targetBmp != null)
            canvas.drawBitmap(targetBmp, dragPointF.x - targetBmp.getWidth() / 2, dragPointF.y - targetBmp.getHeight() / 2, null);
    }

    private void handleUp() {
        if (null != getBezeierPath()) {
            //回弹效果
            final PointF end = new PointF(dragPointF.x, dragPointF.y);
            final PointF offset = new PointF(dragPointF.x - fixationPointF.x, dragPointF.y - fixationPointF.y);
            Log.d("DragView", "start  :" + dragPointF.toString());
            Log.d("DragView", "offset :" + offset.toString());
            ValueAnimator animator = ObjectAnimator.ofFloat(1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    //dragPointF.x = end.x - offset.x * percent;
                    //dragPointF.y = end.y - offset.y * percent;
                    //refreshDragPoint(percent, end, offset);
                    //刷新
                    refreshDragPoint(end.x - offset.x * percent, end.y - offset.y * percent);
                    Log.d("DragView", "update" + dragPointF.toString() + "percent:" + percent);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //恢复原状态
                    recovery();
                }
            });
            animator.setDuration(1000);
            animator.start();
            animator.setInterpolator(new OvershootInterpolator(3f));
        }
    }

    private void recovery() {
        DragView.this.setVisibility(GONE);
        target.setVisibility(VISIBLE);
    }

    private void refreshDragPoint(float x, float y) {
        dragPointF.set(x, y);
        invalidate();
    }

    private void handleDown(float x, float y) {
        dragPointF.set(x, y);
        fixationPointF.set(x, y);
    }

    private float distance(PointF dragPointF, PointF fixationPointF) {
        return (float) Math.sqrt(Math.pow(dragPointF.x - fixationPointF.x, 2) + Math.pow(dragPointF.y - fixationPointF.y, 2));
    }

    private Path getBezeierPath() {
        int distance = (int) distance(dragPointF, fixationPointF);
        Log.d("tag", "distance->" + distance);
        fixationRadius = (int) (maxDistance - distance / 5);
        if (fixationRadius < 1) {
            return null;
        }
        Path bezeierPath = new Path();

        // 求∠A
        float x = dragPointF.x - fixationPointF.x;
        float y = dragPointF.y - fixationPointF.y;
        float tanA = y / x;
        double arcTanA = Math.atan(tanA);
        // 求A的坐标 cosA = b / c    cosA = x / r
        float ax = (float) (fixationPointF.x + fixationRadius * Math.sin(arcTanA));
        float ay = (float) (fixationPointF.y - fixationRadius * Math.cos(arcTanA));

        // 求B的坐标
        float bx = (float) (dragPointF.x + dragRadius * Math.sin(arcTanA));
        float by = (float) (dragPointF.y - dragRadius * Math.cos(arcTanA));

        // 求C的坐标
        float cx = (float) (dragPointF.x - dragRadius * Math.sin(arcTanA));
        float cy = (float) (dragPointF.y + dragRadius * Math.cos(arcTanA));

        // 求D的坐标
        float dx = (float) (fixationPointF.x - fixationRadius * Math.sin(arcTanA));
        float dy = (float) (fixationPointF.y + fixationRadius * Math.cos(arcTanA));

        // 求控制点坐标
        float controlx = (float) ((dragPointF.x + fixationPointF.x) * 0.5);
        float controly = (float) ((dragPointF.y + fixationPointF.y) * 0.5);

        // 获得贝塞尔路径
        bezeierPath.moveTo(ax, ay);
        bezeierPath.quadTo(controlx, controly, bx, by);
        bezeierPath.lineTo(cx, cy);
        bezeierPath.quadTo(controlx, controly, dx, dy);
        bezeierPath.close();
        return bezeierPath;
    }

    private void setTargetView(View target) {
        this.target = target;
    }

    private void setTargetBmp(Bitmap bmp) {
        this.targetBmp = bmp;
        invalidate();
    }

    private static class TargetViewListener implements OnTouchListener {
        /**
         * 拖拽贝塞尔View
         */
        private DragView dragView;
        /**
         * 需要被拖拽的View
         */
        private View target;
        /**
         * 拖拽View回调
         */
        private DragViewListener listener;

        TargetViewListener(View view, DragViewListener listener) {
            this.target = view;
            this.listener = listener;
        }

        private void addToWindow(Context context, View view) {
            if (dragView == null) {
                target = view;
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.format = PixelFormat.TRANSLUCENT;
                dragView = new DragView(context);
                wm.addView(dragView, params);
            }
            dragView.setVisibility(VISIBLE);
            target.setVisibility(GONE);
        }

        private Bitmap viewToBmp(View view) {
            view.setDrawingCacheEnabled(true);
            return Bitmap.createBitmap(view.getDrawingCache());
        }

        private int getStatusBarHeight(Context context) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
            return dip2px(25, context);
        }

        private int dip2px(int i, Context context) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, context.getResources().getDisplayMetrics());
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //目标View添加到window中
                    addToWindow(target.getContext(), target);
                    //按下事件传回到拖拽贝塞尔View中处理
                    dragView.setTargetView(target);
                    dragView.setTargetBmp(viewToBmp(target));
                    GetPoint getPoint = new GetPoint().invoke();
                    dragView.handleDown(getPoint.getX(), getPoint.getY());
                    listener.onStart();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //拖拽过程贝塞尔View不停的更新处理，贝塞尔View位置不断变化
                    //dragView.handleMove(event);
                    dragView.refreshDragPoint(event.getRawX(), event.getRawY());
                    listener.onDrag();
                    break;
                case MotionEvent.ACTION_UP:
                    //事件释放处理，初始状态恢复，回弹效果，爆炸效果
                    dragView.handleUp();
                    listener.onDismiss();
                    break;
            }
            return true;
        }

        private class GetPoint {
            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public float getY() {
                return y;
            }

            public GetPoint invoke() {
                int[] location = new int[2];
                target.getLocationOnScreen(location);
                x = location[0] + target.getWidth() / 2;
                y = location[1] + target.getHeight() / 2 - getStatusBarHeight(dragView.getContext());
                return this;
            }
        }
    }
}
