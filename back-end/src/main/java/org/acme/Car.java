package org.acme;

public class Car {

    private double x;
    private double y;

    public Car(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Car() {
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}