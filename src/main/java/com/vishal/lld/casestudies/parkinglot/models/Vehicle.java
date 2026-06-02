package com.vishal.lld.casestudies.parkinglot.models;

import java.util.concurrent.atomic.AtomicInteger;

import com.vishal.lld.casestudies.parkinglot.enums.VehicleType;

public class Vehicle {
    private static AtomicInteger vehicleCount = new AtomicInteger(0);

    final int vehicleId;
    final VehicleType vehicleType;

    public Vehicle(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        
        this.vehicleType = vehicleType;
        this.vehicleId = vehicleCount.incrementAndGet();
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

}
