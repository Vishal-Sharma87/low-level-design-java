# Observer Pattern

## Problem It Solves

One event triggers multiple reactions. Without Observer, the publisher must know and call every dependent directly — tight coupling, OCP violation, hard to scale.

**Without Observer:**

```java
public class OrderService {
    public void placeOrder() {
        emailService.sendConfirmation();        // knows EmailService
        inventoryService.updateStock();         // knows InventoryService
        deliveryService.scheduleDelivery();     // knows DeliveryService
        notificationService.notifyUser();       // knows NotificationService
        // adding new reaction = modifying OrderService — OCP violation
    }
}
```

**What is wrong:**

- `OrderService` tightly coupled to every dependent service
- Adding new reaction requires modifying `OrderService`
- Hard to scale — all reactions happen synchronously in same thread
- Hard to debug — one failure in email blocks delivery scheduling

---

## Core Idea

> One object changes state — all registered dependents are automatically notified.  
> Publisher does not call dependents directly — they register themselves and react when notified.  
> Publisher has zero knowledge of what observers do.

---

## Real World Connection

Your URL shortener Kafka implementation is Observer at scale:

```
Publisher  → URL shortener publishes "link created" event to Kafka topic
Observers  → VirusTotal scanner worker   — reacts independently
             DB verdict saver worker     — reacts independently
             Email notification worker   — reacts independently
```

Kafka is essentially a highly scalable, durable, distributed Observer infrastructure. The pattern in memory works the same way — without the durability and distribution.

---

## Pattern Terminology

```
Your term          Pattern term
---------------------------------
EventPublisher  →  Subject / Observable
Services list   →  List of Observers
trigger()       →  update() or onEvent()
Services        →  Observers / Listeners
```

---

## The Structure

```
OrderObservable         ← interface — register(), remove(), broadcast()
OrderObserver           ← interface — register(), trigger()
BaseOrderObserver       ← abstract — null check, common register() logic
OrderEmailObserver      ← extends BaseOrderObserver, implements trigger()
OrderNotificationObserver ← extends BaseOrderObserver, implements trigger()
OrderPublisher          ← implements OrderObservable, maintains observer list
OrderService            ← triggers broadcast(), zero knowledge of observers
```

---

## The Implementation

**Observer interface — common contract:**

```java
public interface OrderObserver {
    void register();
    void trigger();
}
```

**Observable interface — publisher contract:**

```java
public interface OrderObservable {
    void register(OrderObserver observer);
    void remove(OrderObserver observer);   // deregister — prevents memory leak
    void broadcast();
}
```

**Abstract base observer — null check and register() written once:**

```java
public abstract class BaseOrderObserver implements OrderObserver {

    protected OrderObservable orderObservable;

    public BaseOrderObserver(OrderObservable orderObservable) {
        if (orderObservable == null)
            throw new IllegalArgumentException("OrderObservable cannot be null");
        this.orderObservable = orderObservable;
    }

    @Override
    public void register() {
        orderObservable.register(this);  // written once, inherited by all observers
    }
}
```

**Concrete observers — only implement trigger():**

```java
public class OrderEmailObserver extends BaseOrderObserver {

    public OrderEmailObserver(OrderObservable orderObservable) {
        super(orderObservable);
    }

    @Override
    public void trigger() {
        System.out.println("[Email Observer] sending email...");
    }
}

public class OrderNotificationObserver extends BaseOrderObserver {

    public OrderNotificationObserver(OrderObservable orderObservable) {
        super(orderObservable);
    }

    @Override
    public void trigger() {
        System.out.println("[Notification Observer] sending notification...");
    }
}
```

**Publisher — maintains observer list, broadcasts:**

