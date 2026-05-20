package com.vishal.lld.designpattern.creational.factory.v2_abstract_approach;

import com.vishal.lld.designpattern.creational.factory.v2_abstract_approach.interfaces.EmailNotification;
import com.vishal.lld.designpattern.creational.factory.v2_abstract_approach.interfaces.Notification;
import com.vishal.lld.designpattern.creational.factory.v2_abstract_approach.interfaces.SmsNotification;

public class NotificationFactory {

    /**
     * Gives Noticiaction object based on type
     * 
     * @param type Notification type
     * @return Notification object
     * @throws IllegalArgumentException if type is unsupported
     */
    public static Notification getNotification(String type) {
        if ("email".equals(type)) {
            return new EmailNotification();
        } else if ("sms".equals(type)) {
            return new SmsNotification();
        } else {
            throw new IllegalArgumentException("Undefined notification type: " + type);
        }
    }

    /*
     * Pros:
     * centralized factory class
     * Single source of truth
     * 
     * Cons:
     * Violates OCP
     */

}
