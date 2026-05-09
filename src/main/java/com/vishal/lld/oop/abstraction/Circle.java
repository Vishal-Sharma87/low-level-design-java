package com.vishal.lld.oop.abstraction;

public class Circle extends Shape {

    private static final double PI = Math.PI;

    private double radius;

    public Circle(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Negative Radius is not allowed");
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return PI * radius * radius;
    }

}
