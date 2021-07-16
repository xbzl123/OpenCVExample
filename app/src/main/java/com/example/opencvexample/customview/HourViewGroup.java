package com.example.opencvexample.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class HourViewGroup extends View implements GestureDetector.OnGestureListener {

    private final String[] days = new String[]{"周一","周二","周三","周四","周五","周六","周日",};
    private Paint mPaint;
    private int sizeWidth,sizeHeigt;
    private float lypos,sypos;
    private float lxpos,sxpos;
    private List<CustRect> rectList = new ArrayList<>();
    private List<CustRect> rectDayList = new ArrayList<>();
    private List<CustRect> titleDayList = new ArrayList<>();
    private List<CustRect> titleHourList = new ArrayList<>();
    private List<CustRect> rectHourList = new ArrayList<>();
    //第一个Integer:1-24小时，第二个Integer:星期一到星期日
    private Map status = new HashMap<Integer,Integer>();
    //保存每个小时的每天的状态，24个小时，List里面包着的是每天的
    private Map selectStatus = new HashMap<Integer,List<Integer>>();
    private final int sizeNum = 8;
    private final int totalNum = 392;
    private CustRect childview;
    private int rectWidth;
    private int rectHeigt;
    private boolean isSel;
    private final int selectColor = Color.parseColor("#FF039BE5");
    private boolean isDoubleMove;
    private int moveYValue;
    private int overFlowY;
    GestureDetector gestureListener;


    public HourViewGroup(Activity context) {
        super(context);
        mPaint = new Paint();

        gestureListener = new GestureDetector(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int curX = left;
        int curY = top;
        titleDayList.clear();
        rectList.clear();
        for (int j=0;j < totalNum;j++){
            CustRect childview = new CustRect();
            childview.setId(j);
            childview.set(curX, curY, curX + rectWidth, curY +rectHeigt);
            curX += rectWidth;
            if((j%sizeNum == (sizeNum-1)) && j > 0){
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
        rectHeigt = sizeWidth/(sizeNum*2);
        overFlowY = rectHeigt*49 - sizeHeigt;
        setMeasuredDimension(sizeWidth+5,sizeHeigt+2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(30);
        for (int i = 0; i < totalNum; i++) {
            CustRect custRect = rectList.get(i);
            if(i == 0){
                canvas.drawLine(0,0,custRect.right,custRect.bottom,mPaint);
            }
            canvas.drawRect(custRect, mPaint);
            if (i > 0 && i < sizeNum) {
                canvas.drawText(days[i - 1], custRect.centerX() - custRect.width() / 4, custRect.centerY(), mPaint);
            } else if (i > (sizeNum-1)) {
                if (i % sizeNum == 0) {
                    titleHourList.add(custRect);
                    canvas.drawText(i / sizeNum + ":00", custRect.centerX() - custRect.width() / 4, custRect.centerY(), mPaint);
                }
            }
        }
//        if(sxpos > lxpos && sypos > lypos){//捺上\
//            canvas.drawRect(lxpos, lypos, sxpos, sypos, mPaint);
//        }else if(sxpos < lxpos && sypos < lypos){//捺下\
//            canvas.drawRect(sxpos, sypos, lxpos, lypos, mPaint);
//        }else if(sxpos < lxpos && sypos > lypos){//撇上/
//            canvas.drawRect(sxpos, lypos, lxpos, sypos, mPaint);
//        } else if(sxpos > lxpos && sypos < lypos){//撇下/
//            canvas.drawRect(lxpos, sypos,sxpos, lypos, mPaint);
//        }

        mPaint.reset();
        mPaint.setColor(selectColor);
        mPaint.setAlpha(100);
        //把所有的选中状态的块（rect）涂上颜色
        for (CustRect rect : rectList) {
            if(rect.getId()!=0 && rect.isSelect()){
                canvas.drawRect(rect, mPaint);
            }
        }
    }

    @Override
        public boolean onTouchEvent(MotionEvent event) {
        gestureListener.onTouchEvent(event);
        int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    sxpos = event.getX();
                    sypos = event.getY() + moveYValue;
                    //获取点击下去的块Rect的是否选择的状态
                    for (CustRect custRect : rectList){
                        boolean contains = custRect.contains(sxpos, sypos, sxpos , sypos);
                            if(contains){
                                isSel = custRect.isSelect();
                                custRect.setTouch(true);
                                break;
                            }
                        }
                    break;
                case MotionEvent.ACTION_MOVE:
                    lxpos = event.getX();
                    lypos = event.getY() + moveYValue;
                    if(!isDoubleMove){
                        for (CustRect rect : rectList) {
                                    if (rect.contains(lxpos, lypos, sxpos, sypos)
                                            || rect.contains(sxpos, sypos, lxpos, lypos)
                                            || rect.contains(sxpos, lypos, lxpos, sypos)
                                            || rect.contains(lxpos, sypos, sxpos, lypos)) {
                                        rect.setSelect(!isSel);
                                        if(titleDayList.contains(rect)||titleHourList.contains(rect)){
                                            rect.setTouch(true);
                                        }
                                }
                        }
                                invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //点击星期标题进行全选
                    for (CustRect c: titleDayList) {
                            if(c.isTouch()){
                            getRectDayList(c.getId());
                            c.setTouch(false);
                            invalidate();
                        }
                    }
                    //点击小时标题进行全选
                    for (CustRect c: titleHourList) {
                        if(c.isTouch()){
                            getRectHourList(c.getId()/sizeNum);
                            c.setTouch(false);
                            invalidate();
                        }
                    }
                    isDoubleMove = false;
                    convertToData();
                    break;
                case MotionEvent.ACTION_POINTER_2_DOWN:
                    isDoubleMove = true;
                    break;
            }
            return true;
        }

    //1以上是Mod,Tue...
    public List<CustRect> getRectDayList(int day) {
        rectDayList.clear();
        for (CustRect rect: rectList) {
            if(rect.getId() > sizeNum && rect.getId() % sizeNum == day){
                rect.setSelect(!isSel);
                rectDayList.add(rect);
            }
        }
        return rectDayList;
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

    List<CustRect> selectedRect = new ArrayList<>();
    public void convertToData(){
        selectedRect.clear();
        status.clear();
        selectStatus.clear();
        for (int i = 0; i < totalNum; i++) {
            CustRect custRect = rectList.get(i);
            if(custRect.isSelect()){
                selectedRect.add(custRect);
            }
        }
        int value = 0;
        int row = 0;
        List<Integer> integers = new ArrayList<>();
        for (CustRect custRect: selectedRect) {
            int rowId = custRect.getId()/sizeNum;
            if(row != rowId){
                value = 0;
                integers.clear();
            }
            if(custRect.isSelect()){
                 value+= Math.pow(2 , (custRect.getId() - rowId * sizeNum  - 1));
                integers.add((custRect.getId() - rowId * sizeNum));
            }
            status.put(rowId,value);
            selectStatus.put(rowId,integers);
            row = rowId;
            Log.e("convertToData","hour = "+rowId+",value = "+value+",integers = "+integers);
        }
    }

    @Test
    public List<Integer> intToList(){
        int n = 7;
        ArrayList<Integer> objects = new ArrayList<>();

        return objects;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(isDoubleMove){
            moveYValue = (int) (e2.getY());
            if(moveYValue <0){
                moveYValue = 0;
            }else if(overFlowY < moveYValue){
                moveYValue = overFlowY+2;
            }
            this.setScrollY(moveYValue);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
