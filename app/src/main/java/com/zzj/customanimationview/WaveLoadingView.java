package com.zzj.customanimationview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bjh on 16/6/27.
 */
public class WaveLoadingView extends View {

    private Paint mWavePaint;
    private Paint mCirclePaint;
    private int mWidth;
    private int mHeight;
    private Bitmap mCircleBm;
    private Canvas mCircleCs;
    private PorterDuffXfermode mMode;
    private Bitmap mRectBm;
    private Canvas mRectCs;
    private Path mPath;

    public WaveLoadingView(Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWavePaint = new Paint();
        mWavePaint.setColor(Color.parseColor("#33b5e5"));
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#99cc00"));

        mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC);


        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
        mCircleBm = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        mCircleCs = new Canvas(mCircleBm);

        mRectBm = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        mRectCs = new Canvas(mRectBm);

    }

    @Override
    protected void onDraw(Canvas canvas) {



        mRectCs.drawRect(0, 0, 300, 300, mWavePaint);
        mCircleCs.drawCircle(150, 150, 100, mCirclePaint);
        mCirclePaint.setXfermode(mMode);
        canvas.drawBitmap(mRectBm, 0, 0, mWavePaint);
//        canvas.drawBitmap(mCircleBm, 0, 0, mCirclePaint);

//        mCirclePaint.setXfermode(mMode);
//        canvas.drawRect(0, 0, 300, 300, mWavePaint);
//        canvas.drawCircle(150, 150, 100, mCirclePaint);

        int x = 0;
        int y = 100;
        if (x >50){
            boolean isLeft = true;
        }

        mPath.reset();
        mPath.moveTo(0, y);
        mPath.cubicTo(100, y + 50, 200, y  -50, 300,y);
        mPath.lineTo(300, 300);
        mPath.lineTo(0, 300);
        mPath.close();
        canvas.drawPath(mPath,mCirclePaint);

        super.onDraw(canvas);
    }
}
