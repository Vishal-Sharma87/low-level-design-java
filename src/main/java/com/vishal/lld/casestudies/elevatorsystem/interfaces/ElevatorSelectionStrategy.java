package com.vishal.lld.casestudies.elevatorsystem.interfaces;

import java.util.List;

import com.vishal.lld.casestudies.elevatorsystem.Elevator;
import com.vishal.lld.casestudies.elevatorsystem.enums.Direction;

public interface ElevatorSelectionStrategy {
    Elevator select(List<Elevator> elevators, Direction direction, int currentFloor);
}
