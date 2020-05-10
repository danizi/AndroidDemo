package com.xm.componet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * 模拟TextView
 */
public class MyTextView extends View {

    private CharSequence text;
    private int color = Color.BLACK;
    private float size;
    private TextPaint textPaint;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义布局属性
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MyTextView_Text) {
                text = a.getText(attr);

            } else if (attr == R.styleable.MyTextView_Color) {
                color = a.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.MyTextView_Size) {
                size = a.getDimensionPixelSize(attr, sp2px(15));

            }
        }
        //初始化创建
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(size);
        textPaint.setColor(color);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (int) (getWidth() / 2 - (measureTextBounds(text.toString()).width() / 2) + textPaint.getStrokeWidth());
        x = 0;
        int y = (int) ((int) getBaseLine(textPaint));
        drawText(canvas, 0, text.length(), x, y, textPaint);
        int stoke = (int) textPaint.getStrokeWidth();
    }

    private void drawText(Canvas canvas, int start, int end, float x, float y, Paint textPaint) {
        canvas.save();
        textPaint.setColor(color);
        canvas.drawText((String) text, /*start, end, */x, y, textPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) measureWidth(widthMeasureSpec), (int) measureHeight(heightMeasureSpec));
    }

    private float measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int stoke = (int) textPaint.getStrokeWidth();
        int width = 10 + (int) (measureTextBounds(text.toString()).width() + getPaddingLeft() + getPaddingRight() + stoke);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }
        return width;
    }

    private float measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 60 + (int) (measureTextBounds(text.toString()).height() + getPaddingTop() + getPaddingBottom());
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        return height;
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private Rect measureTextBounds(String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    private float getBaseLine(Paint textPaint) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        return measureTextBounds((String) text).height() / 2 + distance;
    }
}
