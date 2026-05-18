package com.vishal.lld.designpattern.creational.singleton.v1_naive;

public class NaiveLogger {

    // static NaiveLogger field repesenting Instance
    private static NaiveLogger instance;

    // private constructor to avoid the outside creation
    private NaiveLogger() {
    }

    // public and static method to return the instance -> CAN CAUSE RACE CONDITIONS
    public static NaiveLogger getInstance() {
        if (instance == null) { // two classes can read "null" at same time and cause creating two instances
            instance = new NaiveLogger();
        }
        return instance;
    }

    // Log the message
    public void log(String message) {
        System.out.println("[Naive Log] " + message);
    }
}
