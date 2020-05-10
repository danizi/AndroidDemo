package com.xm.componet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * 字母表侧标栏
 */
@SuppressLint("AppCompatCustomView")
public class LetterSideBar extends View {

    private static final String TAG = "LetterSideBar";
    private TextPaint paint;
    private String alphabetArr[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int itemHeight;

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(50);
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int y = 0;
        itemHeight = getBaseLine() + measureTextBounds("A").height();
        for (String alphabet : alphabetArr) {
            //竖向绘制字符
            int x = (getWidth() - measureTextBounds("A").width()) / 2;
            y += itemHeight;
            canvas.drawText(alphabet, x, y, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getY();
                int index = (int) (x / itemHeight) % 27;
                Log.d(TAG, "当前字符" + this.alphabetArr[index]);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private int measureHeight(int heightMeasureSpec) {
        return MeasureSpec.getSize(heightMeasureSpec);
    }

    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int stoke = (int) paint.getStrokeWidth();
        int width = (measureTextBounds("A").width() + getPaddingLeft() + getPaddingRight() + stoke);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }
        return width;
    }

    private Rect measureTextBounds(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    private int getBaseLine() {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int baseLine = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        return baseLine;
    }
}
