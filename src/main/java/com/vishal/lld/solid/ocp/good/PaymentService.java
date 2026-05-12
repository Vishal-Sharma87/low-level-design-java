package com.vishal.lld.solid.ocp.good;

import com.vishal.lld.solid.ocp.good.factory.PaymentStrategyFactory;
import com.vishal.lld.solid.ocp.good.interfaces.PaymentStrategy;

public class PaymentService {

    /*
     * The class to process the payment irrespective of which type is given
     * factory returns the PaymentStartegy object and service calls teh "pay" method
     */

    private PaymentStrategyFactory paymentFactory = new PaymentStrategyFactory();

    public void processPayment(String type, double amount) {
        // get the PaymentStrategy object associated with tyep "type"
        PaymentStrategy strategy = paymentFactory.getPaymentStrategy(type);

        // calls "pay" method of above strategy
        strategy.pay(amount);
    }

}
