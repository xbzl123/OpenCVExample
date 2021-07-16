package com.example.opencvexample.structrue_pattern.decoration;

interface Printer{
    void print();
}

class PaperPrinter implements Printer{
    @Override
    public void print() {
        System.out.println("PaperPrinter ...");
    }
}
class PlasticPrinter implements Printer{
    @Override
    public void print() {
        System.out.println("PlasticPrinter ...");
    }
}
//装饰器
class PrinterDecoration implements Printer{
    Printer p;
    public PrinterDecoration(Printer printer) {
        this.p = printer;
    }

    @Override
    public void print() {
        p.print();
    }
}

//增加新的功能者
class Printer3D extends PrinterDecoration{
    public Printer3D(Printer printer) {
        super(printer);
    }

    @Override
    public void print() {
        System.out.println("3D ...");
        p.print();
    }
}
class Demo {
    public static void main(String[] args) {
        Printer printer = new PaperPrinter();
        Printer printer3DPaper = new Printer3D(printer);
        Printer printer3DPlastic = new Printer3D(new PlasticPrinter());
        printer.print();
        printer3DPaper.print();
        printer3DPlastic.print();
    }
}
