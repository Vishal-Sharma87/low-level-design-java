package com.vishal.lld.solid.dip.good;

public class OrderService {

    // Depends on the database service not the actual "database"
    private DatabaseService databaseService;

    public OrderService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public int placeOrder(Object order) {
        return databaseService.save(order);
    }

    public Object findById(int id) {
        return databaseService.findById(id);
    }
}
