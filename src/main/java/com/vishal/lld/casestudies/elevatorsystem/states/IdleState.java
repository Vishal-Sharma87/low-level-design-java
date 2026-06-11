package com.vishal.lld.casestudies.elevatorsystem.states;

import java.util.concurrent.PriorityBlockingQueue;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;
import com.vishal.lld.casestudies.elevatorsystem.enums.Status;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorState;

public class IdleState implements ElevatorState {

    @Override
    public void handle(Elevator elevator) {
        PriorityBlockingQueue<Integer> upQueue = elevator.getUpQueue();
        PriorityBlockingQueue<Integer> downQueue = elevator.getDownQueue();

        PriorityBlockingQueue<Integer> queue = upQueue.isEmpty() ? downQueue : upQueue;

        if (queue.isEmpty()) {
            // No request to process -> ask Elevator to wait untill request arrives

            synchronized (elevator) {
                try {
                    System.out.println("SYNCHRONIZED");
                    elevator.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } else {
            if (elevator.getCurrentFloor() < queue.peek()) {
                elevator.updateStatusAndState(new MovingUpState(), Status.MOVING_UP);
            } else {
                elevator.updateStatusAndState(new MovingDownState(), Status.MOVING_DOWN);
            }
        }

    }

}
