package com.example.opencvexample.structrue_pattern.facade_pattern;

class ShapeFacade {
    interface Shape{
        void draw();
    }

    class SquareShape implements Shape{

        @Override
        public void draw() {
            System.out.println("SquareShape : draw");
        }
    }

    SquareShape squareShape = new SquareShape();

    public void drawSquare() {
        squareShape.draw();
    }

    public static void main(String[] args) {
        ShapeFacade shapeFacade = new ShapeFacade();
        shapeFacade.drawSquare();
    }
}
