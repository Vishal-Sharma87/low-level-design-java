package com.vishal.lld.casestudies.elevatorsystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.vishal.lld.casestudies.elevatorsystem.enums.Status;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorState;
import com.vishal.lld.casestudies.elevatorsystem.observers.ElevatorObserver;
import com.vishal.lld.casestudies.elevatorsystem.states.IdleState;

public class Elevator implements Runnable {

    private static AtomicInteger ElevatorCount = new AtomicInteger(0);

    private final int elevatorId;
    private ElevatorState currentState;
    private Status elevatorStatus;
    private int currentFloor;
    private PriorityBlockingQueue<Integer> upQueue;
    private PriorityBlockingQueue<Integer> downQueue;

    private List<ElevatorObserver> elevatorObservers;

    public Elevator() {
        elevatorId = ElevatorCount.incrementAndGet();
        currentFloor = 0; // groud floor
        currentState = new IdleState();
        elevatorStatus = Status.IDLE;
        upQueue = new PriorityBlockingQueue<>();
        downQueue = new PriorityBlockingQueue<Integer>(11, Comparator.reverseOrder());
        elevatorObservers = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            currentState.handle(this);
        }
    }

    public void registerObserver(ElevatorObserver observer) {
        elevatorObservers.add(observer);
    }

    public void addFloorToQueue(int floor) {
        if (floor > 10 || floor < 0)
            throw new IllegalArgumentException("Floor must be within bound");

        synchronized (this) {
            if (currentFloor <= floor) {
                upQueue.offer(floor);
            } else {
                downQueue.offer(floor);
            }

            this.notify();
        }
    }

    public void moveUp() {
        currentFloor++;

        elevatorObservers.forEach(ob -> ob.track(elevatorId, currentFloor, elevatorStatus));

        sleepQuitely();
    }

    public void moveDown() {
        currentFloor--;

        elevatorObservers.forEach(ob -> ob.track(elevatorId, currentFloor, elevatorStatus));

        sleepQuitely();
    }

    private void sleepQuitely() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public PriorityBlockingQueue<Integer> getUpQueue() {
        return upQueue;
    }

    public PriorityBlockingQueue<Integer> getDownQueue() {
        return downQueue;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void updateStatusAndState(ElevatorState elevatorState, Status status) {
        this.currentState = elevatorState;
        this.elevatorStatus = status;
    }

    public Status getStatus() {
        return elevatorStatus;
    }
}
