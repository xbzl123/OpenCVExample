package com.example.opencvexample.structrue_pattern.proxy_pattern;

interface Printer{
    void print();
}

class ColorPrinter implements Printer{
    private String name;

    public ColorPrinter(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println("color print ..."+name);
    }
}
//代理器
class ProxyPrinter implements Printer{

    private ColorPrinter colorPrinter;
    private String name;

    public ProxyPrinter(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        if (colorPrinter == null) {
            colorPrinter = new ColorPrinter(name);
        }
        colorPrinter.print();
    }
}

class Demo {
    public static void main(String[] args) {
        Printer test = new ProxyPrinter(" this is a test");
        test.print();
    }
}
