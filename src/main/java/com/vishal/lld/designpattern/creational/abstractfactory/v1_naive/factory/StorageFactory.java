package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Storage;

public class StorageFactory {
    private static Map<String, Storage> storageMap = new HashMap<>();

    public static void register(String provider, Storage storage) {
        storageMap.put(provider, storage);
    }

    public static Storage getStorage(String provider) {
        if (storageMap.containsKey(provider))
            return storageMap.get(provider);

        throw new IllegalArgumentException("undefined provider: " + provider);
    }
}
