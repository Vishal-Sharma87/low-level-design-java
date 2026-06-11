package com.vishal.lld.casestudies.elevatorsystem.states;

import java.util.concurrent.PriorityBlockingQueue;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;
import com.vishal.lld.casestudies.elevatorsystem.enums.Status;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorState;

public class MovingUpState implements ElevatorState {

    @Override
    public void handle(Elevator elevator) {
        PriorityBlockingQueue<Integer> upQueue = elevator.getUpQueue();
        if (upQueue.isEmpty()) {
            elevator.updateStatusAndState(new IdleState(), Status.IDLE);
            System.out.println("Elevator state set as IdleState");
            return;
        }

        if (elevator.getCurrentFloor() == upQueue.peek()) {
            System.out.printf("Elevator with elevatroId: %d arrived at destination: %d\n", elevator.getElevatorId(),
                    upQueue.poll());
            if (upQueue.isEmpty()) {
                System.out.printf("elevatorStatus of Elevator with elevatorId: %d is set as Idle\n",
                        elevator.getElevatorId());
                elevator.updateStatusAndState(new IdleState(), Status.IDLE);
            }
        } else if (elevator.getCurrentFloor() == 10) {
            elevator.updateStatusAndState(new IdleState(), Status.IDLE);
            System.out.printf("elevatorStatus of Elevator with elevatorId: %d is set as Idle\n",
                    elevator.getElevatorId());
        } else {
            elevator.moveUp();
        }

    }

}
