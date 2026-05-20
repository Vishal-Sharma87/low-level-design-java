package com.vishal.lld.designpattern.creational.factory.v3_map_approach.interfaces;

public class EmailNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("[Email] Sending message: " + message);
    }
}
