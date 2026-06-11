package com.vishal.lld.casestudies.elevatorsystem.observers;

import com.vishal.lld.casestudies.elevatorsystem.enums.Status;

public interface ElevatorObserver {
    void track(int elevatorId, int currentFloor, Status elevatorStatus);
}
