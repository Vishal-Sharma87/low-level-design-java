# Singleton Pattern

## Problem It Solves

Some things in a system should exist **exactly once**.  
If you accidentally create two instances, you get bugs — inconsistent state, double logging, two separate DB connection pools, etc.

Examples where only one instance should exist:

- Logger
- Database connection pool
- Configuration manager
- Cache manager

---

## Core Idea

> Restrict instantiation of a class to one object, and provide a global point of access to it.

---

## Progressive Evolution

### v1 — Naive (basic getInstance, no thread safety)

```java
public class NaiveLogger {

    private static NaiveLogger instance = null;

    private NaiveLogger() {}

    public static NaiveLogger getInstance() {
        if (instance == null) {
            instance = new NaiveLogger(); // ❌ race condition possible
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

**What it does:** Private constructor blocks direct `new NaiveLogger()`. `getInstance()` creates the object only when first needed (lazy initialization).

**What is wrong:** Two threads can both pass `instance == null` check at the same time and each create a new instance. Singleton guarantee is broken.

**Simulating the problem:**

```java
Runnable task = () -> {
    NaiveLogger logger = NaiveLogger.getInstance();
    System.out.println(Thread.currentThread().getName() + " got: " + logger.hashCode());
};

Thread t1 = new Thread(task);
Thread t2 = new Thread(task);
t1.start();
t2.start();
```

Run this multiple times. Occasionally you will see two different `hashCode` values — two instances were created.

---

### v2 — Synchronized Method (thread safe but slow)

```java
public class SynchronizedLogger {

    private static SynchronizedLogger instance = null;

    private SynchronizedLogger() {}

    public static synchronized SynchronizedLogger getInstance() {
        if (instance == null) {
            instance = new SynchronizedLogger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

**What it fixes:** `synchronized` ensures only one thread executes `getInstance()` at a time. Race condition is gone.

**What is wrong:** Lock is acquired on **every single call** — even after the instance is already created. Thousands of calls means thousands of unnecessary locks. Performance bottleneck.

---

### v3 — Double Checked (no bottleneck, rare risk)

```java
public class SynchronizedIfNullLogger {

    private static SynchronizedIfNullLogger instance = null;

    private SynchronizedIfNullLogger() {}

    public static SynchronizedIfNullLogger getInstance() {
        if (instance == null) {                             // first check — no lock
            synchronized (SynchronizedIfNullLogger.class) { // lock only if needed
                if (instance == null) {                     // second check — inside lock
                    instance = new SynchronizedIfNullLogger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

**What it fixes:** Lock is only acquired when `instance` is null — only on first creation. Every call after skips the lock entirely.

**What is wrong:** JVM can reorder the three internal steps of object creation:

1. Allocate memory
2. Call constructor
3. Assign to `instance`

JVM can do step 3 before step 2. Another thread can then get a half-constructed object. Rare but possible.

---

### v4 — Optimal (double checked + volatile)

```java
public class OptimalLogger {

    private static volatile OptimalLogger instance = null; // volatile prevents reordering

    private OptimalLogger() {}

    public static OptimalLogger getInstance() {
        if (instance == null) {
            synchronized (OptimalLogger.class) {
                if (instance == null) {
                    instance = new OptimalLogger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

**What it fixes:** `volatile` tells the JVM — do not reorder instructions around this variable. Steps 1, 2, 3 of object creation must happen in order before `instance` is visible to other threads. Half-constructed object risk is eliminated.

---

## Full Evolution Summary

| Version           | Lazy | Thread Safe | Performant | Issue                    |
| ----------------- | ---- | ----------- | ---------- | ------------------------ |
| v1 Naive          | ✅   | ❌          | ✅         | Race condition           |
| v2 Synchronized   | ✅   | ✅          | ❌         | Lock on every call       |
| v3 Double Checked | ✅   | ⚠️          | ✅         | Half-baked instance risk |
| v4 Optimal        | ✅   | ✅          | ✅         | None                     |

---

## Main Method — Putting It All Together

```java
public static void main(String[] args) throws InterruptedException {

    // v1 — naive logger
    // might create multiple instances on concurrent calls
    NaiveLogger naiveLogger1 = NaiveLogger.getInstance();
    NaiveLogger naiveLogger2 = NaiveLogger.getInstance();
    System.out.println(naiveLogger1 == naiveLogger2); // true in single thread
    naiveLogger1.log("Message 1 by: " + naiveLogger1.toString());
    naiveLogger2.log("Message 2 by: " + naiveLogger2.toString());

    // simulating the v1 problem — run multiple times to occasionally see two hashCodes
    Runnable task = () -> {
        NaiveLogger logger = NaiveLogger.getInstance();
        System.out.println(Thread.currentThread().getName() + " got: " + logger.hashCode());
    };
    Thread t1 = new Thread(task);
    Thread t2 = new Thread(task);
    t1.start();
    t2.start();
    t1.join();
    t2.join();

    // v2 — synchronized logger
    // guarantees single instance but slow due to lock on every call
    SynchronizedLogger synchronizedLogger1 = SynchronizedLogger.getInstance();
    SynchronizedLogger synchronizedLogger2 = SynchronizedLogger.getInstance();
    System.out.println(synchronizedLogger1 == synchronizedLogger2); // true
    synchronizedLogger1.log("Message 1 by: " + synchronizedLogger1.toString());
    synchronizedLogger2.log("Message 2 by: " + synchronizedLogger2.toString());

    // v3 — double checked logger
    // no performance bottleneck but rare half-baked instance possibility
    SynchronizedIfNullLogger logger1 = SynchronizedIfNullLogger.getInstance();
    SynchronizedIfNullLogger logger2 = SynchronizedIfNullLogger.getInstance();
    System.out.println(logger1 == logger2); // true
    logger1.log("Message 1 by: " + logger1.toString());
    logger2.log("Message 2 by: " + logger2.toString());

    // v4 — optimal logger
    // guarantees no half-baked instance, thread safe, performant
    OptimalLogger optimalLogger1 = OptimalLogger.getInstance();
    OptimalLogger optimalLogger2 = OptimalLogger.getInstance();
    System.out.println(optimalLogger1 == optimalLogger2); // true
    optimalLogger1.log("Message 1 by: " + optimalLogger1.toString());
    optimalLogger2.log("Message 2 by: " + optimalLogger2.toString());
}
```

---

## Where Singleton Appears in LLD Case Studies

| Case Study    | Singleton Used For  |
| ------------- | ------------------- |
| Parking lot   | `ParkingLotManager` |
| ATM system    | `ATMController`     |
| Logger system | `Logger` itself     |
| LRU Cache     | `CacheManager`      |
| Ride sharing  | `TripDispatcher`    |

---

## Interview Version to Write

Always write **v4 — optimal**. If the interviewer asks why `volatile`, explain the JVM instruction reordering risk.
