package com.zzj.customanimationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bjh on 16/6/21.
 */
public class AnimatorView extends View {

    private int rect_width = 60;
    private int rect_spliteWidth = 40;
    private int total_paint_time = 100;
    private int[][] rect_array = {
            {380, Color.GRAY},
            {600, Color.YELLOW},
            {200, Color.RED},
            {450, Color.BLACK},
            {300, Color.BLUE}

    };
    private Paint mPaint;


    public AnimatorView(Context context) {
        this(context, null);
    }

    public AnimatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    int mPaintTime = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        mPaintTime++;
        for (int i = 0;i<rect_array.length;i++){
            mPaint.setColor(rect_array[i][1]);
            int paintX = i*(rect_width+rect_spliteWidth)+rect_spliteWidth;
            int paintY = rect_array[i][0]/total_paint_time*mPaintTime;
            canvas.drawRect(paintX,getHeight()-paintY,paintX+rect_width,getHeight(),mPaint);
        }
        if (mPaintTime<total_paint_time){
            invalidate();
        }
        System.out.println(mPaintTime);
    }
}
