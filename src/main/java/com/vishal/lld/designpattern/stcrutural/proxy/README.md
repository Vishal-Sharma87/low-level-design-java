# Proxy Pattern

## Problem It Solves

The real object is expensive, limited, or needs controlled access. Calling it directly on every request causes performance issues, quota exhaustion, or security risks.

**Real world example — VirusTotal URL Scanner:**

- First scan of a URL takes up to 20 seconds
- Free tier limited to 4 requests per minute, 500 per day
- Scanning same URL twice wastes quota and time

Without Proxy — every call hits the real scanner:

```java
URLScanner scanner = new VirusTotalScanner();
scanner.scan("google.com");   // 20s — hits VirusTotal
scanner.scan("google.com");   // 20s — hits VirusTotal again, wastes quota
scanner.scan("google.com");   // 20s — same result, quota wasted again
```

---

## Core Idea

> A Proxy sits in front of the real object and controls access to it.  
> It has the same interface as the real object — caller does not know it is talking to a Proxy.  
> Proxy decides whether to forward the request or handle it itself.

---

## Proxy vs Decorator

Both wrap an object with the same interface — but intent is different:

```
Decorator  → always calls the wrapped object
             adds behavior before or after — never blocks
             chain always completes
             caller assembles the chain — knows what behaviors are added

Proxy      → may or may not call the real object
             cache hit → returns immediately, real object never called
             rate limit exceeded → throws exception, real object never called
             controls ACCESS to the real object
             caller is unaware of proxy — thinks it is talking to real object
```

```
Decorator  → adding behavior
Proxy      → controlling access
```

---

## Three Common Proxy Types

```
Cache Proxy       → return stored result if available, avoid expensive operation
Rate Limit Proxy  → block requests beyond a threshold
Protection Proxy  → check permissions before allowing access to real object
```

---

## The Implementation

**Interface — same contract for real object and proxies:**

```java
public interface URLScanner {
    String scan(String url);
}
```

**Real service — expensive, limited:**

```java
public class VirusTotalScanner implements URLScanner {

    @Override
    public String scan(String url) {
        System.out.printf("%s scanning %s\n", "[Virus Total]", url);
        if (url.contains("Malicious")) {
            System.out.printf("%s result for %s is %s\n", "[Virus Total]", url, "UNSAFE");
            return "UNSAFE";
        }
        System.out.printf("%s result for %s is %s\n", "[Virus Total]", url, "SAFE");
        return "SAFE";
    }
}
```

**Cache Proxy — avoids hitting real scanner for repeated URLs:**

```java
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
    public String scan(String url) {
        if (cache.containsKey(url)) {
            System.out.println("[Cache Hit] URL: " + url);
            return cache.get(url);   // real scanner never called — returns immediately
        }
        System.out.println("[Cache Miss] URL: " + url);
        String result = originalScanner.scan(url);
        cache.put(url, result);      // store for next time
        return result;
    }
}
```

**Rate Limit Proxy — enforces usage limits:**

```java
public class RateLimitProxy implements URLScanner {

    /*
     * remainingLimit should ideally be per user and stored in cache/db
     * using as instance variable here to demonstrate rate limiting concept
     */
    private Integer remainingLimit;
    private URLScanner originalScanner;

    public RateLimitProxy(URLScanner originalScanner) {
        if (originalScanner == null) {
            throw new IllegalArgumentException("URLScanner cannot be null");
        }
        this.originalScanner = originalScanner;
        this.remainingLimit = 4;
    }

    @Override
    public String scan(String url) {
        if (remainingLimit == 0) {
            System.out.println("[Rate Limit] Exceeded");
            throw new RuntimeException("Rate limit exceeded, try again later");
        }
        remainingLimit--;
        return originalScanner.scan(url);
    }
}
```

---

## Stacking Proxies

Both proxies implement `URLScanner` — they can be stacked like layers:

```java
URLScanner urlScanner = new RateLimitProxy(new CacheProxy(new VirusTotalScanner()));
```

Request flow:

```
caller.scan(url)
→ RateLimitProxy  — limit exceeded? throw. else decrement and forward
→ CacheProxy      — cache hit? return immediately. else forward
→ VirusTotalScanner — actual scan, result returned up the chain
```

---

## Usage — Full Demonstration

```java
List<String> urls = new ArrayList<>(List.of(
        "google.com",                                    // limit 4→3 + cache miss + SAFE
        "Malicious.hack",                                // limit 3→2 + cache miss + UNSAFE
        "https://www.linkedin.com/in/vishal-sharma87/",  // limit 2→1 + cache miss + SAFE
        "google.com",                                    // limit 1→0 + cache hit  + SAFE
        "willExhaustLimit.com"));                        // limit 0   + rate limit exceeded

URLScanner urlScanner = new RateLimitProxy(new CacheProxy(new VirusTotalScanner()));

urls.forEach(url -> {
    try {
        urlScanner.scan(url);
    } catch (RuntimeException e) {
        System.out.println("[Main] " + e.getMessage()); // handle gracefully, continue loop
    }
});
```

**Expected output flow:**

```
[Cache Miss] google.com         → [VirusTotal] scans → SAFE
[Cache Miss] Malicious.hack     → [VirusTotal] scans → UNSAFE
[Cache Miss] linkedin.com       → [VirusTotal] scans → SAFE
[Cache Hit]  google.com         → returns cached SAFE, VirusTotal never called
[Rate Limit] Exceeded           → exception thrown, caught in main
```

---

## Key Design Decisions

**Why does Proxy implement the same interface as the real object?**
Caller should not know or care whether it is talking to a Proxy or the real object. Same interface guarantees transparent substitution — zero changes in caller code.

**Why stack RateLimitProxy outside CacheProxy?**
Rate limit should be checked before hitting cache — a cache hit should still count against the limit in some systems. Alternatively, cache hits can be exempt from rate limiting — then CacheProxy goes outside. Order depends on business requirements.

**Why is `remainingLimit` an instance variable and not from a database?**
For demonstration purposes only. In production, rate limit state must be stored per user in a shared cache like Redis — otherwise each server instance has its own counter and the limit is not enforced across servers.

**Why catch `RuntimeException` in main instead of letting it propagate?**
`forEach` stops on first uncaught exception. Catching it allows the loop to continue processing remaining URLs — important when one failure should not block others.

---

## Where Proxy Appears in LLD Case Studies

| Case Study      | Proxy Used For                                                    |
| --------------- | ----------------------------------------------------------------- |
| URL scanner     | Cache + Rate limit before hitting VirusTotal                      |
| Image loader    | Load thumbnail first, full image only when needed                 |
| Database access | Connection pool proxy — reuse connections instead of creating new |
| API gateway     | Auth check, rate limiting, logging before routing to real service |

---

## Interview Version to Write

Start with the expensive real object problem — repeated calls, quota exhaustion.  
Show Cache Proxy first — same interface, returns cached result, real object skipped.  
Add Rate Limit Proxy — throws before even reaching cache.  
Stack both — show the request flow through each layer.  
Distinguish from Decorator — Proxy controls access, Decorator adds behavior.
