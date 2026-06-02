package com.vishal.lld.casestudies.parkinglot.paymentstrategies;

import com.vishal.lld.casestudies.parkinglot.factory.PaymentStrategyFactory;
import com.vishal.lld.casestudies.parkinglot.interfaces.PaymentStrategy;

public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public void register() {
        PaymentStrategyFactory.register("upi", this);
    }

    @Override
    public void pay() {
        System.out.println("Paying via UPI");
    }

}
