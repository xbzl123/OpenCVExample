package com.example.opencvexample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;

public class SvgItem {

    private Path path;

    private String name;
    /**

     * 绘制颜色

     * */

    private String drawColor;

    public void setDrawColor(String drawColor){

        this.drawColor = drawColor;

    }

    public String getName() {
        return name;
    }

    public SvgItem(Path path, String s) {

        this.path = path;
        this.name = s;

    }


    public void drawItem(Canvas canvas, Paint paint, boolean isSelect){

        if (isSelect){

            //绘制内部颜色

            paint.clearShadowLayer();

            paint.setStrokeWidth(1);

            paint.setStyle(Paint.Style.FILL);

            paint.setColor(0xffffff00); // 填充的颜色为黄色

            canvas.drawPath(path,paint);

            //绘制边界

            paint.setStyle(Paint.Style.STROKE);

            paint.setColor(0xFFD0E8F4);

            canvas.drawPath(path,paint);
            Log.e("test","=================selectItem = "+name);

        }else {

            paint.setStrokeWidth(2);

            paint.setColor(Color.BLACK);

            paint.setStyle(Paint.Style.FILL);

            paint.setShadowLayer(8,0,0,0xffffff);

            canvas.drawPath(path,paint);

            //绘制边界

            paint.clearShadowLayer();

            paint.setColor(Color.parseColor(drawColor));

            paint.setStyle(Paint.Style.FILL);

            paint.setStrokeWidth(2);

            canvas.drawPath(path,paint);

        }

    }



    /*判断手指点击区域是否落入*/

    public boolean isTouch(float x,float y){

        RectF rectF = new RectF();

        path.computeBounds(rectF,true);

        Region region = new Region();

        region.setPath(path,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int) rectF.bottom));

        return  region.contains((int)x,(int)y);

    }

}
