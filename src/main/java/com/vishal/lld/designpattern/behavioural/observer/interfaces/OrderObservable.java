package com.vishal.lld.designpattern.behavioural.observer.interfaces;

public interface OrderObservable {

    void broadcast();

    void register(OrderObserver observer);

    void remove(OrderObserver observer);

}
