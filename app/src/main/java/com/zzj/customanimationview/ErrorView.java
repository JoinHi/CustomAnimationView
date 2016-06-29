package com.zzj.customanimationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bjh on 16/6/22.
 */
public class ErrorView extends View {

    private Paint mPaint;
    private int time;
    private int strokeWidth;
    private int circleCount;
    private int leftCount;
    private int rightCount;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        strokeWidth = 5;
        mPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth() - strokeWidth * 2;
        float tem = getWidth() / 4;
        canvas.drawArc(new RectF(strokeWidth, strokeWidth, width + strokeWidth, width + strokeWidth), -90, 360 * 1.0f * circleCount / 50, false, mPaint);
        circleCount = time;
        if (time > 50) {
            circleCount = 50;
            canvas.drawLine(getWidth() / 2 - tem, getWidth() / 2 - tem,
                    getWidth() / 2-tem+(tem *2)*1.0f*(leftCount-50)/50, getWidth() / 2-tem+(tem *2)*1.0f*(leftCount-50)/50, mPaint);
        }
        leftCount = time;
        if (time > 100) {
            leftCount = 100;
            canvas.drawLine(getWidth() / 2 - tem, getWidth() / 2 + tem, getWidth() / 2 - tem+tem*2.0f*(rightCount-100)/50,
                    getWidth() / 2 + tem-tem*2.0f*(rightCount-100)/50, mPaint);
        }
        rightCount = time;
        if (time < 150) {
            invalidate();

        }

        time += 2;
    }
}
