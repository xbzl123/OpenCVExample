package com.example.opencvexample.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

public class DayContainer extends ViewGroup {
    String title;
    private boolean isSelect = false;
    private HourView hourView;
    public DayContainer(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public DayContainer(String days) {
        super(null);
        this.title = days;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if(isSelect){
            paint.setColor(Color.RED);
        }else {
            paint.setColor(Color.BLACK);
        }
        canvas.drawText(title,0,0,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = !select;
        invalidate();
    }
}
