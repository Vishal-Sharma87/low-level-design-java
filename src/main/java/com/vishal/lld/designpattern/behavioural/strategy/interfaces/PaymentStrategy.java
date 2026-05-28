package com.vishal.lld.designpattern.behavioural.strategy.interfaces;

public interface PaymentStrategy {
    // real signature would be pay(double amount, User user)
    // simplified here to focus on the pattern
    void pay();

    void register();
}
