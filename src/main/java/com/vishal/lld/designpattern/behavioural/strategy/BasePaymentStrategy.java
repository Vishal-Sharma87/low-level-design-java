package com.vishal.lld.designpattern.behavioural.strategy;

import com.vishal.lld.designpattern.behavioural.strategy.factory.PaymentStrategyFactory;
import com.vishal.lld.designpattern.behavioural.strategy.interfaces.PaymentStrategy;

public abstract class BasePaymentStrategy implements PaymentStrategy {
    protected String type;

    public BasePaymentStrategy(String type) {
        this.type = type;
    }

    @Override
    public void register() {
        PaymentStrategyFactory.register(type, this);

    }
}
