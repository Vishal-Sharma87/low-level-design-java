package com.vishal.lld.designpattern.behavioural.observer.observers;

import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObservable;

public class OrderEmailObserver extends BaseOrderObserver {

    public OrderEmailObserver(OrderObservable orderObservable) {
        super(orderObservable);
    }

    @Override
    public void trigger() {
        System.out.println("[Email Observer] sending email...");
    }

}
