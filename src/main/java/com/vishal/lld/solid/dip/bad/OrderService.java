package com.vishal.lld.solid.dip.bad;


public class OrderService {

    private DatabaseService databaseService = new DatabaseService();

    public void placeOrder(Object order) {
        databaseService.saveIntoDatabase(order);
        System.out.println("Order placed ");
    }
}
