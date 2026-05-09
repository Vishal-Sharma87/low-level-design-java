package com.vishal.lld.oop.abstraction;

import com.vishal.lld.oop.Car;

public class HybridCar extends Car implements Chargeable {

    private int fuelLevel;

    public HybridCar(String brand, String color, int speed, int fuelLevel) {
        super(brand, color, speed);

        if (fuelLevel < 0) {
            throw new IllegalArgumentException("Fuel Level cannot be negative");
        }
        this.fuelLevel = fuelLevel;
    }

    @Override
    public void displayInfo() {
        System.out.println("displayInfo invoked for HybridCar.");
        super.displayInfo();

        System.out.println("fuelLevel: " + this.fuelLevel);
    }

    @Override
    public void charge() {
        System.out.println("Charging HybridCar...");
        this.fuelLevel = Math.min(fuelLevel + 1, 100);
    }

}
