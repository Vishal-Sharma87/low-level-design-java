package com.vishal.lld.designpattern.creational.factory.v2_abstract_approach;

import com.vishal.lld.designpattern.creational.factory.v2_abstract_approach.interfaces.Notification;

/**
 * v2 - abstraction based approach
 * uses NotificationFactory to decouple object creation from business logic
 * if-else still exists inside factory
 */
public class AbstractNotificationService {

    /**
     * Sends message via given type
     * 
     * @param type    Notifiaction type
     * @param message Message to send
     * @throws IllegalArgumentException if "type" is not equls to supported ones
     */
    public void sendNotification(String type, String message) {

        // get the Notification object from the Factory
        Notification notification = NotificationFactory.getNotification(type);

        // send the message using that Notifications's "send" method
        notification.send(message);
    }
}
