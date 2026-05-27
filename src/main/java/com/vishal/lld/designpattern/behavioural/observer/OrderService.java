package com.vishal.lld.designpattern.behavioural.observer;

import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObservable;

public class OrderService {

    private final OrderObservable orderObservable;

    public OrderService(OrderObservable orderObservable) {
        this.orderObservable = orderObservable;
    }

    public void placeOrder() {

        System.out.println("[Order Service] placing order...");

        orderObservable.broadcast();

    }

}
