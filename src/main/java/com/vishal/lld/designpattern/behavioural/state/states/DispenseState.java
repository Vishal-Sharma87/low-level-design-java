package com.vishal.lld.designpattern.behavioural.state.states;

import com.vishal.lld.designpattern.behavioural.state.VendingMachine;
import com.vishal.lld.designpattern.behavioural.state.interfaces.VendingMachineState;

public class DispenseState implements VendingMachineState {

    private VendingMachine machine;

    public DispenseState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("You've already inserted coin and have selected Items, dispense them first.");
    }

    @Override
    public void selectItem() {
        System.out.println("You've already selected Items, dispense them first.");
    }

    @Override
    public void dispense() {
        System.out.println("Dispensing Item...");
        machine.setState(machine.getIdleState());
    }

}
