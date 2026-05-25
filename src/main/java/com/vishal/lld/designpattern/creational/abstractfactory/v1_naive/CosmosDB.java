package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory.DatabaseFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Database;

public class CosmosDB implements Database {

    @Override
    public void save() {
        System.out.println("[AZURE CosmosDB] saving...");
    }

    @Override
    public void register() {
        DatabaseFactory.register("azure", this);
    }

}
