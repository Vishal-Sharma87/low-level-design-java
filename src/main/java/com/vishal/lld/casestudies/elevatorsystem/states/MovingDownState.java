package com.vishal.lld.casestudies.elevatorsystem.states;

import java.util.concurrent.PriorityBlockingQueue;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;
import com.vishal.lld.casestudies.elevatorsystem.enums.Status;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorState;

public class MovingDownState implements ElevatorState {

    @Override
    public void handle(Elevator elevator) {
        PriorityBlockingQueue<Integer> downQueue = elevator.getDownQueue();
        if (downQueue.isEmpty()) {

            elevator.updateStatusAndState(new IdleState(), Status.IDLE);
            System.out.printf("[MovingDownState 1] elevatorStatus of Elevator with elevatorId: %d is set as Idle\n",
                    elevator.getElevatorId());
            return;
        }

        if (elevator.getCurrentFloor() == downQueue.peek()) {
            System.out.printf("[MovingDownState 2] Elevator with elevatroId: %d arrived at destination: %d\n",
                    elevator.getElevatorId(), downQueue.poll());
            if (downQueue.isEmpty()) {
                System.out.printf("[MovingDownState 3] elevatorStatus of Elevator with elevatorId: %d is set as Idle\n",
                        elevator.getElevatorId());
                elevator.updateStatusAndState(new IdleState(), Status.IDLE);
            }
        } else if (elevator.getCurrentFloor() == 0) {
            elevator.updateStatusAndState(new IdleState(), Status.IDLE);
            System.out.printf("[MovingDownState 4] elevatorStatus of Elevator with elevatorId: %d is set as Idle\n",
                    elevator.getElevatorId());
            return;
        } else {
            elevator.moveDown();
        }

    }

}
