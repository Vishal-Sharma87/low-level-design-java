package com.vishal.lld.designpattern.creational.factory.v2_abstract_approach.interfaces;

public class SmsNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("[SMS] Sending message: " + message);
    }
}
