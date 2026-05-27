package com.vishal.lld.designpattern.behavioural.observer.observables;

import java.util.ArrayList;
import java.util.List;

import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObservable;
import com.vishal.lld.designpattern.behavioural.observer.interfaces.OrderObserver;

public class OrderPublisher implements OrderObservable {

    private List<OrderObserver> orderEvents;

    public OrderPublisher() {
        orderEvents = new ArrayList<>();
    }

    @Override
    public void broadcast() {
        orderEvents.forEach(OrderObserver::trigger);
    }

    @Override
    public void register(OrderObserver observer) {
        if (observer == null)
            throw new IllegalArgumentException("OrderObserver cannot be null");
        orderEvents.add(observer);
    }

    @Override
    public void remove(OrderObserver observer) {
        orderEvents.remove(observer);
    }

}
