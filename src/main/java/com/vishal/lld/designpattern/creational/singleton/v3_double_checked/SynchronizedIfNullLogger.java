package com.vishal.lld.designpattern.creational.singleton.v3_double_checked;

public class SynchronizedIfNullLogger {
    private static SynchronizedIfNullLogger instance;

    private SynchronizedIfNullLogger() {
    }

    /*
     * The rare but possible Limitation remaining is that JVM can alter the steps of
     * creating instance for optimization, like object creation steps are
     * 
     * Allocate memory
     * assign that memory to variable
     * call the constructor
     * 
     * Two threads arrived
     * first one sees null -> acquires lock
     * JVM performed step 1 and step 2 for first thread,
     * NOW "instance" is no longer null but yet not fully baked
     * 
     * second thread sees instance != null
     * returns half baked instance
     * 
     * SOLUTION -> optimal logger 
     */
    public static SynchronizedIfNullLogger getInstance() {
        if (instance == null) {
            synchronized (SynchronizedIfNullLogger.class) {
                // avoids botteleneck of returning Instance sequentially
                // acquire "lock" only if Instance field is null
                instance = new SynchronizedIfNullLogger();
            }
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[SynchronizedIfNullLogger Log] " + message);
    }
}
