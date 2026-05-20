package com.vishal.lld.designpattern.creational.factory.v3_map_approach;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.creational.factory.v3_map_approach.interfaces.Notification;

public class MapBasedNotificationFactory {

    // "type" to "Notifiaction" map
    private static Map<String, Notification> notificationMap;

    static {
        // initilaize the map
        notificationMap = new HashMap<>();
    }

    /**
     * Gives Noticiaction object based on type
     * 
     * @param type Notification type
     * @return Notification object
     * @throws IllegalArgumentException if type is unsupported
     */
    public static Notification getNotification(String type) {
        if (notificationMap.containsKey(type)) {
            return notificationMap.get(type);
        }
        throw new IllegalArgumentException("Undefinded type: " + type);
    }

    /**
     * Registers "type" & "Notification" in map
     * 
     * @param type         type of Notifiaction needed
     * @param notifiaction Associated Notifiaction object
     */
    public static void register(String type, Notification notifiaction) {
        notificationMap.put(type, notifiaction);
    }

    /*
     * Pros:
     * centralized factory class
     * Single source of truth
     * honours OCP
     * 
     * Cons:
     * Manual registration for each "type" Notification
     * caller in main must know both the
     * type string "email" and the concrete class EmailNotification
     * adding new type still requires change in main
     */

}
