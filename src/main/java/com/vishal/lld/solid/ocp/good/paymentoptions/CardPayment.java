package com.vishal.lld.solid.ocp.good.paymentoptions;

import com.vishal.lld.solid.ocp.good.interfaces.PaymentStrategy;

public class CardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing payemnt with CARD for amount: " + String.valueOf(amount));
    }

}
