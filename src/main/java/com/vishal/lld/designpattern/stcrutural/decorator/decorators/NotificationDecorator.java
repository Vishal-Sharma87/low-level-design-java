package com.vishal.lld.designpattern.stcrutural.decorator.decorators;

import com.vishal.lld.designpattern.stcrutural.decorator.interfaces.Notification;

public abstract class NotificationDecorator implements Notification {
    protected Notification wrapped;

    public NotificationDecorator(Notification wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Wrapped cannot be null");
        }
        this.wrapped = wrapped;
    }

    public void send(String message) {
        wrapped.send(message);
    }
}
