package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.Storage;

public class S3Storage implements Storage {

    @Override
    public void store() {
        System.out.println("[AWS S3] storing...");
    }

}
