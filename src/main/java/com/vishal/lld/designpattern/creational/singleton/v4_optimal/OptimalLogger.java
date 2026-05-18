package com.vishal.lld.designpattern.creational.singleton.v4_optimal;

public class OptimalLogger {

    // volatile -> forces JVM to always do the step as they are
    // also global visbility directly to the field not any cached field
    private static volatile OptimalLogger instance;

    private OptimalLogger(){}

    public static OptimalLogger getInstance(){
        if(instance == null){
            synchronized(OptimalLogger.class){
                instance = new OptimalLogger(); 
            }
        }

        return instance;
    }

    public void log(String message){
        System.out.println("[Optimal Logger] " + message);
    }
}
