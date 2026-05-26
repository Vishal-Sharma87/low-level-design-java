package com.vishal.lld.designpattern.stcrutural.adapter;

import com.vishal.lld.designpattern.stcrutural.adapter.interfaces.Logger;

public class LegacyLogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println("[Legacy Log] " + message);
    }

}
