package com.vishal.lld.casestudies.elevatorsystem.strategy;

import java.util.List;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;
import com.vishal.lld.casestudies.elevatorsystem.enums.Direction;
import com.vishal.lld.casestudies.elevatorsystem.enums.Status;
import com.vishal.lld.casestudies.elevatorsystem.interfaces.ElevatorSelectionStrategy;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy {

    @Override
    public Elevator select(List<Elevator> elevators, Direction direction, int destination) {
        if (elevators.isEmpty()) {
            throw new IllegalArgumentException("No Lifts to chooe from.");
        }

        int minCost = Integer.MAX_VALUE;
        Elevator best = elevators.get(0);

        /**
         * Assuming
         * topFloor = 10
         * groundFloor = 0
         * 
         * maxCostPerCycle = 10
         */

        for (int i = 0; i < elevators.size(); i++) {
            Elevator elevator = elevators.get(i);
            Status status = elevator.getStatus();
            int currentFloor = elevator.getCurrentFloor();
            int cost = 0;
            switch (status) {
                case MOVING_UP: {
                    if (direction.equals(Direction.UP)) {
                        if (currentFloor <= destination) {
                            cost += Math.abs(destination - currentFloor);
                        } else {
                            // topFLoor - currentFloor + maxCost + destination - groundFloor
                            cost += (10 - currentFloor) + destination;
                        }
                    } else {
                        cost += 10 - currentFloor + 10 - destination;
                    }
                }
                    break;

                case MOVING_DOWN: {
                    if (direction.equals(Direction.DOWN)) {
                        if (currentFloor >= destination) {
                            cost += Math.abs(destination - currentFloor);
                        } else {
                            cost += currentFloor + destination;
                        }
                    } else {
                        cost += currentFloor + destination;
                    }
                }
                    break;

                default: {
                    cost += Math.abs(destination - currentFloor);
                }
            }

            if (cost < minCost) {
                minCost = cost;
                best = elevator;
            }
        }
        return best;
    }

}
