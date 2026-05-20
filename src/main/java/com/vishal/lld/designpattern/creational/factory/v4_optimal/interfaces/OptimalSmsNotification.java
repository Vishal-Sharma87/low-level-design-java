package com.vishal.lld.designpattern.creational.factory.v4_optimal.interfaces;

import com.vishal.lld.designpattern.creational.factory.v4_optimal.OptimalNotificationFactory;

public class OptimalSmsNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("[SMS] Sending message: " + message);
    }

    @Override
    public void register() {
        OptimalNotificationFactory.register("sms", this);
    }
}
