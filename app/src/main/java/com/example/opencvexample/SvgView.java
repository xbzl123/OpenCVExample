package com.example.opencvexample;

import android.annotation.SuppressLint;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Paint;

import android.graphics.Path;

import android.graphics.RectF;

import android.os.Handler;

import android.os.Looper;

import android.util.AttributeSet;

import android.util.Log;

import android.view.MotionEvent;

import android.view.View;
import android.widget.Toast;

import androidx.core.graphics.PathParser;

import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.NodeList;

import java.io.InputStream;

import java.util.ArrayList;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;





/**

 * ① 不是直接使用svg图片，需要将svg图片转换为vector标签页才能使用 利用网站在线转换；

 * ② svg图片本身要有足够的颜色信息，意味着初始的那张png图片必须是色彩丰富的，不能是黑白的；

 */

public class SvgView extends View {



    private Context context;//上下文

    private List<SvgItem> itemList;//各省地图列表 各省地图颜色 与路径

    private Paint paint;    //初始化画笔

    private SvgItem select; //选中的省份

    private RectF totalRect;//中国地图的矩形范围

    private float scale = 1.0f;//中国地图的缩放比例

    private int resId;

    private List<String> nameList;

    public SvgView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public SvgView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }

    private MapSelectPostion selectPostion;
    public void init(Context context, int resId,MapSelectPostion mapSelectPostion) {

        this.context = context;

        paint = new Paint();

        paint.setAntiAlias(true);

        this.selectPostion = mapSelectPostion;

        itemList = new ArrayList<>();

        loadThread.start();

        this.resId = resId;

    }



    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取当前控件的高度 让地图宽高适配当前控件

        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (totalRect != null) {

            double mapWidth = totalRect.width();

            scale = (float) (width / mapWidth); //获取控件高度为了让地图能缩放到和控件宽高适配

        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),

                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

    }



    /*不是直接使用svg图片，需要将svg图片转换为vector标签页才能使用*/

    private Thread loadThread = new Thread() {

        @Override

        public void run() {

            final InputStream inputStream = context.getResources().openRawResource(resId);// 读取地图svg 把要解析的XML文档转化为输入流，以便 DOM 解析器解析它

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 获取DocumentBuilderFactory实例，调用 DocumentBuilderFactory.newInstance() 方法得到创建 DOM 解析器的工厂

            DocumentBuilder builder = null;

            try {

                builder = factory.newDocumentBuilder(); // 调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象

                Document doc = builder.parse(inputStream);// 解析svg的输入流 调用 DOM 解析器对象的 parse()方法解析XML文档，得到代表整个文档的Document对象，进行可以利用DOM特性对整个XML文档进行操作了

                Element rootElement = doc.getDocumentElement(); // 得到 XML文档的根节点

                NodeList items = rootElement.getElementsByTagName("path");



                NodeList items2 = rootElement.getChildNodes(); // 得到节点的子节点

                String str = rootElement.getAttribute("");

                //获取地图的整个上下左右位置，

                float left = -1;

                float right = -1;

                float top = -1;

                float bottom = -1;

                List<SvgItem> list = new ArrayList<>();

                for (int i = 0; i < items.getLength(); i++) {

                    Element element = (Element) items.item(i);

                    String pathData = element.getAttribute("android:pathData"); // 取得节点的属性值 可以读取path内的具体属性值

                    String idData = element.getAttribute("android:id"); // 具体属性值 — id

                    String colorData = element.getAttribute("android:fillColor"); // 具体属性值 — color



                    Log.d("HorseView", "run_000: " + pathData);

                    Log.d("HorseView", "run_111: " + colorData);

                    Log.d("HorseView", "run_222: " + idData);

                    @SuppressLint("RestrictedApi")

                    Path path = PathParser.createPathFromPathData(pathData);



                    SvgItem SvgItem = new SvgItem(path,nameList.get(i));//设置路径

                    SvgItem.setDrawColor(colorData);//设置颜色

                    //取每个省的上下左右 最后拿出最小或者最大的来充当 总地图的上下左右

                    RectF rect = new RectF();

                    path.computeBounds(rect, true);

                    left = left == -1 ? rect.left : Math.min(left, rect.left);

                    right = right == -1 ? rect.right : Math.max(right, rect.right);

                    top = top == -1 ? rect.top : Math.min(top, rect.top);

                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                    list.add(SvgItem);

                }

                itemList = list;

                totalRect = new RectF(left, top, right, bottom);//设置地图的上下左右位置



                //加载完以后刷新界面

                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override

                    public void run() {

                        requestLayout();

                        invalidate();

                    }

                });

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    };



    @Override

    public boolean onTouchEvent(MotionEvent event) {

        handleTouch(event.getX() / scale, event.getY() / scale);

        return super.onTouchEvent(event);

    }



    private void handleTouch(float x, float y) {

        if (itemList == null) {

            return;

        }

        SvgItem selectItem = null;

        for (SvgItem SvgItem : itemList) {

            if (SvgItem.isTouch(x, y)) {

                selectItem = SvgItem;

            }

        }

        if (selectItem != null) {

            select = selectItem;
            if(selectPostion != null){
                selectPostion.select(select,itemList.indexOf(selectItem));
            }
            postInvalidate();

        }

    }



    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (itemList != null) {

            canvas.save();

            canvas.scale(scale, scale);//把画布缩放匹配到本控件的宽高

            for (SvgItem SvgItem : itemList) {

                if (SvgItem != select) {

                    SvgItem.drawItem(canvas, paint, false);

                } else {

                    SvgItem.drawItem(canvas, paint, true);
//                    Toast.makeText(context,"你现在选择的省份是："+SvgItem.getName(),Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public void setSelect(int select) {
        if(itemList.size()>0){
        this.select = itemList.get(select);
        postInvalidate();
        }
    }
}


