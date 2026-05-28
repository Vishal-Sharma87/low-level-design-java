package com.vishal.lld.designpattern.behavioural.strategy;

import com.vishal.lld.designpattern.behavioural.strategy.factory.PaymentStrategyFactory;
import com.vishal.lld.designpattern.behavioural.strategy.interfaces.PaymentStrategy;

public class OrderService {

    public void placeOrder(String type) {

        System.out.println("Placing order with payment type: " + type);
        
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(type);

        PaymentService paymentService = new PaymentService(strategy);

        paymentService.pay();
    }
}
