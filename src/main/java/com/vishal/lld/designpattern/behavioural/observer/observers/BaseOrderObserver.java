package com.vishal.lld.designpattern.behavioural.observer.observers;

import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObservable;
import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObserver;

public abstract class BaseOrderObserver implements OrderObserver {

    protected OrderObservable orderObservable;

    public BaseOrderObserver(OrderObservable orderObservable) {
        if (orderObservable == null)
            throw new IllegalArgumentException("OrderObservale cannot be null");

        this.orderObservable = orderObservable;
    }

    @Override
    public void register() {
        orderObservable.register(this);
    }

}