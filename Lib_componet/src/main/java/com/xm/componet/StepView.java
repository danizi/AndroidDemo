package com.xm.componet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * 仿造QQ步数进度View
 */
public class StepView extends View {

    private int mCircleSize = 30;
    private int mCircleTextSize = 30;
    private Paint mBgArcPaint;
    private Paint mProgressArcPaint;
    private TextPaint mStepPaint;
    private int mBgArcColor = Color.GRAY;
    private int mProgressArcColor = Color.RED;
    private int mStepColor = mProgressArcColor;
    private int mStartAngle = 160;
    private int mSweepAngle = 220;
    private float mProgress = 0;
    private String mProgressDes = "0步";

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.StepView);
        int attr;
        for (int i = 0; i < a.getIndexCount(); i++) {
            attr = a.getIndex(i);
            if (attr == R.styleable.StepView_arcSize) {
                mCircleSize = a.getInteger(attr, 30);

            } else if (attr == R.styleable.StepView_arcTextSize) {
                mCircleTextSize = a.getInteger(attr, 30);

            } else if (attr == R.styleable.StepView_bgArcColor) {
                mBgArcColor = a.getColor(attr, Color.GRAY);

            } else if (attr == R.styleable.StepView_progressArcColor) {
                mProgressArcColor = a.getColor(attr, Color.RED);
                mStepColor = mProgressArcColor;

            } else if (attr == R.styleable.StepView_arcProgress) {
                mProgress = a.getFloat(attr, 0);

            }
        }
        a.recycle();

        //初始化画笔
        mBgArcPaint = new Paint();
        mBgArcPaint.setColor(mBgArcColor);
        mBgArcPaint.setAntiAlias(true);
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mCircleSize);
        mBgArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgArcPaint.setStrokeJoin(Paint.Join.ROUND);

        mProgressArcPaint = new Paint();
        mBgArcPaint.setColor(mProgressArcColor);
        mProgressArcPaint.setAntiAlias(true);
        mProgressArcPaint.setStyle(Paint.Style.STROKE);
        mProgressArcPaint.setStrokeWidth(mCircleSize);
        mProgressArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressArcPaint.setStrokeJoin(Paint.Join.ROUND);

        mStepPaint = new TextPaint();
        mStepPaint.density = getResources().getDisplayMetrics().density;
        mStepPaint.setColor(mStepColor);
        mStepPaint.setAntiAlias(true);
        mStepPaint.setTextSize(mCircleTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1 绘制背景圆弧
        drawBgArc(canvas);
        //2 绘制当前进度
        drawProgressArc(canvas);
        //3 绘制步数
        drawStep(canvas);
    }


    private void drawBgArc(Canvas canvas) {
        canvas.save();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = (int) (centerX - mBgArcPaint.getStrokeWidth());
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(oval, mStartAngle, mSweepAngle, false, mBgArcPaint);
        canvas.restore();
    }

    private void drawProgressArc(Canvas canvas) {
        canvas.save();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = (int) (centerX - mProgressArcPaint.getStrokeWidth());
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(oval, mStartAngle, mProgress * mSweepAngle, false, mProgressArcPaint);
        canvas.restore();
    }

    private void drawStep(Canvas canvas) {
        canvas.save();
        mProgressDes = mProgress * 100 + "步";
        canvas.drawText(mProgressDes, (getWidth() - getTextBounds().width()) / 2 + mStepPaint.getStrokeWidth(), getHeight() / 2 + getTextBaseline(), mStepPaint);
        canvas.restore();
    }

    private Rect getTextBounds() {
        Rect bounds = new Rect();
        mStepPaint.getTextBounds(mProgressDes, 0, mProgressDes.length(), bounds);
        return bounds;
    }

    private int getTextBaseline() {
        int baseline;
        Paint.FontMetrics fontMetrics = mStepPaint.getFontMetrics();
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
//    private int measureWidth(int widthMeasureSpec) {
//        int mode = MeasureSpec.getMode(widthMeasureSpec);
//        int size = MeasureSpec.getSize(widthMeasureSpec);
//        int width = (int) (200 + mBgArcPaint.getStrokeWidth());
//        if (mode == MeasureSpec.EXACTLY) {
//            width = size;
//        } else if (mode == MeasureSpec.AT_MOST) {
//            width = Math.min(width, size);
//        }
//        return width;
//    }
//
//    private int measureHeight(int heightMeasureSpec) {
//        int mode = MeasureSpec.getMode(heightMeasureSpec);
//        int size = MeasureSpec.getSize(heightMeasureSpec);
//        int height = (int) (200 + mBgArcPaint.getStrokeWidth());
//        if (mode == MeasureSpec.EXACTLY) {
//            height = size;
//        } else if (mode == MeasureSpec.AT_MOST) {
//            height = Math.min(height, size);
//        }
//        return height;
//    }

    public void setProgress(float progress) {
        mProgress = progress;
        postInvalidate();
    }
}
