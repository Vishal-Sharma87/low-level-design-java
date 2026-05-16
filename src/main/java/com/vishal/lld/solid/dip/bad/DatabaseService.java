package com.vishal.lld.solid.dip.bad;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    /*
     * Here the DatabaseService is directly coupled with low level implementataion
     * 
     * This breaks DEPENDENCY INVERSION PRINCIPLE
     * high level implementataion should not depend on low level implementataion
     * 
     * 
     * It could be hard to change the tightly coupled dependency
     * like from MySQL to any other database
     */

    // "List" to demonstrate Database
    private List<Object> database = new ArrayList<>();

    // methods demonstrating real database features
    public void saveIntoDatabase(Object order) {
        database.add(order);
    }
}
