package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.factory.CloudProviderFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.CloudProvider;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Database;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Queue;
import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Storage;

public class AWS implements CloudProvider {

    @Override
    public Queue getQueue() {
        return new SQSQueue();
    }

    @Override
    public Database getDatabase() {
        return new DynamoDb();
    }

    @Override
    public Storage getStorage() {
        return new S3Storage();
    }

    @Override
    public void register() {
        CloudProviderFactory.register("aws", this);
    }

}
