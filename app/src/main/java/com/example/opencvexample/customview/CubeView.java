package com.example.opencvexample.customview;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.opencvexample.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CubeView extends View {

    private Paint mPaint;
    private int sizeWidth,sizeHeigt;
    private float lypos,sypos;
    private float lxpos,sxpos;
    private List<CustRect> rectList = new ArrayList<>();
    private List<CustRect> titleDayList = new ArrayList<>();
    private List<CustRect> rectHourList = new ArrayList<>();
    private final int sizeNum = 16;
    private final int totalNum = 400;
    private CustRect childview;
    private int rectWidth;
    private int rectHeigt;
    private boolean isSel;
    private int selectColor = Color.parseColor("#FF757575");
    private Disposable disposable;
    //使用矩阵
    private MyMatrix matrix;


    Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Log.e("test","disposable = "+ disposable.isDisposed());
                restartDrop();
            }
        }
    };
    private boolean isRotate;

    public CubeView(Activity context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(selectColor);
        mPaint.setAlpha(50);
//        Log.e("color","selectColor = "+selectColor+",R = "+ getResources().getColor(R.color.gray_600));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        CustRect custRect = new CustRect();
//        custRect.set(left,rectWidth*25-1,right,rectWidth*25);
//        dropedRects.add(custRect);
        int curX = left;
        int curY = top;
        titleDayList.clear();
        rectList.clear();
        for (int j=0;j<totalNum;j++){
            childview = new CustRect();
            childview.setId(j);
            childview.set(curX, curY, curX + rectWidth, curY +rectHeigt);
            curX += rectWidth;
            if((j%sizeNum == (sizeNum -1)) && j > 0){
                curY+= rectHeigt;
                curX = left;
            }
            if( j>0 && j< sizeNum){
                titleDayList.add(childview);
            }
            rectList.add(childview);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeigt = MeasureSpec.getSize(heightMeasureSpec);
        rectWidth = sizeWidth/sizeNum;
        rectHeigt = sizeWidth/sizeNum;
        setMeasuredDimension(sizeWidth,sizeHeigt);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(30);
        for (int i = 0; i < totalNum; i++) {
            CustRect custRect = rectList.get(i);
            if(i == 0){
                canvas.drawLine(0,0,custRect.right,custRect.bottom,mPaint);
            }
            canvas.drawRect(custRect, paint);
        }

        for (CustRect rect : dropRects) {
                canvas.drawRect(rect, mPaint);
        }
        //把所有的选中状态的块（rect）涂上颜色
        for (CustRect rect : dropedRects) {
            canvas.drawRect(rect, mPaint);
        }
    }

    public List<CustRect> getRectHourList(int hour) {
        rectHourList.clear();
        for (CustRect rect: rectList) {
            if(rect.getId() > (sizeNum -1) && rect.getId() / sizeNum == hour){
                rect.setSelect(!isSel);
                rectHourList.add(rect);
            }
        }
        return rectHourList;
    }
    private volatile List<CustRect> dropRects = new ArrayList<>();
    List<CustRect> dropedRects = new ArrayList<>();

    public void drop(){
        dropRects.clear();
        dropRects.add(rectList.get(7));
        dropRects.add(rectList.get(8));
        disposable = Observable.interval(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((c->{
            Log.e("test","c ="+c);
                    dropRectsStart(dropRects, c);
                    invalidate();
        }),throwable->{
            Log.e("test","disposable = error:"+throwable.getMessage());
        });
    }

    private List<CustRect> saveRects = new ArrayList<>();

    private void dropRectsStart(List<CustRect> Rects,long c) {
        List<CustRect> tmp = new ArrayList<>();
        CustRect dropingRect = null;
        boolean isinteract = false;

        for (CustRect rect: Rects) {
            int id = rect.getId();
            int nextValue = downValue==0?sizeNum:sizeNum*downValue;
            dropingRect = rectList.get((id + movingValue) + nextValue);
            tmp.add(dropingRect);
            if(rect.bottom == rectHeigt*24){
                isinteract = true;
                }else {
                CustRect nextRect = null;
                if(dropedRects.size()>0){
                     nextRect = rectList.get(dropingRect.getId() + sizeNum);
                    if (nextRect.isSelect()){
                        isinteract = true;
                    }
                }
            }
        }

        if(isinteract){
            for(CustRect rect: tmp){
                rect.setSelect(true);
            }
            disposable.dispose();
            handler.sendEmptyMessage(1);
            dropedRects.addAll(tmp);
            tmp.clear();

//            if(disposable.isDisposed()){
//                dropRects.clear();
//                Log.e("test","dropRects = "+ Arrays.toString(dropRects.toArray()));
//                restartDrop();
//            }
        }
        dropRects.clear();
        dropRects.addAll(tmp);
        movingValue = 0;
        downValue = 0;
    }

    private void restartDrop() {
        drop();
    }

    int movingValue = 0;
    public void gotoLeft(){
        movingValue--;
    }
    public void gotoRight(){
        movingValue++;
    }
    public void stop() {
        dropRects.clear();
        disposable.dispose();
        invalidate();
    }
    int downValue = 0;

    public void gotoBottom() {
        downValue++;
    }


    public void gotoRotation() {
        if(dropRects.size()<1)return;
        List<CustRect> dropRectsBk = new ArrayList<>();
        dropRectsBk.addAll(dropRects);
        int num = dropRectsBk.size();
        switch (num){
            case 2:
                isRotate = !isRotate;
                int id = dropRectsBk.get(0).getId();
                if(isRotate){
                    dropRects.remove(1);
                    dropRects.add(rectList.get(id-sizeNum));
                }else {
                    dropRects.clear();
                    dropRects.addAll(dropRectsBk);
                }
                break;
        }
        invalidate();
    }
}
