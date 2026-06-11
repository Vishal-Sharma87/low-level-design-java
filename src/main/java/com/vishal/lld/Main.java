package com.vishal.lld;

import java.util.List;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;
import com.vishal.lld.casestudies.elevatorsystem.ElevatorAssignmentService;
import com.vishal.lld.casestudies.elevatorsystem.ElevatorTracker;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorSelectionStrategy;
import com.vishal.lld.casestudies.elevatorsystem.panels.ElevatorCabinPanel;
import com.vishal.lld.casestudies.elevatorsystem.panels.HallPanel;
import com.vishal.lld.casestudies.elevatorsystem.strategy.NearestElevatorStrategy;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Elevator elevator1 = new Elevator();
        Elevator elevator2 = new Elevator();
        Elevator elevator3 = new Elevator();

        ElevatorCabinPanel elevatorCabinPanel1 = new ElevatorCabinPanel(elevator1);
        ElevatorCabinPanel elevatorCabinPanel2 = new ElevatorCabinPanel(elevator2);
        ElevatorCabinPanel elevatorCabinPanel3 = new ElevatorCabinPanel(elevator3);

        Thread thread1 = new Thread(elevator1);
        Thread thread2 = new Thread(elevator2);
        Thread thread3 = new Thread(elevator3);

        ElevatorTracker elevatorTracker = ElevatorTracker.getInstance(List.of(elevator1, elevator2, elevator3));

        elevator1.registerObserver(elevatorTracker);
        elevator2.registerObserver(elevatorTracker);
        elevator3.registerObserver(elevatorTracker);

        ElevatorAssignmentService elevatorAssignmentService = new ElevatorAssignmentService(elevatorTracker);
        ElevatorSelectionStrategy elevatorSelectionStrategy = new NearestElevatorStrategy();

        HallPanel hallPanel = new HallPanel(elevatorAssignmentService, elevatorSelectionStrategy);

        thread1.start();
        thread2.start();
        thread3.start();

        hallPanel.down(3);
        hallPanel.up(5);
        hallPanel.down(2);
        hallPanel.down(7);
        hallPanel.up(6);
        hallPanel.down(1);
        hallPanel.up(10);
        hallPanel.down(5);
        hallPanel.down(2);

    }
}