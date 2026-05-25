package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory.StorageFactory;
import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Storage;

public class S3Storage implements Storage {

    @Override
    public void store() {
        System.out.println("[AWS S3] storing...");
    }

    @Override
    public void register() {
        StorageFactory.register("aws", this);
    }

}
