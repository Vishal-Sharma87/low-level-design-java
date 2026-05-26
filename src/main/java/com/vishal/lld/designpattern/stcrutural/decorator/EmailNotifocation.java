package com.vishal.lld.designpattern.stcrutural.decorator;

import com.vishal.lld.designpattern.stcrutural.decorator.interfaces.Notification;

public class EmailNotifocation implements Notification {

    @Override
    public void send(String message) {
        System.out.println("[EMAIL MESSAGE]" + message);
    }

}
