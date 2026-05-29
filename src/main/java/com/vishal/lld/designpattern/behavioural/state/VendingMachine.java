package com.vishal.lld.designpattern.behavioural.state;

import com.vishal.lld.designpattern.behavioural.state.interfaces.VendingMachineState;
import com.vishal.lld.designpattern.behavioural.state.states.DispenseState;
import com.vishal.lld.designpattern.behavioural.state.states.HasCoinState;
import com.vishal.lld.designpattern.behavioural.state.states.IdleState;

public class VendingMachine {
    private VendingMachineState currentState;
    private VendingMachineState IdleState;
    private VendingMachineState hasCoinState;
    private VendingMachineState dispenseState;

    public VendingMachine() {

        IdleState = new IdleState(this);
        hasCoinState = new HasCoinState(this);
        dispenseState = new DispenseState(this);

        this.currentState = IdleState;
    }

    public void setState(VendingMachineState state) {
        if (state == null)
            throw new IllegalArgumentException("VendingMachineState cannot be null");

        currentState = state;
    }

    public VendingMachineState getIdleState() {
        return IdleState;
    }

    public VendingMachineState getHasCoinState() {
        return hasCoinState;
    }

    public VendingMachineState getdispenseState() {
        return dispenseState;
    }

    public void insertCoin() {
        currentState.insertCoin();
    }

    public void selectItem() {
        currentState.selectItem();
    }

    public void dispense() {
        currentState.dispense();
    }
}
