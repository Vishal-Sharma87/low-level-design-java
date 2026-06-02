package com.vishal.lld.casestudies.parkinglot.enums;

import java.util.List;

public enum VehicleType {
    BIKE, CAR, TRUCK;

    public static List<VehicleType> getValues() {
        return List.of(VehicleType.values());
    }
}
