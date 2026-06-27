# Low Level Design — Java

A structured, hands-on repository for mastering Low Level Design concepts in Java — built concept by concept, with code and notes at every step.

---

## Progress Tracker

| Block               | Status      |
| ------------------- | ----------- |
| ✅ OOP              | Complete    |
| ✅ SOLID Principles | Complete    |
| ✅ Design Patterns  | Complete    |
| 🔄 LLD Case Studies | In Progress |

---

## Repository Structure

```
src/
└── main/java/com/vishal/lld/
    ├── oop/
    │   ├── Car.java
    │   ├── constructor/
    │   ├── encapsulation/
    │   ├── inheritance/
    │   │   ├── ElectricCar.java
    │   │   └── HybridCar.java
    │   ├── abstraction/
    │   │   ├── Shape.java
    │   │   ├── Circle.java
    │   │   ├── Rectangle.java
    │   │   └── Chargeable.java
    │   └── static-final/
    ├── solid/
    │   ├── srp/
    │   │   ├── bad/
    │   │   ├── good/
    │   │   └── README.md
    │   ├── ocp/
    │   │   ├── bad/
    │   │   ├── good/
    │   │   └── README.md
    │   ├── lsp/
    │   │   ├── bad/
    │   │   ├── good/
    │   │   └── README.md
    │   ├── isp/
    │   │   ├── bad/
    │   │   ├── good/
    │   │   └── README.md
    │   └── dip/
    │       ├── bad/
    │       ├── good/
    │       └── README.md
    ├── design-patterns/
    │   ├── creational/
    │   │   ├── singleton/
    │   │   ├── factory/
    │   │   └── builder/
    │   ├── structural/
    │   │   ├── adapter/
    │   │   ├── decorator/
    │   │   └── facade/
    │   └── behavioral/
    │       ├── strategy/
    │       ├── observer/
    │       └── state/
    └── casestudies/
        ├── parkinglot/
        │   ├── models/
        │   ├── enums/
        │   ├── factory/
        │   ├── interfaces/
        │   ├── paymentstrategies/
        │   ├── ParkingLot.java
        │   ├── SlotManager.java
        │   ├── BillingSystem.java
        │   └── README.md
        └── elevatorsystem/
            ├── models/
            ├── enums/
            ├── interfaces/
            ├── observers/
            ├── panels/
            ├── states/
            ├── strategy/
            ├── Elevator.java
            ├── ElevatorTracker.java
            ├── ElevatorAssignmentService.java
            └── README.md
```

---

## Block 1 — OOP (Object Oriented Programming)

### Concepts Covered

**1. Classes & Objects**

- Class as a blueprint, object as an instance
- Fields, methods, and the `new` keyword

**2. Constructors & `this` Keyword**

- Parameterized constructors with validation
- `this` for field assignment, constructor chaining, and passing current object
- Default constructor removed when custom constructor is defined

**3. Encapsulation**

- `private` fields with `public` getters and setters
- Validation in constructor vs setter — when to use which
- Immutable classes — getters only, no setters

**4. Inheritance**

- `extends` keyword, code reuse across child classes
- `super()` for parent constructor, `super.method()` for parent method
- `private` fields not directly accessible in child — accessed via inherited getters/setters

**5. Method Overriding & `super`**

- `@Override` annotation and its importance
- Rules — same name, same parameters, same or covariant return type
- Cannot override `final` or `static` methods

**6. Polymorphism**

- Runtime polymorphism — parent reference, child object
- Dynamic method dispatch — JVM resolves method at runtime
- Compile-time polymorphism — method overloading
- `instanceof` and pattern matching (Java 16+)

**7. Abstraction**

- Abstract class — partial abstraction, shared state and behaviour
- Interface — contract, capability-based design
- `default` and `static` methods in interfaces (Java 8+)
- IS-A (abstract class) vs CAN-DO (interface) relationship

**8. `static` Keyword**

- Static fields — shared across all instances
- Static methods — class-bound, no `this`
- Static initializer blocks
- `AtomicInteger` for thread-safe counters

**9. `final` Keyword**

- `final` field — initialized once, never reassigned
- `final` method — cannot be overridden
- `final` class — cannot be extended
- Why `String` is `final` in Java

---

## Block 2 — SOLID Principles

### Principles Covered

**1. SRP — Single Responsibility Principle**

- A class should have only one reason to change
- Example: Invoice system — separating printing, persistence, and calculation
- Violation: one class handling business logic + DB + formatting

**2. OCP — Open/Closed Principle**

- Open for extension, closed for modification
- Example: Payment system using Strategy + Factory patterns
- Adding new payment methods without touching existing code

**3. LSP — Liskov Substitution Principle**

- Subtypes must be substitutable for their base types without breaking behaviour
- Examples: Bird/Penguin, Employee/ContractEmployee
- Violation: subclass that throws `UnsupportedOperationException` for inherited methods

**4. ISP — Interface Segregation Principle**

- Clients should not be forced to depend on methods they don't use
- Examples: Worker system, Printer system
- Split fat interfaces into focused, role-specific contracts

**5. DIP — Dependency Inversion Principle**

- High-level modules should not depend on low-level modules — both should depend on abstractions
- Example: OrderService → DatabaseService → MySQLDatabase layered architecture
- Constructor injection as the preferred technique

---

## Block 3 — Design Patterns

### Creational Patterns

