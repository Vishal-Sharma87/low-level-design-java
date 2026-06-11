package com.vishal.lld.casestudies.elevatorsystem;

import java.util.List;

import com.vishal.lld.casestudies.elevatorsystem.enums.Direction;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorSelectionStrategy;

public class ElevatorAssignmentService {

    private final ElevatorTracker elevatorTracker;

    public ElevatorAssignmentService(ElevatorTracker elevatorTracker) {
        this.elevatorTracker = elevatorTracker;
    }

    public void assignElevator(Direction direction, int currentFloor, ElevatorSelectionStrategy selectionStrategy) {
        /*
         * Get the elevators from ElevatorTracker
         * call the strategy to get the best available Elevator
         * put the request in that lift
         * return the lift
         */

        List<Elevator> elevators = elevatorTracker.getElevators();

        Elevator elevator = selectionStrategy.select(elevators, direction, currentFloor);

        System.out.printf("Floor: %d, direction: %s is assigned to elevatorId: %d\n", currentFloor,
                direction.toString(), elevator.getElevatorId());

        elevator.addFloorToQueue(currentFloor);
    }
}
