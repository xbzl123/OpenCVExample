package com.example.opencvexample.action_pattern.strategy;

class Dog {
    private int weight;
    private int heigt;
    private int width;

    public Dog(int weight) {
        this.weight = weight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigt() {
        return heigt;
    }

    public void setHeigt(int heigt) {
        this.heigt = heigt;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "weight=" + weight +
                ", heigt=" + heigt +
                ", width=" + width +
                '}';
    }
}