| Pattern   | Intent                                       | Used In                 |
| --------- | -------------------------------------------- | ----------------------- |
| Singleton | One instance, global access point            | DB connections, configs |
| Factory   | Delegate object creation to subclasses       | Payment processors      |
| Builder   | Step-by-step construction of complex objects | HTTP requests, queries  |

### Structural Patterns

| Pattern   | Intent                                        | Used In                  |
| --------- | --------------------------------------------- | ------------------------ |
| Adapter   | Make incompatible interfaces work together    | Third-party integrations |
| Decorator | Add behaviour dynamically without subclassing | Notification wrappers    |
| Facade    | Simplified interface over a complex subsystem | SDK wrappers             |

### Behavioral Patterns

| Pattern  | Intent                                               | Used In                       |
| -------- | ---------------------------------------------------- | ----------------------------- |
| Strategy | Encapsulate interchangeable algorithms               | Sorting, payment, routing     |
| Observer | Notify multiple dependents on state change           | Event systems, Kafka, pub-sub |
| State    | Object changes behaviour when internal state changes | Vending machine, elevators    |

---

## Block 4 — LLD Case Studies

Applying multiple patterns together to solve real system design problems.

| Problem                | Patterns Applied                       | Status      |
| ---------------------- | -------------------------------------- | ----------- |
| Parking Lot            | Singleton, Strategy, Factory           | ✅ Complete |
| Elevator System        | State, Strategy, Observer              | ✅ Complete |
| Notification System    | Factory, Observer, Decorator, Strategy | ⬜ Upcoming |
| Library Management     | Factory, Observer, Strategy            | ⬜ Upcoming |
| Food Delivery (Swiggy) | Factory, Strategy, Observer, Builder   | ⬜ Upcoming |
| ATM Machine            | State, Strategy, Singleton             | ⬜ Upcoming |

---

### Parking Lot

**Patterns:** Singleton, Strategy, Factory

**Key design decisions:**
- `SlotManager` (Singleton) — owns two `Map<VehicleType, LinkedHashSet<Integer>>` for available and occupied slots; O(1) assignment and release
- `BillingSystem` + `PaymentStrategy` — pluggable fee calculation per slot type; strategies self-register via static initializer blocks and `Class.forName()` in `PaymentStrategyFactory`
- `ParkingTicket` — records vehicle, slot, and arrival time; consumed on exit
- Slot release is conditional — only after confirmed payment; failed billing leaves slot occupied
- `VehicleType` and `SlotType` unified into one enum for base design; separation deferred to v2 when overflow parking (BIKE in TRUCK slot) is introduced

---

### Elevator System

**Patterns:** State, Strategy, Observer

**Key design decisions:**
- Each `Elevator` implements `Runnable` — runs its own movement loop on a dedicated thread; no central scheduler
- Two `PriorityBlockingQueue` per elevator — `upQueue` (min heap) for ascending floors, `downQueue` (max heap, `Comparator.reverseOrder()`) for descending floors; ensures SCAN-style ordered processing
- `addFloorToQueue(int floor)` derives direction from `currentFloor` vs destination internally — not from user's requested direction. This prevents a critical bug where an elevator at floor 0 assigned a DOWN request for floor 5 would enqueue into `downQueue` and never move upward to reach the pickup floor
- CPU-efficient idle blocking — `IdleState` calls `elevator.wait()` when both queues are empty; `addFloorToQueue()` calls `notify()` on new request; each elevator blocks and wakes independently on its own monitor
- `updateStatusAndState(ElevatorState, Status)` — single method keeps `ElevatorState` interface and `Status` enum in sync atomically; prevents partial state inconsistency
- `NearestElevatorStrategy` — calculates SCAN-aware cost per elevator based on current status, direction, and floor position; accounts for elevators that must complete current direction before reversing
- `ElevatorTracker` (Singleton, Observer) — holds full `Elevator` references for assignment service; prints floor and status on each `moveUp()`/`moveDown()` notification

---

## Key Classes Built

| Class         | Type                                   | Highlights                                        |
| ------------- | -------------------------------------- | ------------------------------------------------- |
| `Car`         | Concrete                               | Encapsulation, static counter, validation         |
| `ElectricCar` | Extends `Car`                          | Inheritance, overriding, `super()`                |
| `HybridCar`   | Extends `Car`, implements `Chargeable` | Multiple interface implementation                 |
| `Shape`       | Abstract                               | Abstract method `area()`, template method pattern |
| `Circle`      | Extends `Shape`                        | `Math.PI`, concrete `area()`                      |
| `Rectangle`   | Extends `Shape`                        | Concrete `area()`                                 |
| `Chargeable`  | Interface                              | Capability contract, `default` method             |

---

## Notes

Each concept has a dedicated notes file with interview Q&A:

- `oop/constructor/README.md`
- `oop/encapsulation/README.md`
- `oop/inheritance/README.md`
- `oop/abstraction/README.md`
- `oop/static-final/README.md`
- `solid/srp/README.md`
- `solid/ocp/README.md`
- `solid/lsp/README.md`
- `solid/isp/README.md`
- `solid/dip/README.md`
- `casestudies/parkinglot/README.md`
- `casestudies/elevatorsystem/README.md`

---

## Setup

**Prerequisites:** Java 21+, Maven

**Run:**

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.vishal.lld.Main"
```

---

## Tech Stack

- **Language:** Java 21
- **Build Tool:** Maven
- **IDE:** VS Code