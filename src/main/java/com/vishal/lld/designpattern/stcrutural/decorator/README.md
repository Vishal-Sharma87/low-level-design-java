# Decorator Pattern

## Problem It Solves

Adding extra behaviors to an existing class without modifying it — and supporting any combination of those behaviors without creating a separate class for each.

**Naive approach — class per combination:**

```
EmailNotification
EmailWithLogging
EmailWithEncryption
EmailWithLoggingAndEncryption
EmailWithLoggingAndRetry
EmailWithEncryptionAndRetry
EmailWithLoggingAndEncryptionAndRetry
// ... keeps exploding
```

**What is wrong:**

- Every new feature doubles the number of classes — called "Class Explosion"
- Not every combination is equally used — some classes created just to satisfy all possibilities
- Adding a new feature requires creating classes for every existing combination
- Violates OCP — existing classes need to be modified or extended for every new requirement

---

## Core Idea

> Instead of pre-building every combination — wrap behaviors like layers around the base object at runtime.  
> Each layer adds its own behavior and delegates the rest to the layer underneath.  
> Any combination is possible with N decorator classes only — no class explosion.

---

## Mental Model — Middleware

Think of it like middleware in a web framework:

```
Request → LoggingMiddleware → AuthMiddleware → RateLimitMiddleware → actual handler
```

Each middleware wraps the next. Each adds its own behavior. The actual handler has no idea how many middlewares wrap it. That is Decorator.

---

## Decorator vs Builder

Both let the caller assemble what they want step by step — but they solve different problems:

```
Builder     → assembles an object's DATA
              .size("Large").extraCheese(true).build()
              result is a fully constructed object

Decorator   → assembles an object's BEHAVIOR
              new LoggingDecorator(new EncryptionDecorator(base))
              result is an object that behaves differently
```

---

## The Structure

```
Notification                ← interface — the contract
NotificationDecorator       ← abstract base decorator
                              implements Notification
                              holds wrapped reference
                              handles null check
                              delegates send() to wrapped
LoggingDecorator            ← extends NotificationDecorator, adds logging
EncryptionDecorator         ← extends NotificationDecorator, adds encryption
EmailNotification           ← implements Notification directly
                              the termination point — actual work happens here
```

---

## The Implementation

**Interface — the contract:**

```java
public interface Notification {
    void send(String message);
}
```

**Base implementation — termination point:**

```java
// actual work happens here — no wrapping, no delegation
public class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("[Email] " + message);
    }
}
```

**Abstract base decorator — handles null check, holds wrapped reference:**

```java
public abstract class NotificationDecorator implements Notification {

    protected Notification wrapped;  // protected — subclasses can access if needed

    public NotificationDecorator(Notification wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Wrapped cannot be null");
        }
        this.wrapped = wrapped;
    }

    public void send(String message) {
        wrapped.send(message);  // default — delegate to wrapped
    }
}
```

**LoggingDecorator — adds logging behavior:**

```java
public class LoggingDecorator extends NotificationDecorator {

    public LoggingDecorator(Notification wrapped) {
        super(wrapped);  // null check handled in parent
    }

    @Override
    public void send(String message) {
        System.out.println("[LOG] Sending message: " + message);
        super.send(message);  // delegate to wrapped layer
        System.out.println("[LOG] Message sent: " + message);
    }
}
```

**EncryptionDecorator — adds encryption behavior:**

```java
public class EncryptionDecorator extends NotificationDecorator {

    public EncryptionDecorator(Notification wrapped) {
        super(wrapped);
    }

    @Override
    public void send(String message) {
        String encrypted = "[Encrypted] " + message;
        super.send(encrypted);  // passes encrypted message down to next layer
    }
}
```

---

## Usage and Ordering

