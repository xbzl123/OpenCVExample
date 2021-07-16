package com.example.opencvexample.structrue_pattern.bridge_pattern;
//桥接接口
interface Printer{
    void print(int w, int h, int radius);
}

class ColorPrinter implements Printer{

    @Override
    public void print(int w, int h, int radius) {
        System.out.println("ColorPrinter="+w+",radius="+radius);
    }
}

class BlackPrinter implements Printer{

    @Override
    public void print(int w, int h, int radius) {
            System.out.println("BlackPrinter="+w+",radius="+radius);
        }
}
abstract class Shape{
     Printer printer;
    Shape(Printer p){
        this.printer = p;
    }
    abstract void draw();
}
class Circle extends Shape {

    private int w, h, radius;
    Circle(int w,int h,int radius,Printer p) {
        super(p);
        this.w = w;
        this.h = h;
        this.radius = radius;
    }

    @Override
    void draw() {
        printer.print(w,h,radius);
    }
}
class Demo {
    public static void main(String[] args) {
        Circle blackcircle = new Circle(10, 10, 10, new BlackPrinter());
        blackcircle.draw();

        Circle colorcircle = new Circle(10, 10, 10, new ColorPrinter());
        colorcircle.draw();
    }

}
