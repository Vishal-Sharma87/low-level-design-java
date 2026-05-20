package com.vishal.lld.designpattern.creational.factory.v3_map_approach;

import com.vishal.lld.designpattern.creational.factory.v3_map_approach.interfaces.Notification;

/**
 * v3 - map based approach
 * uses NotificationFactory to decouple object creation from business logic
 */
public class MapNotificationService {

    /**
     * Sends message via given type
     * 
     * @param type    Notifiaction type
     * @param message Message to send
     * @throws IllegalArgumentException if "type" is not equls to supported ones
     */
    public void sendNotification(String type, String message) {

        // get the Notification object from the Factory
        Notification notification = MapBasedNotificationFactory.getNotification(type);

        // send the message using that Notifications's "send" method
        notification.send(message);
    }
}
