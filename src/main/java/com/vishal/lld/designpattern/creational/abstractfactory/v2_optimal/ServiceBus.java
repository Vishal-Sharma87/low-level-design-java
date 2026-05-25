package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Queue;

public class ServiceBus implements Queue {

    @Override
    public void push() {
        System.out.println("[Azure ServiceBus] pushing...");
    }

}
