package com.vishal.lld.designpattern.creational.factory.v1_naive;

public class NaiveNotificationService {

    /**
     * Sends message via given type
     * 
     * @param type    Notifiaction type
     * @param message Message to send
     * @throws IllegalArgumentException if "type" is not equls to supported ones
     */
    public void sendNotification(String type, String message) {

        if ("email".equals(type)) {
            System.out.println("[Email] sending message: " + message);
        } else if ("sms".equals(type)) {
            System.out.println("[SMS] sending message: " + message);
        } else {
            throw new IllegalArgumentException("Undefined notification type: " + type);
        }
    }

    /*
     * Pros:
     * easy to reason about and write
     * 
     * cons:
     * violates OCP
     * no abstraction — caller is coupled to this class directly,
     * cannot swap or mock notification behavior for testing
     * every new notification type requires modifying this method
     * in a large codebase this method could be called from 50 places
     * all 50 need to be updated — classic OCP violation
     */

}
