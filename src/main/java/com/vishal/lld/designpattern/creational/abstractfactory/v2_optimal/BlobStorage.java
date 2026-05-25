package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Storage;

public class BlobStorage implements Storage {

    @Override
    public void store() {
        System.out.println("[Azure BlobStorage] storing...");
    }

}
