# Facade Pattern

## Problem It Solves

A caller knowing too much — directly coupled to multiple subsystems to complete one logical operation.

```java
// OrderController orchestrating everything itself
public class OrderController {
    public void placeOrder(Order order) {
        inventoryService.checkAvailability(order);   // knows InventoryService
        paymentService.processPayment(order);         // knows PaymentService
        notificationService.sendConfirmation(order);  // knows NotificationService
        deliveryService.scheduleDelivery(order);      // knows DeliveryService
    }
}
```

**What is wrong:**

- Controller's job is to receive request and delegate — not orchestrate business logic
- 3 more controllers need to place orders — all duplicate the same 4 steps
- Tomorrow a 5th step added — every controller that duplicates this flow needs updating — OCP violation
- Controller coupled to 4 services — changing any service signature ripples into controller

---

## Core Idea

> Provide a single simplified interface over a complex subsystem of multiple services.  
> Caller talks to one thing. Facade orchestrates everything internally.

---

## The Solution

```
Before — caller knows everything:
OrderController → inventoryService
               → paymentService
               → notificationService
               → deliveryService

After — caller knows one thing:
OrderController → OrderFacade → inventoryService
                             → paymentService
                             → notificationService
                             → deliveryService
```

**Facade — owns the orchestration:**

```java
public class OrderFacade {

    private InventoryService inventoryService;
    private PaymentService paymentService;
    private NotificationService notificationService;
    private DeliveryService deliveryService;

    public OrderFacade(InventoryService inventoryService,
                       PaymentService paymentService,
                       NotificationService notificationService,
                       DeliveryService deliveryService) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
        this.deliveryService = deliveryService;
    }

    public void placeOrder(Order order) {
        inventoryService.checkAvailability(order);
        paymentService.processPayment(order);
        notificationService.sendConfirmation(order);
        deliveryService.scheduleDelivery(order);
    }
}
```

**Controller — knows nothing about internals:**

```java
public class OrderController {
    private OrderFacade orderFacade;

    public void placeOrder(Order order) {
        orderFacade.placeOrder(order);  // one line — fully delegated
    }
}
```

Tomorrow a 5th step added — change only `OrderFacade`. Zero controller changes.

---

## You Already Use This Pattern

The **Service layer** in any Spring application is a Facade.

```
Controller  → receives HTTP request, delegates to Service
Service     → orchestrates repositories, external APIs, cache, notifications
Repository  → talks to database
```

`Service` is the Facade — it hides the complexity of multiple subsystems behind one clean interface. Controller just calls one method and gets a response.

```java
// Spring example — this IS the Facade pattern
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;  // the Facade

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        orderService.placeOrder(request);  // one call — Facade handles everything
        return ResponseEntity.ok().build();
    }
}

@Service
public class OrderService {  // the Facade

    @Autowired private InventoryService inventoryService;
    @Autowired private PaymentService paymentService;
    @Autowired private NotificationService notificationService;
    @Autowired private DeliveryService deliveryService;

    public void placeOrder(OrderRequest request) {
        inventoryService.checkAvailability(request);
        paymentService.processPayment(request);
        notificationService.sendConfirmation(request);
        deliveryService.scheduleDelivery(request);
    }
}
```

---

## Summary

```
Problem  → caller coupled to multiple subsystems
           orchestration logic duplicated across callers
           adding new step requires updating every caller

Solution → Facade sits in between
           exposes one simple method
           owns the orchestration internally
           caller depends on Facade only
```

---

## Facade vs Other Patterns

```
Adapter  → translates incompatible interfaces — two systems that cannot talk
Facade   → simplifies a complex subsystem — one system that is too complex to talk to directly

Facade vs Service Layer
         → same idea, different context
           Facade is the pattern name
           Service layer is the Spring/backend implementation of it
```

---

## Key Design Decisions

**Why not just put orchestration logic in the controller?**
Controller's responsibility is HTTP — parsing requests, returning responses. Business orchestration is not its concern. Mixing them violates SRP.

**Does Facade hide the subsystems completely?**
Not necessarily. Subsystems can still be used directly when needed. Facade just provides a convenient entry point for the common case.

**Is Facade the same as Service layer?**
Yes — Service layer is the real world backend implementation of the Facade pattern. When you explain Facade in an interview, connecting it to Service layer shows you understand both theory and practice.

---

## Where Facade Appears in LLD Case Studies

| Case Study    | Facade Used For                                                      |
| ------------- | -------------------------------------------------------------------- |
| Food ordering | `OrderService` — inventory, payment, notification, delivery          |
| Ride sharing  | `TripService` — matching, pricing, notification, tracking            |
| Banking       | `TransactionService` — validation, transfer, audit, notification     |
| Hotel booking | `BookingService` — availability, payment, confirmation, housekeeping |

---

## Interview Version to Explain

Show the controller knowing too many services — point out SRP and OCP violations.  
Introduce Facade as the single entry point that owns orchestration.  
Connect it to the Service layer in Spring — shows real world understanding.  
"Controller delegates to Service, Service orchestrates subsystems — Service is the Facade."
