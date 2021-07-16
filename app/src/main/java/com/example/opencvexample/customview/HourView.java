package com.example.opencvexample.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class HourView extends View {
    private int mWidth,mHeight;
    private Paint mPaint;
    private boolean isSelect;
    private int size;
    private String title = "";
    private float lypos,sypos;
    private float lxpos,sxpos;

    public HourView(Context context) {
        super(context);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
//        Log.e("Rest1","mHeight = "+mHeight);
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(1);
            mPaint.setColor(Color.BLACK);
            setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        float[] lineArr = new float[]{0,getHeight(),getWidth(),getHeight(),0,0,getWidth(),0,0,0,0,getHeight(),getWidth(),0,getWidth(),getHeight()};
        canvas.drawLine(0,0,0,getHeight(),mPaint);//左竖线
        canvas.drawLine(getWidth()-1,0,getWidth()-1,getHeight(),mPaint);//右竖线
        canvas.drawLine(0,0,getWidth(),0,mPaint);//上横线
        canvas.drawLine(0,getHeight()-1,getWidth(),getHeight()-1,mPaint);//下横线
//        canvas.drawLines(lineArr,mPaint);
        mPaint.setTextSize(30);
        canvas.drawText(title,15,getHeight()/3*2,mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isSelect = !isSelect;
//        setBackgroundColor(isSelect?Color.RED:Color.YELLOW);
        return super.onTouchEvent(event);
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastPos(float sxpos, float sypos,float lxpos, float lypos) {
        this.sxpos = sxpos;
        this.sypos = sypos;
        this.lxpos = lxpos;
        this.lypos = lypos;
    }
}
