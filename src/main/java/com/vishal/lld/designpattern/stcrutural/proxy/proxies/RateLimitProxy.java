package com.vishal.lld.designpattern.stcrutural.proxy.proxies;

import com.vishal.lld.designpattern.stcrutural.proxy.interfaces.URLScanner;

public class RateLimitProxy implements URLScanner {

    /*
     * The remaining limit must be per user based and should be extracted from cache
     * using as instance variable to demonstrate the rate limiting purpose
     */
    private Integer remainingLimit;

    private URLScanner originalScanner;

    public RateLimitProxy(URLScanner originalScanner) {
        if (originalScanner == null) {
            throw new IllegalArgumentException("URLScanner cannot be null");
        }
        this.originalScanner = originalScanner;
        remainingLimit = 4;
    }

    @Override
    public String scan(String URL) {
        if (remainingLimit == 0) {
            System.out.println("[Rate Limit] Exceeded");
            throw new RuntimeException("Rate limit exceeded, try again later");
        }

        remainingLimit--;
        return originalScanner.scan(URL);
    }

}
