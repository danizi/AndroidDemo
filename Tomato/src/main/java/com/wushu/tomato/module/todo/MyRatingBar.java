package com.wushu.tomato.module.todo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wushu.tomato.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制
 */
public class MyRatingBar extends View {

    private int totalNum;
    private int selNum;
    private Paint paintRatingBg;
    private Paint paintRating;
    private Bitmap ratingBarBgBmp;
    private Bitmap ratingBarBmp;
    private List<Rect> rects;
    private int haveDrawSel;
    private float startX, startY;
    private int space = 20;
    private boolean isMove = false;

    public MyRatingBar(Context context) {
        this(context, null);
    }

    public MyRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.MyRatingBar);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyRatingBar_ratingBarBgRes:
                    int ratingBarBgRes = a.getResourceId(attr, 0);
                    ratingBarBgBmp = getBmp(ratingBarBgRes);
                    break;
                case R.styleable.MyRatingBar_ratingBarRes:
                    int ratingBarRes = a.getResourceId(attr, 0);
                    ratingBarBmp = getBmp(ratingBarRes);
                    break;
                case R.styleable.MyRatingBar_selNum:
                    selNum = a.getInteger(attr, 0);
                    break;
                case R.styleable.MyRatingBar_totalNum:
                    totalNum = a.getInteger(attr, 5);
                    break;
            }
        }
        a.recycle();

        //初始化
        paintRatingBg = new Paint();
        paintRatingBg.setAntiAlias(true);
        paintRating = new Paint();
        paintRating.setAntiAlias(true);
        rects = new ArrayList<>();
        haveDrawSel = 0;
        //处理onTouchEvent只有down事件
        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制等级背景
        drawRatingBarBg(canvas);
        //绘制当前等级
        drawRatingBar(canvas);
    }

    private void drawRatingBarBg(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < totalNum; i++) {
            rects.add(new Rect(i * ratingBarBgBmp.getWidth(), 0, ((i + 1) * ratingBarBgBmp.getWidth()), ratingBarBgBmp.getHeight()));
            canvas.drawBitmap(ratingBarBgBmp, i * ratingBarBgBmp.getWidth(), 0, paintRatingBg);
        }
        canvas.restore();
    }

    private void drawRatingBar(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < selNum; i++) {
            canvas.drawBitmap(ratingBarBmp, i * ratingBarBmp.getWidth(), 0, paintRating);
        }
        canvas.restore();
    }

    private Bitmap getBmp(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    private Bitmap resetBitmap(Bitmap bitMap, int startWidth, int startHeight) {
        return Bitmap.createScaledBitmap(bitMap, startWidth, startHeight, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                if (isInvalidate(range(event.getX(), event.getY()))) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                float offsetX = event.getX() - startX;
                float offsetY = event.getY() - startY;
                //点击处理
                if (offsetX < 10 && offsetY < 10 && !isMove) {
                    int i = range(event.getX(), event.getY());
                    if (haveDrawSel == i) {
                        //当前index已经设置，点击取消等级
                        isInvalidate(i - 1);
                    } else {
                        //当前index未设置，点击设置等级
                        isInvalidate(i);
                    }

                }
                isMove = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isInvalidate(int index) {
        if (index != -1) {
            selNum = index;
            if (haveDrawSel != index) {
                Log.d("tag", "刷新等级:" + selNum);
                invalidate();
                haveDrawSel = index;
                return true;
            }
        }
        return false;
    }

    private int range(float x, float y) {
        //往右滑动
        //往左滑动
        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            if ((rect.left - space) < x && x < (rect.right - space) && (rect.top - space) < y && y < (rect.bottom - space)) {
                return i + 1;
            }
        }
        return -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = totalNum * ratingBarBgBmp.getWidth() + getPaddingLeft() + getPaddingRight();
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

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height = ratingBarBgBmp.getHeight() + getPaddingTop() + getPaddingBottom();
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

    public float getRating() {
        return selNum;
    }

}
