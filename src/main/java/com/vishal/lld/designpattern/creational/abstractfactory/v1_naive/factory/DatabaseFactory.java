package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Database;

public class DatabaseFactory {
    private static Map<String, Database> databaseMap = new HashMap<>();

    public static void register(String provider, Database database) {
        databaseMap.put(provider, database);
    }

    public static Database getDatabase(String provider) {
        if (databaseMap.containsKey(provider))
            return databaseMap.get(provider);
        throw new IllegalArgumentException("undefined provider: " + provider);
    }
}
