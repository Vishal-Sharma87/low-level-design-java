package com.vishal.lld.oop.abstraction;

public abstract class Shape {
    
    public abstract double area();

    public void displayArea() {
        System.out.println(this.area());
    }

}
