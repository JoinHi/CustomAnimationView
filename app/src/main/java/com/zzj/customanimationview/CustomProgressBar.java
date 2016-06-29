package com.zzj.customanimationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bjh on 16/6/21.
 */
public class CustomProgressBar extends View {

    private final Path progressPath;
    private final Path failPath;
    private int progressCount = 0;

    private int mWidth;
    private Paint mPaint;
    private Path arrowPath;
    private int centerY;
    private int mHeight;
    private int rectPercent;
    private RectF progressRect;
    private int progressWidth = 10;
    private float failProgressHeight;
    private boolean isFail;
    private boolean isReset;
    private float rotateDegress;
    private Matrix matrix;
    private Paint textPaint;
    private int idleAnimCount;
    private boolean isIdle;
    private float temp;
    private Path gapPath;
    private boolean startShowText;
    private boolean isStart;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(new CornerPathEffect(10));
        arrowPath = new Path();
        progressPath = new Path();
        failPath = new Path();
        matrix = new Matrix();
        textPaint = new Paint();
        gapPath = new Path();
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
        centerY = mHeight/2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawAnimation(canvas);
        drawArrow(canvas);
        drawProgress(canvas);

        super.onDraw(canvas);
        if (!isReset && rectPercent < 500&&isStart){
            rectPercent += 5;
            invalidate();
        }
        if (isReset && rectPercent>=0){
            rectPercent -= 5;
            if (rectPercent <0){
                rectPercent = 0;
            }
            if (rectPercent == 0){
                isStart = false;
                isReset = false;
            }
            invalidate();
        }
        setIdleState();
    }

    private void setIdleState() {
        if (isIdle) {
            if (idleAnimCount <= 50) {
                int begin = idleAnimCount;

                int end = 0;
                if (begin > 40) {
                    begin = 40;
                }
                if (idleAnimCount > 40) {
                    end = idleAnimCount - 40;
                }
                idleAnimCount += 5;
                if (temp>0) {
                    rotateDegress = getGradientFloat(temp,-10,begin,40) + end;
                }
                if (temp<0){
                    rotateDegress = getGradientFloat(temp,10,begin,40) - end;
                }

                if (idleAnimCount > 50){
                    isIdle = false;
                }

                invalidate();
            }
        }
    }

    float startY = 0;
    float endY = 0;
    int cornerWidth = 0;
    int failCount = 0;

    private void drawProgress(Canvas canvas) {
        float dx = 0;
        int dropY = 0;
        failProgressHeight = failCount;

        if (isFail && failProgressHeight< 100){
            cornerWidth = 5;
            startY = failProgressHeight;
            endY = failProgressHeight - 50;
            if (startY>50){
                startY = 50;
            }
            if (endY < 0){
                endY = 0;
            }
        }

        float moveX = 1.0f*(getWidth() - centerY/2)*progressCount/100;
        if (isFail && failCount>100){
                rotateDegress = 20;
            dx =moveX;
            dropY = failCount -100;
        }

        float e = ( 2.0f*progressWidth)*endY/50;
        float s = ( 1.5f*progressWidth)*startY/50 + e*0.25f;
        float dn = 20.0f*dropY;

        /*画缺口*/
        if (isFail){
            gapPath.reset();
            mPaint.setColor(Color.parseColor("#2C97DE"));
            gapPath.moveTo(/*左边距*/centerY / 4 + moveX,centerY);
            gapPath.lineTo(/*左边距*/centerY / 4 + moveX + cornerWidth, centerY + progressWidth );
            gapPath.lineTo(/*左边距*/centerY / 4 + moveX - 10 + cornerWidth, centerY + progressWidth);
            gapPath.close();
            canvas.drawPath(gapPath,mPaint);
        }
        //白色进度条
        progressPath.reset();
        mPaint.setColor(Color.WHITE);
        progressPath.moveTo(/*左边距*/centerY / 4 + dx, centerY - progressWidth + e + dn);
        progressPath.lineTo(/*左边距*/centerY / 4 + moveX, centerY - progressWidth + s+dn);
        progressPath.lineTo(/*左边距*/centerY / 4 + moveX + cornerWidth, centerY + progressWidth+dn);
        progressPath.lineTo(/*左边距*/centerY / 4 + moveX -10 +2* cornerWidth, centerY + progressWidth +2*failProgressHeight+dn);
        progressPath.lineTo(/*左边距*/centerY / 4 + moveX -10 + cornerWidth, centerY + progressWidth+dn);
        progressPath.lineTo(/*左边距*/centerY / 4 + moveX -10 + cornerWidth, centerY + progressWidth+dn);
        progressPath.lineTo(/*左边距*/centerY / 4 + dx, centerY + progressWidth + dn);
        progressPath.close();
        canvas.drawPath(progressPath, mPaint);
        if (isFail&&failCount<200) {
            postInvalidate();
            failCount += 2;
        }
        if (failCount>=200){
            resetDownload();
        }
    }

    private void resetDownload() {
        isReset = true;
        reset();
        postInvalidate();
    }

    int leftPercent = 0;
    int rightPercent = 0;
    private void drawArrow(Canvas canvas) {
        int shapePercent = rectPercent;
        if (rectPercent>100){
            shapePercent = 100;
            if (rectPercent<=200){
                leftPercent = rectPercent - 100;
                rotateDegress = -15;
            }else {
                if (rectPercent <= 300){
                    rotateDegress = 15;
                    rightPercent = rectPercent -200;
                    if (rectPercent == 300){
                        rotateDegress = 0;
                        startShowText = true;
                    }
                }
            }
        }
        //防止动画还没结束就设置进度
        if (!startShowText){
            progressCount = 0;
        }
        float shapeSize = (1.0f*centerY/8*shapePercent/100);
        int upPercent = shapePercent;
        if (upPercent>60){
            upPercent = 60;
        }
        int downPercent = 60;
        if (shapePercent>60){
             downPercent = shapePercent;
        }
        float arrowDown = (1.0f*mHeight/4 + 1.2f*centerY/8 - centerY/4 - progressWidth)*(downPercent-60)/40;
        float arrowUp = 1.0f*mHeight/4*upPercent/60 - arrowDown;
        float arrowRight = 1.0f*(/*左边距*/centerY/4 - getWidth()/2 - centerY/4)*rightPercent/100;
        float arrowLeft = 1.0f*centerY/4*leftPercent/100 + arrowRight + 1.0f*(getWidth() - centerY/2)*progressCount/100;
        mPaint.setColor(Color.WHITE);
        arrowPath.reset();
        arrowPath.moveTo((mWidth / 2 - centerY / 8) - /*shape*/shapeSize + arrowLeft,
                centerY - centerY / 4 - arrowUp);
        arrowPath.lineTo(mWidth / 2 + centerY / 8 + /*shape*/shapeSize + arrowLeft,
                centerY - centerY / 4 - arrowUp);
        arrowPath.lineTo(mWidth / 2 + centerY / 8 + /*shape*/shapeSize + arrowLeft, centerY - arrowUp);
        arrowPath.lineTo(mWidth / 2 + centerY / 4 - shapeSize*1.2f+arrowLeft, centerY - arrowUp);
        arrowPath.lineTo(mWidth / 2 + arrowLeft, centerY + centerY / 4 - shapeSize * 1.2f - arrowUp);
        arrowPath.lineTo(mWidth / 2 - centerY / 4 + shapeSize * 1.2f + arrowLeft, centerY - arrowUp);
        arrowPath.lineTo(mWidth / 2 - centerY / 8 - /*shape*/shapeSize + arrowLeft, centerY - arrowUp);
        arrowPath.close();
        matrix.setRotate(rotateDegress, mWidth / 2 + arrowLeft, centerY + centerY / 4 - shapeSize * 1.2f - arrowUp);
        arrowPath.transform(matrix);
        canvas.drawPath(arrowPath, mPaint);
        drawText(canvas);

    }

    private void drawText(Canvas canvas) {
        if (!startShowText){
            return;
        }
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(0.06f*mHeight);
        String text = progressCount+"%";
            if (isFail && failCount>100){
                textPaint.setColor(Color.RED);
                text = "Failed";
                canvas.drawTextOnPath(text, arrowPath, 20, 40, textPaint);

        }else {
            canvas.drawTextOnPath(text, arrowPath, 35, 40, textPaint);
        }
    }

    private void drawAnimation(Canvas canvas) {
        int percent = rectPercent;
        if (percent>100){
            percent = 100;
        }
        int progress_left = mWidth/2-centerY/2;
        int progress_top = centerY/2;
        int progress_right = mWidth/2+centerY/2;
        int progress_bottom = centerY*3/2;
        float left = centerY/4;
        mPaint.setColor(Color.parseColor("#FF0F293B"));

        progressRect = new RectF(getGradientFloat(progress_left,left,percent,100)  ,
                getGradientFloat(progress_top,centerY-progressWidth,percent,100) ,
                getGradientFloat(progress_right,mWidth-left,percent,100),
                getGradientFloat(progress_bottom,centerY+progressWidth,percent,100) );
        canvas.drawRoundRect(progressRect, progressWidth, progressWidth, mPaint);
    }

    public void setProgressCount(int progress){
        isFail = false;
        failCount = 0;
        if (progress > progressCount){
            rotateDegress = -15;
        }else {
            rotateDegress = 15;
        }
        startTimerTask();

        progressCount = progress;
        postInvalidate();
    }

    public void loadingFail(){
        failCount = 0;
        isFail = true;
        postInvalidate();
    }

    public void start(){
        isStart = true;
        postInvalidate();
    }

    public void reset(){
        rotateDegress = 0;
        startY = 0;
        endY = 0;
        cornerWidth = 0;
        failCount = 0;
        progressCount = 0;
        leftPercent = 0;
        rightPercent = 0;
        startShowText = false;
        isFail = false;
        rectPercent = 100;
    }


    private float getGradientFloat(float from ,float to , int progress , int total){

        return from - (from - to)*progress/total;
    }

    boolean isTaskRun;
    private Timer timer;
    private TimerTask timerTask;
    private void startTimerTask(){
        if (isTaskRun) {
            timer.cancel();
            timerTask.cancel();
        }
        timer = new Timer();
        timerTask = new MyTimerTask();
        isTaskRun = true;
        timer.schedule(timerTask, 200);

    }


    class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            isTaskRun = false;
            isIdle = true;
            temp = rotateDegress ;
            idleAnimCount = 0;
            postInvalidate();
        }
    }

}
