package com.vishal.lld.designpattern.behavioural.strategy;

public class CardStrategy extends BasePaymentStrategy {

    public CardStrategy() {
        super("card");
    }

    @Override
    public void pay() {
        System.out.println("Paying via card...");
    }

}
