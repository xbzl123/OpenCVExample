package com.example.opencvexample.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekContainer extends ViewGroup {
    String[] days = new String[]{"周一","周二","周三","周四","周五","周六","周日",};

    private List<DayContainer> dayContainerList = new ArrayList<>();
    private Context mContext;
    private int viewcount = 200;
    private int sizeNum = 8;
    private HourView hourView;
    private int mHeightMeasureSpec;
    private int mWidthMeasureSpec;
    private float lypos,sypos;
    private float lxpos,sxpos;
    private Paint mPaint;

    public WeekContainer(Context context) {
        super(context);
        mContext = context;
//        setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        Log.e("test2","widthMeasureSpec = "+widthMeasureSpec);
        Log.e("test","heightMeasureSpec = "+heightMeasureSpec);
        mWidthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);
        mHeightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("test1","getChildCount = "+getChildCount());
        if(getChildCount()<1)return;
        int curX = l;
        int curY = t;
        for (int j=0;j<200;j++){
            View childview = getChildAt(j);
//            Log.e("test1","childview = "+childview.getWidth()+",curX = "+curX);
            childview.layout(curX, curY, curX + childview.getMeasuredWidth(), curY +childview.getMeasuredWidth());
            curX += childview.getMeasuredWidth();
            if((j%sizeNum == 0) && j > 7){
                Log.e("test1","childview = "+childview.getWidth()+",curX = "+curX);
                curY+= childview.getMeasuredWidth();
                curX = l;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("test0","widthMeasureSpec = ");
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        if(sxpos > lxpos && sypos > lypos){//捺上\
            canvas.drawRect(lxpos, lypos, sxpos, sypos, mPaint);
        }else if(sxpos < lxpos && sypos < lypos){//捺下\
            canvas.drawRect(sxpos, sypos, lxpos, lypos, mPaint);
        }else if(sxpos < lxpos && sypos > lypos){//撇上/
            canvas.drawRect(sxpos, lypos, lxpos, sypos, mPaint);
        } else if(sxpos > lxpos && sypos < lypos){//撇下/
            canvas.drawRect(lxpos, sypos,sxpos, lypos, mPaint);
        }
    }
    public void addViewsToContainer(){
//        removeAllViews();
        for (int i = 0; i < 200; i++) {
            hourView = new HourView(mContext);
            if(i>0 && i<8){
                hourView.setTitle(days[i-1]);
            }else if(i > 7){
                switch (i%8){
                    case 1:
                        hourView.setTitle(String.valueOf((i/8))+":00");
                        break;
                    default:
                        List<HourView> hourViewList = new ArrayList<>();
                        hourViewList.add(hourView);
                        dayMap.put(i%8,hourViewList);
                        break;

                }
            }
            addView(hourView);
        }
    }

    Map<Integer,List<HourView>> dayMap = new HashMap<>();
    //2以上是Mod,Tue...
    public List<HourView> isWhichDay(int pos){
        List<HourView> objects = new ArrayList<>();
        if (dayMap == null && dayMap.size() > 0) {
            objects = dayMap.get(pos);

        }
        return objects;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("pos","getAction = "+ event.getAction());
//        int action = event.getAction();
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                sxpos = event.getX();
//                sypos = event.getY();
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    public void setLastPos(float sxpos, float sypos,float lxpos, float lypos) {
        this.sxpos = sxpos;
        this.sypos = sypos;
        this.lxpos = lxpos;
        this.lypos = lypos;
    }
    public void setLastPos(float lxpos, float lypos) {
        this.lxpos = lxpos;
        this.lypos = lypos;
    }
}
