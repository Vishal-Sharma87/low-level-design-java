package com.vishal.lld.solid.ocp.good.paymentoptions;

import com.vishal.lld.solid.ocp.good.interfaces.PaymentStrategy;

public class NetbankingPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Processing payemnt with Netbanking for amount: " + String.valueOf(amount));
    }

}
