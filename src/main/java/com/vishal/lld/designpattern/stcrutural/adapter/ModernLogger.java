package com.vishal.lld.designpattern.stcrutural.adapter;

public class ModernLogger {
    /*
     * THIRD PARTY LOGGER SYSTEM SIMULATION
     */

    public void writeLog(String level, String message) {
        System.out.printf("%s %s %s\n", "[Modern Log]", level, message);
    }

}
