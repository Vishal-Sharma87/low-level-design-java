package com.vishal.lld.casestudies.parkinglot.factory;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.casestudies.parkinglot.interfaces.PaymentStrategy;

public class PaymentStrategyFactory {
    private static Map<String, PaymentStrategy> strategyMap = new HashMap<>();

    static {

        try {
            Class.forName("com.vishal.lld.casestudies.parkinglot.paymentstrategies.UpiPaymentStrategy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void register(String name, PaymentStrategy strategy) {
        if (name != null && !name.isEmpty() && strategy != null) {
            strategyMap.put(name, strategy);
            return;
        }
        throw new IllegalArgumentException("Name and Strategy must not be null");
    }

    public static PaymentStrategy getPaymentStrategy(String name) {
        if (strategyMap.containsKey(name)) {
            return strategyMap.get(name);
        }
        throw new IllegalArgumentException("payment startegy undefined: " + name);
    }
}
