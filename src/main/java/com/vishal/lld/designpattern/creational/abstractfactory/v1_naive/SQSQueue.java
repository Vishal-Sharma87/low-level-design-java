package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory.QueueFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Queue;

public class SQSQueue implements Queue {

    @Override
    public void push() {
        System.out.println("[AWS SQS] pushing...");
    }

    @Override
    public void register() {
        QueueFactory.register("aws", this);
    }

}
