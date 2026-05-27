package com.vishal.lld.designpattern.stcrutural.proxy;

import com.vishal.lld.designpattern.stcrutural.proxy.interfaces.URLScanner;

public class VIrusTotalScanner implements URLScanner {

    @Override
    public String scan(String URL) {
        System.out.printf("%s scanning %s\n", "[Virus Total]", URL);

        if (URL.contains("Malicious")) {
            System.out.printf("%s result for %s is %s\n", "[Virus Total]", URL, "UNSAFE");
            return "UNSAFE";
        }
        System.out.printf("%s result for %s is %s\n", "[Virus Total]", URL, "SAFE");
        return "SAFE";
    }

}
