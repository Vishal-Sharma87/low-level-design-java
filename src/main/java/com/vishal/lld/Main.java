package com.vishal.lld;

import com.vishal.lld.solid.dip.good.DatabaseService;
import com.vishal.lld.solid.dip.good.OrderService;
import com.vishal.lld.solid.dip.good.interfaces.MySQLDatabase;

public class Main {
    public static void main(String[] args) {
        // Creating DatabaseService at the start of program by passing the "database" ->
        // can be MySQL, PostgreSQL or any other
        DatabaseService databaseService = new DatabaseService(new MySQLDatabase());

        // creating "OrderService" by passing the above created "DatabaseService"
        OrderService orderService = new OrderService(databaseService);

        // Placing one order
        int orderId = orderService.placeOrder("Name:vishal");

        // checking the order associated with above orderId
        System.out.printf("Order with orderId %d is %s", orderId, String.valueOf(orderService.findById(orderId)));
    }
}