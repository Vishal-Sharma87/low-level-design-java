# Factory Method Pattern

## Problem It Solves

Business logic should not be responsible for deciding **which object to create**.  
Directly using `new` inside a service causes:

- Service tightly coupled to concrete classes
- Every new type requires modifying existing code — OCP violation
- In a large codebase, same `if-else` logic scattered across 50 places
- Cannot swap or mock implementations for testing

---

## Core Idea

> Move object creation out of the caller's hands into a dedicated factory.  
> The caller only works with the abstraction — it does not know or care what is created underneath.

---

## The Scenario

A notification system that supports Email, SMS, and Push notifications.  
The service's only job is to send a notification — not to decide which type to create.

---

## Progressive Evolution

### v1 — Naive (inline logic, no abstraction)

```java
/**
 * v1 - naive approach
 * all creation and sending logic inline
 * no abstraction, no factory
 * violates OCP — every new type requires modifying this method
 */
public class NaiveNotificationService {

    public void sendNotification(String type, String message) {
        if ("email".equals(type)) {
            System.out.println("[Email] sending message: " + message);
        } else if ("sms".equals(type)) {
            System.out.println("[SMS] sending message: " + message);
        } else {
            throw new IllegalArgumentException("Undefined notification type: " + type);
        }
    }
}
```

**What is wrong:**

- Service handles both creation logic and sending logic — SRP violation
- Adding new type means modifying this method — OCP violation
- No abstraction — cannot swap or mock for testing
- Same if-else would be duplicated in every class that needs to send notifications

---

### v2 — Abstraction + Simple Factory (if-else moves to factory)

**Notification interface — common abstraction:**

```java
public interface Notification {
    void send(String message);
    void register();
}
```

**Implementing classes:**

```java
public class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("[Email] sending message: " + message);
    }
}

public class SmsNotification implements Notification {
    public void send(String message) {
        System.out.println("[SMS] sending message: " + message);
    }
}
```

**Factory — if-else lives here, not in service:**

```java
/**
 * v2 - simple factory
 * if-else centralized in one place
 * service is clean but factory violates OCP on every new type
 */
public class NotificationFactory {

    public static Notification getNotification(String type) {
        if ("email".equals(type)) {
            return new EmailNotification();
        } else if ("sms".equals(type)) {
            return new SmsNotification();
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
```

**Service — clean, no creation logic:**

```java
/**
 * v2 - abstraction based service
 * delegates object creation to factory
 * only works with Notification abstraction
 */
public class AbstractNotificationService {

    public void sendNotification(String type, String message) {
        Notification notification = NotificationFactory.getNotification(type);
        notification.send(message);
    }
}
```

**What improved over v1:**

- Service has zero creation logic — clean SRP
- if-else centralized in factory — one place to change
- Service depends on abstraction, not concrete classes

**What is still wrong:**

- Factory still has if-else — adding new type means modifying factory — OCP violation inside factory

---

### v3 — Map Based Factory (if-else replaced with map)

```java
/**
 * v3 - map based factory
 * if-else replaced with map lookup
 * factory has no knowledge of concrete types
 * but main is responsible for registration — knows all types
 */
public class MapBasedNotificationFactory {

    private static Map<String, Notification> notificationMap = new HashMap<>();

    public static Notification getNotification(String type) {
        if (notificationMap.containsKey(type)) {
            return notificationMap.get(type);
        }
        throw new IllegalArgumentException("Undefined type: " + type);
    }

    public static void register(String type, Notification notification) {
        notificationMap.put(type, notification);
    }
}
```

**Main — responsible for registration:**

```java
// main knows both the type string and the concrete class
MapBasedNotificationFactory.register("email", new EmailNotification());
MapBasedNotificationFactory.register("sms", new SmsNotification());
```

**What improved over v2:**

- No if-else in factory
- Factory has zero knowledge of concrete types
- Adding new type does not touch the factory

**What is still wrong:**

- Main must know both the type string `"email"` and the class `EmailNotification`
- Forgetting to register a type causes a silent runtime bug
- Registration responsibility sits outside the type itself

---

### v4 — Self Registering Factory (each type owns its registration)

**Interface — register() added:**

