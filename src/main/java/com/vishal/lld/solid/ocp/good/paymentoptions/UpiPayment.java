package com.vishal.lld.solid.ocp.good.paymentoptions;

import com.vishal.lld.solid.ocp.good.interfaces.PaymentStrategy;

public class UpiPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing payemnt with UPI for amount: " + String.valueOf(amount));
    }

}
