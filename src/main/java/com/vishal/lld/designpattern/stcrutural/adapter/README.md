# Adapter Pattern

## Problem It Solves

Two systems need to work together but have incompatible interfaces. You cannot modify either side — one is your existing codebase, the other is a third party library.

```java
// your existing interface — entire codebase depends on this
public interface Logger {
    void log(String message);
}

// third party library — cannot modify, completely unaware of your Logger interface
public class ModernLogger {
    public void writeLog(String level, String message) { }  // different name, different params
}
```

**Without Adapter — 500 call sites need to change:**

```java
// before — what your entire codebase looks like
legacyLogger.log(message);

// after — what every single call would need to become
modernLogger.writeLog("INFO", message);

// imagine 500 such calls across the codebase — high risk, high effort, easy to miss one
```

---

## Core Idea

> Wrap the incompatible interface inside a class that translates calls from what your code expects to what the third party provides.  
> Your code stays untouched. Third party stays untouched. Adapter sits in between.

---

## The Three Roles

```
Target   → your interface (Logger)           — what your code expects
Adaptee  → third party class (ModernLogger)  — what actually does the work, incompatible interface
Adapter  → the translator (ModernLoggerAdapter) — only new thing you write
```

---

## Real World Analogies

**Power socket adapter** — your laptop plug did not change, foreign socket did not change, adapter makes them work together.

**Sendgrid SDK** — you call `sendEmail(to, subject, body)`, SDK translates it to Sendgrid's complex HTTP API request. The SDK itself is an Adapter.

**JDBC** — your code calls standard `DriverManager.getConnection()`, JDBC translates to MySQL or PostgreSQL specific calls underneath.

---

## The Implementation

**Your interface — Target:**

```java
public interface Logger {
    void log(String message);
}
```

**Your existing implementation — works as before:**

```java
public class LegacyLogger implements Logger {
    public void log(String message) {
        System.out.println("[Legacy Log] " + message);
    }
}
```

**Third party library — Adaptee:**

```java
/*
 * THIRD PARTY LOGGER SYSTEM SIMULATION
 * Cannot modify this — not your code
 */
public class ModernLogger {
    public void writeLog(String level, String message) {
        System.out.printf("%s %s %s\n", "[Modern Log]", level, message);
    }
}
```

**Adapter — the translator:**

```java
// implements YOUR interface — so your codebase sees it as a Logger
// wraps THEIR class — translates log() to writeLog() internally
public class ModernLoggerAdapter implements Logger {

    private ModernLogger modernLogger;  // third party — the adaptee
    private String level;               // level decided at construction, not per call

    public ModernLoggerAdapter(ModernLogger modernLogger, String level) {
        this.modernLogger = modernLogger;
        this.level = level;
    }

    // translation happens here — log() becomes writeLog()
    public void log(String message) {
        modernLogger.writeLog(level, message);
    }
}
```

**Usage — your codebase unchanged:**

```java
String message = "Your message here";

// existing code — unchanged
Logger legacyLogger = new LegacyLogger();
legacyLogger.log(message);

// switching to third party — only change is object creation
// every logger.log() call across 500 files stays untouched
Logger infoLogger  = new ModernLoggerAdapter(new ModernLogger(), "INFO");
Logger warnLogger  = new ModernLoggerAdapter(new ModernLogger(), "WARN");
Logger errorLogger = new ModernLoggerAdapter(new ModernLogger(), "ERROR");

infoLogger.log(message);
warnLogger.log(message);
errorLogger.log(message);
```

---

## What the Adapter Translates

```
Your code calls   → log(message)
Adapter receives  → log(message)
Adapter calls     → writeLog(level, message)
ModernLogger does → the actual work
```

Two incompatibilities resolved by Adapter:

- Method name: `log()` → `writeLog()`
- Parameters: `(message)` → `(level, message)`

---

## Key Design Decisions

**Why does ModernLogger not implement Logger interface?**
Third party code has no idea your interface exists. They designed their API independently. That is exactly why Adapter is needed — to bridge two independently designed systems.

**Why is level set at construction and not per call?**
Your `Logger` interface only accepts `message` — there is no way to pass level per call without changing the interface. Setting level at construction is the clean solution — different adapter instances for different levels.

**Why not just change the Logger interface to add level?**
That would break every existing implementation and every call site across the codebase. Adapter exists precisely to avoid that.

---

## Summary

```
Problem  → incompatible interfaces, cannot modify either side
Solution → Adapter implements your interface, wraps third party internally

Target   → your interface        → Logger
Adaptee  → third party class     → ModernLogger
Adapter  → translator in between → ModernLoggerAdapter
```

---

## Where Adapter Appears in LLD Case Studies

| Case Study          | Adapter Used For                                                |
| ------------------- | --------------------------------------------------------------- |
| Logging system      | Wrapping third party loggers (Log4J, SLF4J)                     |
| Payment gateway     | Wrapping Stripe, Razorpay under common PaymentGateway interface |
| Notification system | Wrapping Sendgrid, Twilio under common Notification interface   |
| Database layer      | JDBC wrapping MySQL, PostgreSQL under common interface          |

---

## Interview Version to Write

Show the incompatible interfaces first — method name mismatch, parameter mismatch.  
Explain that modifying either side is not an option.  
Write the three roles clearly — Target, Adaptee, Adapter.  
Show that after the Adapter, your existing codebase calls `logger.log()` exactly as before — zero changes.
