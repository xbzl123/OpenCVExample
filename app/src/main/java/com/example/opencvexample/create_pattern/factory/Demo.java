package com.example.opencvexample.create_pattern.factory;
interface Printer{
    void print();
}
class WebPrinter implements Printer{

    @Override
    public void print() {
        System.out.println("WebPrinter");
    }
}

class PaperPrinter implements Printer{
    @Override
    public void print() {
        System.out.println("PaperPrinter");
    }
}
class PrinterFactory{
    public Printer getPrinter(String name){
        if(name == null){
            return null;
        }else if(name.equalsIgnoreCase("web")){
            return new WebPrinter();
        }else if(name.equalsIgnoreCase("paper")){
            return new PaperPrinter();
        }
        return null;
    }
}

class Demo {
    public static void main(String[] args) {
        PrinterFactory printerFactory = new PrinterFactory();
        Printer web = printerFactory.getPrinter("web");
        web.print();
        Printer paper = printerFactory.getPrinter("paper");
        paper.print();

    }
}
