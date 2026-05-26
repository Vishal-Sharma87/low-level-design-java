package com.vishal.lld.designpattern.stcrutural.decorator.decorators;

import com.vishal.lld.designpattern.stcrutural.decorator.interfaces.Notification;

public class EncryptionDecorator extends NotificationDecorator {

    public EncryptionDecorator(Notification wrapped) {
        super(wrapped);
    }

    @Override
    public void send(String message) {
        String encryted = "[Encrypted] " + message;
        super.send(encryted);
    }

}
