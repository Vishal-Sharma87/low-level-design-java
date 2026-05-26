package com.vishal.lld.designpattern.stcrutural.decorator.decorators;

import com.vishal.lld.designpattern.stcrutural.decorator.interfaces.Notification;

public class LoggingDecorator extends NotificationDecorator {

    public LoggingDecorator(Notification wrapped) {
        super(wrapped);
    }

    @Override
    public void send(String message) {
        System.out.println("[LOG] Sending message: " + message);
        super.send(message);
        System.out.println("[LOG] Message sent: " + message);
    }
}
