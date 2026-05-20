package com.vishal.lld.designpattern.creational.factory.v4_optimal;

import com.vishal.lld.designpattern.creational.factory.v4_optimal.interfaces.Notification;

public class OptimalNotificationService {

    /**
     * Sends message via given type
     * 
     * @param type    Notifiaction type
     * @param message Message to send
     * @throws IllegalArgumentException if "type" is not equls to supported ones
     */
    public void sendNotification(String type, String message) {

        Notification notification = OptimalNotificationFactory.getNotification(type);

        notification.send(message);
    }
}
