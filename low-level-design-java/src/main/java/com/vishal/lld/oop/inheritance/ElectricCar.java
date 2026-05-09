package com.vishal.lld.oop.inheritance;

import com.vishal.lld.oop.Car;

public class ElectricCar extends Car {

    private int batteryLevel;

    public ElectricCar(String brand, String color, int speed, int batteryLevel) {
        super(brand, color, speed);
        if (batteryLevel < 0) {
            throw new IllegalArgumentException("Batery Level cannot be negative");
        }
        this.batteryLevel = batteryLevel;
    }

    public void charge() {
        System.out.println("Charging....");
        batteryLevel = Math.min(batteryLevel + 1, 100);
    }

    // overridden method
    @Override
    public void displayInfo() {
        System.out.println("displayInfo invoked from ElectricCar.");

        super.displayInfo();
        System.out.println("batteryLevel: " + this.batteryLevel);
    }

}
