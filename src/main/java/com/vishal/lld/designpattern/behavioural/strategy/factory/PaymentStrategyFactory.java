package com.vishal.lld.designpattern.behavioural.strategy.factory;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.behavioural.strategy.interfaces.PaymentStrategy;

public class PaymentStrategyFactory {
    /*
     * reusing strategy instances from map is safe only when strategies are
     * stateless
     * if pay() needs request specific data — create fresh strategy per request
     * instead
     */
    private static Map<String, PaymentStrategy> payMap = new HashMap<>();

    public static void register(String type, PaymentStrategy paymentStrategy) {
        payMap.put(type, paymentStrategy);
    }

    public static PaymentStrategy getStrategy(String type) {
        if (payMap.containsKey(type))
            return payMap.get(type);
        throw new IllegalArgumentException("Undefined type: " + type);
    }

}
