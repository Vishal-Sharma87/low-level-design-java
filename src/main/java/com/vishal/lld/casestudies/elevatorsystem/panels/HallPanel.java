package com.vishal.lld.casestudies.elevatorsystem.panels;

import com.vishal.lld.casestudies.elevatorsystem.ElevatorAssignmentService;
import com.vishal.lld.casestudies.elevatorsystem.enums.Direction;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorSelectionStrategy;

public class HallPanel {
    private ElevatorAssignmentService elevatorAssignmentService;
    private ElevatorSelectionStrategy elevatorSelectionStrategy;

    public HallPanel(ElevatorAssignmentService elevatorAssignmentService,
            ElevatorSelectionStrategy elevatorSelectionStrategy) {
        this.elevatorAssignmentService = elevatorAssignmentService;
        this.elevatorSelectionStrategy = elevatorSelectionStrategy;
    }

    public void up(int currentFloor) {
        System.out.println("Incoming UP request for floor: " + currentFloor);
        elevatorAssignmentService.assignElevator(Direction.UP, currentFloor, elevatorSelectionStrategy);
    }

    public void down(int currentFloor) {
        System.out.println("Incoming DOWN request for floor: " + currentFloor);
        elevatorAssignmentService.assignElevator(Direction.DOWN, currentFloor, elevatorSelectionStrategy);
    }
}
