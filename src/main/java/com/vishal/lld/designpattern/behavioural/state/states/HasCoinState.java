package com.vishal.lld.designpattern.behavioural.state.states;

import com.vishal.lld.designpattern.behavioural.state.VendingMachine;
import com.vishal.lld.designpattern.behavioural.state.interfaces.VendingMachineState;

public class HasCoinState implements VendingMachineState {

    private VendingMachine machine;

    public HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Coins already inserted, select items to dispense.");
    }

    @Override
    public void selectItem() {
        System.out.println("Selecting items...");
        machine.setState(machine.getdispenseState());
    }

    @Override
    public void dispense() {
        System.out.println("Select item first, before dispencing...");
    }

}
