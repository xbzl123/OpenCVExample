package com.example.opencvexample.create_pattern.absratct_factory;

interface Sharp{
    void draw();
}
class Circle implements Sharp{

    @Override
    public void draw() {
        System.out.println("Circle");
    }
}

class Rectangle implements Sharp{

    @Override
    public void draw() {
        System.out.println("Rectangle");
    }
}
interface Printer{
    void print();
}
class WebPrinter implements Printer {

    @Override
    public void print() {
        System.out.println("WebPrinter");
    }
}

class PaperPrinter implements Printer {
    @Override
    public void print() {
        System.out.println("PaperPrinter");
    }
}
abstract class AbstractFactory{
    abstract Printer getPrinter(String name);
    abstract Sharp getSharp(String name);
}
class PrinterFactory extends AbstractFactory{

    @Override
    Printer getPrinter(String name) {
        if (name == null) {
            return null;
        }
        if(name.equalsIgnoreCase("PaperPrinter")){
            return new PaperPrinter();
        }else {
            return new WebPrinter();
        }
    }

    @Override
    Sharp getSharp(String name) {
        return null;
    }
}

class SharpFactory extends AbstractFactory{

    @Override
    Printer getPrinter(String name) {
        return null;
    }

    @Override
    Sharp getSharp(String name) {
        if(name == null){
            return null;
        }
        if(name.equalsIgnoreCase("Rectangle")){
            return new Rectangle();
        }else {
            return new Circle();
        }
    }
}
class Demo {
    public static void main(String[] args) {
        PrinterFactory printerFactory = new PrinterFactory();
        Printer web = printerFactory.getPrinter("web");
        Printer paper = printerFactory.getPrinter("paper");
        web.print();
        paper.print();
        SharpFactory sharpFactory = new SharpFactory();
        Sharp rectangle = sharpFactory.getSharp("Rectangle");
        rectangle.draw();
    }
}
