package demo.xm.com.demo.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import demo.xm.com.demo.R;

/**
 * 圆形进度条
 */
public class CircleProgressView extends View {

    private State mState;
    private int mRadius;
    private Paint mOriginalPaint;
    private Paint mProgressPaint;
    private Paint mProgressDesPaint;
    private int mCircleWidth = 30;
    private int mOriginalColor = Color.GRAY;
    private int mProgressColor = Color.RED;
    private String mProgressDes = "0";
    private float mProgress = 0.1f;
    private CircleProgressListenter mCircleProgressListenter;

    enum State {
        PROGRESS,
        COMPLETE,
        PAUSE,
        RESUME,
    }

    interface CircleProgressListenter {
        void onPrgress();

        void onComplete();

        void onPause();
    }

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.CircleProgressView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CircleProgressView_circleSize:
                    mCircleWidth = a.getInteger(attr, 30);
                    break;
                case R.styleable.CircleProgressView_originalColor:
                    mOriginalColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.CircleProgressView_progressColor:
                    mProgressColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CircleProgressView_progress:
                    mProgress = a.getFloat(attr, 0);
                    break;
            }
        }
        a.recycle();

        mState = State.PAUSE;

        //初始化画笔
        mOriginalPaint = new Paint();
        mOriginalPaint.setStyle(Paint.Style.STROKE);
        mOriginalPaint.setAntiAlias(true);
        mOriginalPaint.setColor(mOriginalColor);
        mOriginalPaint.setStrokeWidth(mCircleWidth);

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mCircleWidth);

        mProgressDesPaint = new Paint();
        mProgressDesPaint.setStrokeWidth(50);
        mProgressDesPaint.setColor(mProgressColor);
        mProgressDesPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1 画一个原始圆圈 圆圈的宽度 原始颜色
        drawCircle(canvas, mRadius = (int) (getWidth() / 2 - mOriginalPaint.getStrokeWidth()), mOriginalPaint);
        // 2 画一个进度圆圈 圆圈的宽度 进度颜色 进度
        drawProgress(canvas, mProgress, mProgressPaint);
        // 3 画一个进度描述 颜色 进度文字描述
        drawProgressDes(canvas, mProgressDesPaint);
    }

    private void drawCircle(Canvas canvas, int radius, Paint paint) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }

    private void drawProgress(Canvas canvas,float progress, Paint paint) {
        int Angle = 270;
        int sweepAngle = (int) (360 * progress);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = (int) (centerX - paint.getStrokeWidth());
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(oval, Angle, sweepAngle, false, paint);
    }

    private void drawProgressDes(Canvas canvas, Paint paint) {
        mProgressDes = Integer.toString((int) (100 * mProgress)) + "%";
        canvas.drawText(mProgressDes, (getWidth() - getTextBounds().width()) / 2, getHeight() / 2 + getTextBaseline(), paint);
    }

    private Rect getTextBounds() {
        Rect bounds = new Rect();
        mProgressDesPaint.getTextBounds(mProgressDes, 0, mProgressDes.length(), bounds);
        return bounds;
    }

    private int getTextBaseline() {
        int baseline;
        Paint.FontMetrics fontMetrics = mProgressDesPaint.getFontMetrics();
        baseline = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        return baseline;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), getMeasureHeight(heightMeasureSpec));
    }

    private int getMeasureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = 200 + getPaddingLeft() + getPaddingRight();
        switch (mode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                width = size;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(width, size);
                break;
        }
        return width;
    }

    private int getMeasureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height = 200 + getPaddingTop() + getPaddingBottom();
        switch (mode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = size;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(height, size);
                break;
        }
        return height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setCircleProgressListenter(CircleProgressListenter listener) {
        this.mCircleProgressListenter = listener;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        postInvalidate();
    }
}