```java
public interface Notification {
    void send(String message);
    void register();   // each type is responsible for registering itself
}
```

**Implementing classes own their identity and registration:**

```java
/**
 * v4 - self registering
 * EmailNotification knows its own type string "email"
 * registers itself into the factory — main has zero knowledge of internals
 */
public class OptimalEmailNotification implements Notification {

    public void register() {
        OptimalNotificationFactory.register("email", this); // owns "email" identity
    }

    public void send(String message) {
        System.out.println("[Email] sending message: " + message);
    }
}
```

**Factory — completely unaware of any concrete type:**

```java
public class OptimalNotificationFactory {

    private static Map<String, Notification> notificationMap = new HashMap<>();

    public static Notification getNotification(String type) {
        if (notificationMap.containsKey(type)) {
            return notificationMap.get(type);
        }
        throw new IllegalArgumentException("Undefined type: " + type);
    }

    public static void register(String type, Notification notification) {
        notificationMap.put(type, notification);
    }
}
```

**Main — just triggers registration, knows nothing about internals:**

```java
// main just says "register yourself"
// does not know the type string or any internal details
new OptimalEmailNotification().register();
new OptimalSmsNotification().register();

/*
 * This still looks manual but the responsibility has shifted
 * EmailNotification owns "email" — main does not
 * Frameworks like Spring eliminate even this via @Component + @PostConstruct
 */
```

**What improved over v3:**

- Each type owns its own identity and registration
- Factory has zero knowledge of concrete types
- Adding new type = new class only, nothing else changes
- Main does not know type strings or concrete classes

**Remaining limitation:**

- Manual `register()` call in main still required
- Frameworks like Spring automate this via classpath scanning + `@PostConstruct`

---

## Full Evolution Summary

| Version                     | Creation Logic               | Scalability | OCP Status     | Client Dependency            | Main Characteristic    |
| --------------------------- | ---------------------------- | ----------- | -------------- | ---------------------------- | ---------------------- |
| v1 Naive                    | `if-else` inside service     | Poor        | Violated       | Depends on concrete classes  | Tight coupling         |
| v2 Simple Factory           | `if-else` moved to factory   | Moderate    | Violated       | Depends on concrete classes  | Centralized creation   |
| v3 Map Based Factory        | Uses `Map<String, Supplier>` | Good        | Followed       | Still knows concrete classes | Easily extendable      |
| v4 Self Registering Factory | Classes register themselves  | Excellent   | Fully followed | Depends only on abstraction  | Fully decoupled design |

---

## How Spring Eliminates Manual Registration

```java
@Component
public class EmailNotification implements Notification {

    @Autowired
    private NotificationFactory factory;

    @PostConstruct
    public void register() {
        factory.register("email", this);
    }

    public void send(String message) {
        System.out.println("[Email] sending message: " + message);
    }
}
```

Spring flow at app startup:

```
scans classpath
→ finds @Component on EmailNotification
→ creates the bean
→ injects dependencies via @Autowired
→ sees @PostConstruct on register()
→ calls register() automatically
→ EmailNotification is in the factory map — zero manual wiring
```

---

## Key Design Decisions

**Why not inject notification objects as fields on the service?**  
Service would be coupled to concrete types — defeats the purpose of the factory.

**Why not pass type as parameter to register()?**  
That would make main responsible for the type string again — same problem as v3.  
Each implementing class should own its own identity.

**Why reuse objects from map instead of creating new every time?**  
Valid when objects are stateless — just behavior, no fields.  
Not valid when objects hold request-specific state — risk of data leaking between requests.

---

## Where Factory Method Appears in LLD Case Studies

| Case Study          | Factory Used For                            |
| ------------------- | ------------------------------------------- |
| Notification system | `NotificationFactory` — Email, SMS, Push    |
| Payment system      | `PaymentFactory` — UPI, Card, Netbanking    |
| Vehicle rental      | `VehicleFactory` — Car, Bike, Truck         |
| Logger system       | `LoggerFactory` — FileLogger, ConsoleLogger |

---

## Interview Version to Write

Write **v4 — self registering** as the optimal solution.  
Start by explaining v1 problem, then walk the interviewer through the evolution.  
That demonstrates thinking, not just pattern knowledge.
