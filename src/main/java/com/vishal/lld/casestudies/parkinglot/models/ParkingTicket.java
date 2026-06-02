package com.vishal.lld.casestudies.parkinglot.models;

import java.time.Instant;

import com.vishal.lld.casestudies.parkinglot.enums.VehicleType;

public class ParkingTicket {
    final int slotId;
    final int vehicleId;
    final VehicleType vehicleType;
    final Instant arrivalTime;

    public ParkingTicket(int slotId, int vehicleId, VehicleType vehicleType) {
        this.slotId = slotId;
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.arrivalTime = Instant.now();
    }

    public int getSlotId() {
        return slotId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

}
