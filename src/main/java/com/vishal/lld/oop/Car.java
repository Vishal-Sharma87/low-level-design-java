package com.vishal.lld.oop;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {

    private static AtomicInteger totalCars;

    static {
        totalCars = new AtomicInteger(0);
    }

    // Properties of car
    private String brand;
    private String color;
    private int speed;

    // constructor
    public Car(String brand, String color, int speed) {
        this.brand = brand;
        this.color = color;
        if (speed < 0)
            throw new IllegalArgumentException("Object creation with negative speed is not allowed");
        this.speed = speed;

        // atomically increase the totalCars count on successfull instance creation
        totalCars.incrementAndGet();
    }

    // returns the total cars created so far
    public static int getToTalCars() {
        return totalCars.get();
    }

    // demonstartion of final method
    public final void printCarPolicy() {
        System.out.println("No Negative speed allowed.");
    }

    // getters and setters
    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSpeed(int speed) {
        if (speed < 0) {
            System.out.println("Warning: Speed set fail, speed cannot be negative");
            return;
        }
        this.speed = speed;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Behaviours
    public void displayInfo() {
        System.out.println("displayInfo invoked from Car.");

        System.out.println("brand: " + brand);
        System.out.println("color: " + color);
        System.out.println("speed: " + speed);
    }
}
