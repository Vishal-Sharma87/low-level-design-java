package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory.StorageFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Storage;

public class BlobStorage implements Storage {

    @Override
    public void store() {
        System.out.println("[AZURE BlobStorage] storing...");
    }

    @Override
    public void register() {
        StorageFactory.register("azure", this);
    }

}