```java
String defaultEmailMessage = "THIS IS DEFAULT MESSAGE FOR EMAIL";

/*
 * what if we want extra functionalities like:
 * 1. Log before and after sending message
 * 2. Encrypt the message before sending
 * 3. Retry sending if failed
 * or some more functionalities?
 *
 * Possible ways:
 *
 * ## Create classes per feature combination
 * Pros: easy to create, self explanatory
 * Cons: every feature addition needs separate class
 *       not every class is equally used — some very occasional
 *       causes "Class Explosion"
 *
 * ## Decorator Pattern
 * Binds a contract with the base service
 * Base service → send the email
 * Features → Logging, Encryption or any combination
 * Contract → Base + Decorator specific feature
 */

// logging wraps encryption wraps email
Notification loggingAndEncryption = new LoggingDecorator(
                                        new EncryptionDecorator(
                                            new EmailNotification()));
loggingAndEncryption.send(defaultEmailMessage);

// here ordering matters
// 1 -> 2 -> 3 != 1 -> 3 -> 2 || 2 -> 1 -> 3
// also the last "Notification" object passed must be "TERMINATOR"

// encryption wraps logging wraps email — different order, different behavior
Notification encryptionAndLogging = new EncryptionDecorator(
                                        new LoggingDecorator(
                                            new EmailNotification()));
encryptionAndLogging.send(defaultEmailMessage);
```

**Execution flow for `loggingAndEncryption.send()`:**

```
LoggingDecorator.send()
→ prints "[LOG] Sending message"
→ calls wrapped.send() — EncryptionDecorator
   → encrypts message
   → calls wrapped.send() — EmailNotification  ← TERMINATOR
      → prints "[Email] [Encrypted] message"
   ← back to EncryptionDecorator
← back to LoggingDecorator
→ prints "[LOG] Message sent"
```

---

## Ordering Matters

```
LoggingDecorator(EncryptionDecorator(Email))
→ log → encrypt → send     — message is encrypted, logging sees plain message

EncryptionDecorator(LoggingDecorator(Email))
→ encrypt → log → send     — logging itself gets encrypted — probably not what you want
```

Outermost decorator runs first. Innermost runs last. Base always terminates.

---

## Handling Combinations

**Fixed combinations — create at startup, store in map:**

```java
Notification basicEmail       = new EmailNotification();
Notification withLogging      = new LoggingDecorator(new EmailNotification());
Notification withBoth         = new LoggingDecorator(
                                    new EncryptionDecorator(
                                        new EmailNotification()));

registry.put("basic", basicEmail);
registry.put("with_logging", withLogging);
registry.put("with_both", withBoth);
```

**Dynamic combinations — assemble at runtime based on flags:**

```java
public Notification buildNotification(boolean logging, boolean encryption) {
    Notification n = new EmailNotification();
    if (encryption) n = new EncryptionDecorator(n);
    if (logging)    n = new LoggingDecorator(n);
    return n;
}
```

---

## Key Design Decisions

**Why abstract base decorator `NotificationDecorator`?**
Null check written once — every decorator inherits it. Without it, every decorator must repeat the null check. Also enforces that every decorator holds a `wrapped` reference.

**Why `protected` on `wrapped` field?**
`private` would block subclasses from accessing `wrapped` directly if ever needed. `protected` keeps it accessible within the decorator hierarchy while still hidden from outside.

**Why can you not unwrap a decorator?**
Once wrapped the object is immutable — behaviors are baked in. If you need a different combination, assemble a new one. This is intentional — immutability keeps behavior predictable.

**Why must the innermost object be a concrete base implementation?**
Decorators delegate `send()` to their `wrapped`. The chain must terminate at something that actually does the work — `EmailNotification` in this case. Without a terminator the chain has nothing to delegate to.

---

## Where Decorator Appears in LLD Case Studies

| Case Study           | Decorator Used For                                                            |
| -------------------- | ----------------------------------------------------------------------------- |
| Notification system  | Adding logging, encryption, retry to any notifier                             |
| Coffee shop ordering | Adding milk, sugar, whip to base coffee                                       |
| I/O streams in Java  | `BufferedInputStream(new FileInputStream())` — Java uses Decorator internally |
| HTTP middleware      | Adding auth, logging, rate limiting to request handler                        |

---

## Interview Version to Write

Show class explosion problem first — N features = 2^N classes.  
Introduce Decorator — N features = N decorator classes, infinite combinations.  
Write the four components — interface, base implementation, abstract decorator, concrete decorators.  
Demonstrate ordering with two combinations and explain why order matters.  
Mention Java's own `InputStream` hierarchy as a real world example of Decorator.
