package com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.factory;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.creational.abstractfactory.v1_naive.interfaces.Queue;

public class QueueFactory {
    private static Map<String, Queue> queueMap = new HashMap<>();

    public static void register(String provider, Queue queue) {
        queueMap.put(provider, queue);
    }

    public static Queue getQueue(String provider) {
        if (queueMap.containsKey(provider))
            return queueMap.get(provider);

        throw new IllegalArgumentException("undefined provider: " + provider);
    }
}
