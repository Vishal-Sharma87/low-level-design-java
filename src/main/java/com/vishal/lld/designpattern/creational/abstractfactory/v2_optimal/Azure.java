package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.factory.CloudProviderFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.CloudProvider;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Database;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Queue;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Storage;

public class Azure implements CloudProvider {

    @Override
    public Queue getQueue() {
        return new ServiceBus();
    }

    @Override
    public Database getDatabase() {
        return new CosmosDb();
    }

    @Override
    public Storage getStorage() {
        return new BlobStorage();
    }

    @Override
    public void register() {
        CloudProviderFactory.register("azure", this);
    }

}
