package com.vishal.lld.casestudies.parkinglot;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vishal.lld.casestudies.parkinglot.enums.VehicleType;

public class SlotManager {

    private static volatile SlotManager instance;

    private Map<VehicleType, LinkedHashSet<Integer>> unOccupiedSlotMap;
    private Map<VehicleType, LinkedHashSet<Integer>> occupiedSlotMap;

    private SlotManager() {
        this.unOccupiedSlotMap = new ConcurrentHashMap<>();
        this.occupiedSlotMap = new ConcurrentHashMap<>();
    }

    public static SlotManager getInstance() {
        if (instance == null) {
            synchronized (SlotManager.class) {
                if (instance == null) {
                    instance = new SlotManager();
                }
            }
        }
        return instance;
    }

    public void populateUnocuppiedSlot(VehicleType type, int slotId) {
        unOccupiedSlotMap.computeIfAbsent(type, t -> new LinkedHashSet<>()).add(slotId);
    }

    public void freeSlot(VehicleType type, int id) {
        unOccupiedSlotMap.get(type).add(id);
        occupiedSlotMap.get(type).remove(id);

        System.out.println("Slot freed, id: " + id);
    }

    public int getAvailableSlot(VehicleType type) {
        if (unOccupiedSlotMap.get(type).isEmpty()) {
            throw new RuntimeException("No empty slots available for type: " + type.toString());
        }

        int freedSlotId = unOccupiedSlotMap.get(type).removeFirst();
        occupiedSlotMap.computeIfAbsent(type, t -> new LinkedHashSet<>()).add(freedSlotId);

        System.out.println("Slot occupied: " + freedSlotId);
        return freedSlotId;
    }

}
