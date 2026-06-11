package com.vishal.lld.casestudies.elevatorsystem.panels;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;

public class ElevatorCabinPanel {

    private final Elevator elevator;
    // making final so that no one cannot change it, Each Cabin belongs to one
    // elevator

    public ElevatorCabinPanel(Elevator elevator) {
        this.elevator = elevator;
    }

    public void pressButton(int destinationFloor) {
        int currentFloor = elevator.getCurrentFloor();

        if (currentFloor == destinationFloor) {
            System.out.println("You already are at floor: " + destinationFloor);
            return;
        }
        if (currentFloor < destinationFloor) {
            elevator.addFloorToQueue(destinationFloor);
        } else
            elevator.addFloorToQueue(destinationFloor);
    }
}
