package com.vishal.lld.solid.dip.good;

import com.vishal.lld.solid.dip.good.interfaces.Database;

public class DatabaseService {

    // abstraction without knowing low level implementation
    private Database database;

    public DatabaseService(Database database) {
        this.database = database;
    }

    public int save(Object order){
        return database.save(order);
    }

    public Object findById(int id){
        return database.findById(id);
    }
}
