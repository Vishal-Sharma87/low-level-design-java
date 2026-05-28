package com.vishal.lld.designpattern.behavioural.strategy;

public class UpiStrategy extends BasePaymentStrategy {

    public UpiStrategy() {
        super("upi");
    }

    @Override
    public void pay() {
        System.out.println("Paying via upi...");
    }

}
