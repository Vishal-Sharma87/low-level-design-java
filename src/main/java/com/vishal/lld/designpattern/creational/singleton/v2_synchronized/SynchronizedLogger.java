package com.vishal.lld.designpattern.creational.singleton.v2_synchronized;

public class SynchronizedLogger {
    private static SynchronizedLogger instance;

    private SynchronizedLogger() {
    }

    // static and synchronized method -> guarantees No RACE CONDITIONS
    public static synchronized SynchronizedLogger getInstance() {
        // synchronized on method can can be performance bottleneck
        // the getInstance method will cause waiting for other threads
        if (instance == null) {
            instance = new SynchronizedLogger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[SynchronizedLogger Log] " + message);
    }
}
