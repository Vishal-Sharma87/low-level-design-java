package com.vishal.lld.designpattern.behavioural.observer.observers;

import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObservable;

public class OrderNotificationObserver extends BaseOrderObserver {

    public OrderNotificationObserver(OrderObservable orderObservable) {
        super(orderObservable);
    }

    @Override
    public void trigger() {
        System.out.println("[Notification Observer]  sending notification...");
    }

}
