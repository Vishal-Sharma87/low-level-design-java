package com.vishal.lld.casestudies.parkinglot.models;

import java.util.concurrent.atomic.AtomicInteger;

import com.vishal.lld.casestudies.parkinglot.enums.VehicleType;

public class Slot {
    private static AtomicInteger slotCount = new AtomicInteger(0);

    final int slotId;
    final VehicleType vehicleType;

    public Slot(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        this.slotId = slotCount.incrementAndGet();
    }

    public int getSlotId() {
        return slotId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

}
