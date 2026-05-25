package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory.QueueFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Queue;

public class ServiceBus implements Queue {

    @Override
    public void push() {
        System.out.println("[Azure ServiceBus] pushing...");
    }

    @Override
    public void register() {
        QueueFactory.register("azure", this);
    }

}
