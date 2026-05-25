package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces;

public interface CloudProvider {

    Queue getQueue();
    Database getDatabase();
    Storage getStorage();

    void register();
    
}
