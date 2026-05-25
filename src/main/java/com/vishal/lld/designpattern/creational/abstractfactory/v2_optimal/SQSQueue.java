package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Queue;

public class SQSQueue implements Queue {

    @Override
    public void push() {
        System.out.println("[AWS SQS] pushing...");
    }

}
