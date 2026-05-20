package com.vishal.lld.designpattern.creational.factory.v2_abstract_approach.interfaces;

public class EmailNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("[Email] Sending message: " + message);
    }
}