```java
public class OrderPublisher implements OrderObservable {

    // ArrayList preserves registration order — observers trigger in order of registration
    // HashSet would prevent duplicates but not guarantee order
    private List<OrderObserver> orderEvents;

    public OrderPublisher() {
        orderEvents = new ArrayList<>();
    }

    @Override
    public void register(OrderObserver observer) {
        if (observer == null)
            throw new IllegalArgumentException("OrderObserver cannot be null");
        orderEvents.add(observer);
    }

    @Override
    public void remove(OrderObserver observer) {
        orderEvents.remove(observer);  // deregister — important to prevent memory leak
    }

    @Override
    public void broadcast() {
        orderEvents.forEach(OrderObserver::trigger);
    }
}
```

**OrderService — triggers broadcast, zero knowledge of observers:**

```java
public class OrderService {

    private final OrderObservable orderObservable;

    public OrderService(OrderObservable orderObservable) {
        this.orderObservable = orderObservable;
    }

    public void placeOrder() {
        System.out.println("[Order Service] placing order...");
        orderObservable.broadcast();  // does not know who listens or what they do
    }
}
```

---

## Usage

```java
// one time setup — observable created once
OrderObservable observable = new OrderPublisher();

// observers self-register — framework handles this via @PostConstruct in Spring
new OrderEmailObserver(observable).register();
new OrderNotificationObserver(observable).register();

// OrderService only knows about observable — zero coupling to observers
OrderService orderService = new OrderService(observable);
orderService.placeOrder();
```

**Output:**

```
[Order Service] placing order...
[Email Observer] sending email...
[Notification Observer] sending notification...
```

---

## ArrayList vs HashSet for Observer List

```
ArrayList    → preserves registration order
               allows duplicate registration — defensive check needed
               use when order of notification matters

HashSet      → prevents duplicate registration automatically
               does not guarantee notification order
               use when order does not matter and duplicates are a concern

LinkedHashSet → prevents duplicates + preserves insertion order
                best of both worlds
```

---

## Memory Leak Warning

Always implement and call `remove()`. Observers that are no longer needed but never deregistered stay in the list forever — classic memory leak in Observer.

```java
// when observer is no longer needed
observable.remove(emailObserver);
```

In Spring — `@PreDestroy` is the right hook to deregister before bean is destroyed.

---

## How Spring Handles Registration

```java
@Component
public class OrderEmailObserver extends BaseOrderObserver {

    @Autowired
    public OrderEmailObserver(OrderObservable orderObservable) {
        super(orderObservable);
    }

    @PostConstruct
    public void register() {
        orderObservable.register(this);  // auto-called by Spring at startup
    }

    @PreDestroy
    public void deregister() {
        orderObservable.remove(this);    // auto-called by Spring at shutdown
    }
}
```

---

## Key Design Decisions

**Why abstract `BaseOrderObserver`?**
`register()` logic is identical across all observers — `orderObservable.register(this)`. Writing it in every observer is duplication. Abstract base writes it once, all observers inherit it.

**Why does `OrderService` only know `OrderObservable` and not individual observers?**
That is the entire point of Observer — publisher decoupled from subscribers. `OrderService` does not know email, notification, or delivery exist. Adding new observer requires zero changes to `OrderService`.

**Why `remove()` on the interface?**
Observers not deregistered when no longer needed cause memory leaks — publisher holds references, GC cannot collect them. `remove()` must be part of the contract, not an afterthought.

---

## Where Observer Appears in LLD Case Studies

| Case Study          | Observer Used For                                      |
| ------------------- | ------------------------------------------------------ |
| Order placement     | Email, inventory, delivery react to order placed event |
| URL shortener       | Virus scan, DB save, email react to link created event |
| Stock trading       | Price change notifies all subscribed traders           |
| Notification system | User action triggers push, email, SMS observers        |

---

## Interview Version to Explain

Show `OrderService` calling every dependent directly — point out tight coupling and OCP violation.  
Introduce `OrderObservable` and `OrderObserver` interfaces.  
Show self registration — observers register themselves, publisher has no knowledge of concrete types.  
Show `OrderService.placeOrder()` calling only `broadcast()` — completely decoupled.  
Mention `remove()` and memory leak risk — shows production awareness.  
Connect to Kafka — Observer at scale with durability and distribution.
