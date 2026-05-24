# Prototype Pattern

## Problem It Solves

Creating a complex object from scratch every time is expensive and error prone when you already have a similar object in memory.

```java
// original order — 6 fields set
PrototypedOrder order1 = PrototypedOrder.builder("Pizza", 2, "UPI")
        .size("Large")
        .extraCheese(true)
        .address("123 MG Road")
        .specialInstructions("Extra crispy")
        .build();

// reorder — same everything, just payment method changed
// without Prototype you repeat all 6 fields just to change 1
PrototypedOrder order2 = PrototypedOrder.builder("Pizza", 2, "Cash") // changed
        .size("Large")
        .extraCheese(true)
        .address("123 MG Road")
        .specialInstructions("Extra crispy")
        .build();
```

**What is wrong:**

- Every field duplicated — error prone, easy to miss or accidentally change something
- Gets worse as fields grow — 15 fields means repeating 15 fields to change 1
- No intent communicated — reader cannot tell what changed between order1 and order2

---

## Core Idea

> Instead of creating an object from scratch — clone an existing object and modify only what changed.

---

## Java's Built-in Way — Cloneable

Java provides `Cloneable` interface and `clone()` method out of the box:

```java
public class Order implements Cloneable {

    @Override
    public Order clone() {
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}

// usage
Order order2 = order1.clone();
```

### The Shallow Copy Problem

`super.clone()` does a **shallow copy** — copies field values as is.

- Primitive fields (`int`, `boolean`) → independent copy ✅
- Object fields (`Address`, `List`) → same reference copied ❌

```java
// if deliveryAddress is an object
order1.address = Address object at memory location X
order2.address = same Address object at memory location X  // not a copy

order2.address.street = "456 MG Road"; // modifies order1's address too ❌
```

### Shallow vs Deep Copy

```
Shallow Copy — copies field values only
    primitives  → independent  ✅
    objects     → shared reference ❌

Deep Copy — copies field values + clones inner objects
    primitives  → independent  ✅
    objects     → independent  ✅
```

Deep copy fix:

```java
@Override
public Order clone() {
    try {
        Order cloned = (Order) super.clone();
        cloned.address = new Address(this.address); // clone inner object too
        return cloned;
    } catch (CloneNotSupportedException e) {
        throw new RuntimeException("Cloning failed", e);
    }
}
```

### Why Cloneable is Not Ideal

- Shallow copy by default — easy to forget cloning inner objects
- Returns a fixed copy — no way to modify fields before getting the final object
- Must implement deep copy manually for every inner object field

---

## Builder-Based Prototype (Production Preferred)

Combines Prototype with Builder — clone an existing object and modify only what needed before building.

```java
public class PrototypedOrder {

    private String itemName;
    private int quantity;
    private String paymentMethod;
    private String size;
    private boolean extraCheese;
    private String address;
    private String specialInstructions;

    // private — only Builder can call this
    private PrototypedOrder(Builder builder) {
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.paymentMethod = builder.paymentMethod;
        this.size = builder.size;
        this.extraCheese = builder.extraCheese;
        this.address = builder.address;
        this.specialInstructions = builder.specialInstructions;
    }

    public static class Builder {

        private String itemName;
        private int quantity;
        private String paymentMethod;
        private String size;
        private boolean extraCheese;
        private String address;
        private String specialInstructions;

        // no-args constructor — used only by from()
        // mandatory fields already copied from existing object
        private Builder() {}

        // standard constructor — enforces mandatory fields from scratch
        private Builder(String itemName, int quantity, String paymentMethod) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.paymentMethod = paymentMethod;
        }

        // copies all fields from existing order into Builder
        // caller then modifies only what changed before calling build()
        private static Builder from(PrototypedOrder order) {
            Builder builder = new Builder();
            builder.itemName = order.itemName;
            builder.quantity = order.quantity;
            builder.paymentMethod = order.paymentMethod;
            builder.size = order.size;
            builder.extraCheese = order.extraCheese;
            builder.address = order.address;
            builder.specialInstructions = order.specialInstructions;
            return builder;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder extraCheese(boolean extraCheese) {
            this.extraCheese = extraCheese;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        public PrototypedOrder build() {
            if (this.itemName == null || this.itemName.isEmpty())
                throw new IllegalStateException("itemName cannot be empty");
            if (this.quantity <= 0)
                throw new IllegalStateException("Quantity must be greater than 0");
            return new PrototypedOrder(this);
        }
    }

    // entry point — create from scratch
    public static Builder builder(String itemName, int quantity, String paymentMethod) {
        return new Builder(itemName, quantity, paymentMethod);
    }

    // entry point — create from existing object
    public static Builder from(PrototypedOrder order) {
        return Builder.from(order);
    }
}
```

**Usage:**

```java
// original order
PrototypedOrder order1 = PrototypedOrder.builder("Pizza", 2, "UPI")
        .size("Large")
        .extraCheese(true)
        .address("123 MG Road")
        .specialInstructions("Extra crispy")
        .build();

// reorder — same everything, just payment method changed
PrototypedOrder order2 = PrototypedOrder.from(order1)
        .paymentMethod("Cash")
        .build();

// prove they are independent objects
System.out.println(order1 == order2); // false — different objects in memory
```

---

## Why Builder-Based Prototype is Better than Cloneable

|                          | Cloneable               | Builder-Based                            |
| ------------------------ | ----------------------- | ---------------------------------------- |
| Shallow copy risk        | ✅ exists               | ❌ none — each field copied individually |
| Modify before finalizing | ❌ not possible         | ✅ chain methods before build()          |
| Immutability preserved   | ❌ needs setters        | ✅ no setters needed                     |
| Intent communicated      | ❌ unclear what changed | ✅ only changed fields are visible       |

---

## Key Design Decisions

**Why a no-args private Builder constructor for `from()`?**
When cloning, mandatory fields are copied from the existing object — not provided upfront. The no-args constructor allows `from()` to populate all fields directly without going through the mandatory-fields constructor.

**Why is `Builder.from()` private?**
It should only be called through `PrototypedOrder.from()` — the public entry point. Direct access to `Builder.from()` from outside is not intended.

**Why not just use setters on the Order object?**
Setters break immutability. Once an Order is built it should not be modifiable. Prototype via Builder gives you a fresh independent object with the changes applied — original stays untouched.

---

## Where Prototype Appears in LLD Case Studies

| Case Study       | Prototype Used For                                      |
| ---------------- | ------------------------------------------------------- |
| Food ordering    | Reorder — clone previous order, change payment          |
| Document editor  | Duplicate slide or page with same formatting            |
| Game development | Spawn enemy clones with same base stats                 |
| Configuration    | Clone base config, override environment specific values |

---

## Relationship with Builder

Prototype and Builder are often used together in production:

```
Builder   → creates objects cleanly with mandatory + optional fields
Prototype → clones existing objects when you need a similar one

Combined  → from() clones via Builder, modify only what changed, build() creates fresh object
```

---

## Interview Version to Write

Explain the problem first — repeating all fields just to change one is error prone.  
Mention `Cloneable` exists but has shallow copy risk.  
Write the Builder-based approach as the clean production solution.  
Demonstrate with `order1` and `order2` — `order1 == order2` prints `false`.
