package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Database;

public class CosmosDb implements Database {

    @Override
    public void save() {
        System.out.println("[Azure CosmosDb] saving...");
    }

}
