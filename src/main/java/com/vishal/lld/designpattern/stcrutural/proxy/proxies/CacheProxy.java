package com.vishal.lld.designpattern.stcrutural.proxy.proxies;

import java.util.HashMap;
import java.util.Map;

import com.vishal.lld.designpattern.stcrutural.proxy.interfaces.URLScanner;

public class CacheProxy implements URLScanner {

    private Map<String, String> cache;
    private URLScanner originalScanner;

    public CacheProxy(URLScanner originalScanner) {
        if (originalScanner == null) {
            throw new IllegalArgumentException("URLScanner cannot be null");
        }
        this.originalScanner = originalScanner;

        this.cache = new HashMap<>();
    }

    @Override
    public String scan(String URL) {
        if (cache.containsKey(URL)) {
            System.out.println("[Cache Hit] URL: " + URL);
            return cache.get(URL);
        }
        System.out.println("[Cache Miss] URL: " + URL);

        String scannedResult = originalScanner.scan(URL);
        
        cache.put(URL, scannedResult);

        return scannedResult;
    }

}
