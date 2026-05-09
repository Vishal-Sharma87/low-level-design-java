# Low Level Design — Java

A structured, hands-on repository for mastering Low Level Design concepts in Java — built concept by concept, with code and notes at every step.

---

## Progress Tracker

| Block               | Status      |
| ------------------- | ----------- |
| ✅ OOP              | Complete    |
| 🔄 SOLID Principles | In Progress |
| ⬜ Design Patterns  | Upcoming    |
| ⬜ LLD Problems     | Upcoming    |

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
    ├── design-patterns/
    └── problems/
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

- `oop/constructor/constructor_notes.md`
- `oop/encapsulation/encapsulation_notes.md`
- `oop/inheritance/inheritance_notes.md`
- `oop/abstraction/abstraction_notes.md`
- `oop/static-final/static_and_final_notes.md`

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
