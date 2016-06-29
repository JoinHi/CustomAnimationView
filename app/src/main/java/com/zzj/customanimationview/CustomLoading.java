package com.zzj.customanimationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bjh on 16/6/23.
 */
public class CustomLoading extends View {

    private Paint paint;
    private Path path;
    private int lineY;
    private int strokeWith = 8;
    private int lineCount ;
    private int foldingCount;
    private int circleCount;
    private int pointCount;

    public CustomLoading(Context context) {
        this(context, null);
    }

    public CustomLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWith);
        paint.setStrokeJoin(Paint.Join.ROUND);
        path = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        paint.setColor(Color.parseColor("#2EA4F2"));
        canvas.drawCircle(centerX, centerY, centerX - strokeWith / 2, paint);
        paint.setColor(Color.WHITE);

        canvas.drawLine(centerX / 2, centerY, centerX, centerY + centerY / 2 - 1.0f * centerY / 2 * (foldingCount-circleCount) / 50, paint);
        canvas.drawLine(centerX, centerY + centerY / 2 - 1.0f * centerY / 2 * (foldingCount-circleCount) / 50,centerX + centerX / 2, centerY-centerY/2*circleCount/50,paint);


        canvas.drawLine(centerX, centerY / 2 + 1.0f * centerY / 2 * lineCount / 50 - strokeWith / 2 - 1.0f * centerY * pointCount / 50, centerX,
                centerY + centerY / 2 - 1.0f * centerY / 2 * lineCount / 50 + strokeWith / 2 - 1.0f * centerY * pointCount / 50, paint);

        canvas.drawArc(new RectF(strokeWith/2,strokeWith/2,getWidth()-strokeWith,getHeight()-strokeWith), 270, -360*circleCount/50, false, paint);
        if (lineCount<50){

            lineCount++;
            invalidate();
        }else {
            if (foldingCount<50){
                foldingCount++;
                invalidate();
            }else{
                if (pointCount<50){
                    pointCount++;
                    invalidate();
                }else {
                    if (circleCount<50){
                        circleCount++;
                        invalidate();
                    }
                }
            }
        }
    }
}
