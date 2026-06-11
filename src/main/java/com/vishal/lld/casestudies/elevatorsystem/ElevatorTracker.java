package com.vishal.lld.casestudies.elevatorsystem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vishal.lld.casestudies.elevatorsystem.enums.Status;
import com.vishal.lld.casestudies.elevatorsystem.observers.ElevatorObserver;

public class ElevatorTracker implements ElevatorObserver {

    private static volatile ElevatorTracker instance;

    private Map<Integer, Elevator> elevators;

    private ElevatorTracker() {
        elevators = new ConcurrentHashMap<>();
    }

    public static ElevatorTracker getInstance(List<Elevator> elevatorsToRegister) {
        if (instance == null) {
            synchronized (ElevatorTracker.class) {
                if (instance == null) {
                    instance = new ElevatorTracker();

                    elevatorsToRegister.forEach(e -> instance.elevators.put(e.getElevatorId(), e));
                }
            }
        }
        return instance;
    }

    @Override
    public void track(int elevatorId, int currentFloor, Status elevatorStatus) {
        System.out.printf("Elevator with elevatoId: %d is at floor: %d with currentStatus: %s\n", elevatorId,
                currentFloor, elevatorStatus.toString());
    }

    public List<Elevator> getElevators() {
        return elevators.values().stream().toList();
    }

}
