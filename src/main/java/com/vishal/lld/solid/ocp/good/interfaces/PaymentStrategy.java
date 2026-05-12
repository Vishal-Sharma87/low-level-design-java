package com.vishal.lld.solid.ocp.good.interfaces;

public interface PaymentStrategy {

    // The contract -> every payment option must implement the "pay" method
    public void pay(double amount);

}
