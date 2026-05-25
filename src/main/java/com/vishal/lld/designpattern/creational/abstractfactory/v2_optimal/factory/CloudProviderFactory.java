package com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.factory;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.creational.abstractfactory.v2_optimal.interfaces.CloudProvider;

public class CloudProviderFactory {

    private static Map<String, CloudProvider> cloudProviderMap = new HashMap<>();

    public static void register(String provider, CloudProvider cloudProvider) {
        cloudProviderMap.put(provider, cloudProvider);
    }

    public static CloudProvider getCloudProvider(String provider) {
        if (cloudProviderMap.containsKey(provider)) {
            return cloudProviderMap.get(provider);
        }

        throw new IllegalArgumentException("Provider undefined: " + provider);
    }

}
