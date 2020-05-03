package demo.xm.com.demo.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

/**
 * 字体方向滑动变色
 */
@SuppressLint("AppCompatCustomView")
public class ColorTrackView extends TextView {

    private String text = "";
    private TextPaint textPaint;
    private int color = Color.RED;

    public ColorTrackView(Context context) {
        this(context, null);
    }

    public ColorTrackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setAntiAlias(true);
        textPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //文字的变化向左右两个方向改变 ->
        //
        // [ADF](原始的颜色) -> [A->AD->ADF](变化的颜色)
        text= (String) getText();
        color = Color.RED;
        drawText(canvas, textPaint,0,getWidth()/2);
        color = Color.BLACK;
        drawText(canvas, textPaint,getWidth()/2,getWidth());
    }

    private void drawText(Canvas canvas,Paint textPaint,int scopeX,int scopeY) {
        int x = getWidth() / 2 - (measureTextBounds(text.toString()).width() / 2);
        int y = (int) getBaseLine(textPaint);
        canvas.save();
        Rect rect = new Rect(scopeX,0,scopeY,getHeight());
        canvas.clipRect(rect);
        textPaint.setColor(color);
        canvas.drawText(text, x, y, textPaint);
        canvas.restore();
    }

    private Rect measureTextBounds(String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    private float getBaseLine(Paint textPaint) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseline = getHeight() / 2 + distance;
        return baseline;
    }

}
