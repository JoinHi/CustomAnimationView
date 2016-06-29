package com.zzj.customanimationview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bjh on 16/6/23.
 */
public class BezierView extends View {

    private Paint paint;
    private Path path;
    private int mSupX;
    private int mSupY;
    private boolean isUp;
    private boolean isStart;
    private int downY;
    private Bitmap bitmap;
    int bitmapX;
    int bitmapY;
    private float f;
    private Matrix matrix;
    private double rotate;
    private Bitmap newBitmap;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        matrix = new Matrix();
    }

    private int count = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        path.moveTo(100, getHeight() / 2);
        path.quadTo(getWidth() / 2 + (mSupX - getWidth() / 2) * count / 50,
                getHeight() / 2 + (mSupY - getHeight() / 2) * count / 50, getWidth() - 100, getHeight() / 2);
        canvas.drawPath(path, paint);
        matrix.setRotate((float) rotate,bitmap.getWidth()/2,bitmap.getHeight()/2);
        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(newBitmap, bitmapX - bitmap.getWidth() / 2 - 20 * (100 - count) * f,
                (bitmapY - bitmap.getHeight()) - 20 * (100 - count),paint);
        if (count > 0 && isUp) {
            count -= 10;
            invalidate();
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                count = 100;
                isUp = false;
                rotate = 0;
                f = 1;
                break;
            case MotionEvent.ACTION_MOVE:
                mSupX = (int) event.getX();
                mSupY = (int) event.getY();
                bitmapX = mSupX;
                bitmapY = mSupY;
                if (mSupX > 100 && mSupX < getWidth() - 100) {
                    if ((downY < getHeight() / 2 && mSupY >= getHeight() / 2)) {
                        isStart = true;
                        double dy = (mSupY-getHeight()/2);
                        double dx = (mSupX-getWidth()/2);
                        if(dx == 0) dx = 1;
                        if(dy == 0) dy = 1;
                        rotate = Math.atan(dx/dy)/3.14* (-180);//计算角度
                        System.out.println(rotate);
                    } else {
                        isStart = false;
                        mSupX = getWidth() / 2;
                        mSupY = getHeight() / 2;
                        invalidate();
                    }
                }
                if (isStart) {
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isStart) {
                    isUp = true;
                    isStart = false;
                    //计算x是y的增长率的倍数
                    f = (1.0f * getWidth() / 2 - mSupX) / (1.0f * getWidth() / 2 - mSupY);

                    invalidate();
                }
                break;
        }
        return true;
    }
}
