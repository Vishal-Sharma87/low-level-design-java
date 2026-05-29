package com.vishal.lld.designpattern.behavioural.state.states;

import com.vishal.lld.designpattern.behavioural.state.VendingMachine;
import com.vishal.lld.designpattern.behavioural.state.interfaces.VendingMachineState;

public class IdleState implements VendingMachineState {

    private VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Coins inserted...");
        machine.setState(machine.getHasCoinState());
    }

    @Override
    public void selectItem() {
        System.out.println("Insert coins first, then select items.");
    }

    @Override
    public void dispense() {
        System.out.println("Insert coin and select item first before dispencing.");
    }

}
