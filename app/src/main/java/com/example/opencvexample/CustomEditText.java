package com.example.opencvexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {
    private int mWidth,mHeight;
    private Paint mPaint;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        setBackground(null);
    }

    public CustomEditText(Context context) {
        this(context, null);
    }


    public void setBackgroundStrokeColor(int color){
        mPaint.setColor(color);
    }

    public void setBackgroundTextSize(int size){
        mPaint.setStrokeWidth(size);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float[] lineArr = new float[]{0,getHeight(),getWidth(),getHeight(),0,0,getWidth(),0,0,0,0,getHeight(),getWidth(),0,getWidth(),getHeight()};
//        canvas.drawLine(0,0,0,getHeight(),mPaint);//左竖线
//        canvas.drawLine(getWidth()-1,0,getWidth()-1,getHeight(),mPaint);//右竖线
//        canvas.drawLine(0,0,getWidth(),0,mPaint);//上横线
//        canvas.drawLine(0,getHeight()-1,getWidth(),getHeight()-1,mPaint);//下横线
        canvas.drawLines(lineArr,mPaint);
        Log.e("222222222","width :"+getWidth()+",height:"+getHeight()+",color ;"+mPaint.getColor());

    }
}
