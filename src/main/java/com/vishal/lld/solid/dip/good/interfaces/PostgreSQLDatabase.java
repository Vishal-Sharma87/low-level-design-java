package com.vishal.lld.solid.dip.good.interfaces;

import java.util.ArrayList;
import java.util.List;

public class PostgreSQLDatabase implements Database {

    /*
     * Here MySQLDatabase is low level implementataion, but
     * "Dataabse is high level abstraction"
     */

    // List demonstrate PostgreSQL Database
    private List<Object> db;

    public PostgreSQLDatabase() {
        this.db = new ArrayList<>();
    }

    @Override
    public int save(Object order) {
        db.add(order);
        System.out.println("Order saved in PostgreSQL database");
        return db.size() - 1;
    }

    @Override
    public Object findById(int id) {
        if (id > db.size()) {
            throw new IllegalArgumentException("Order not found in PostgreSQL database for id: " + id);
        }
        return db.get(id);
    }

}
