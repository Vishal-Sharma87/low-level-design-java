# Builder Pattern

## Problem It Solves

Objects with many fields where not all fields are mandatory cause two problems:

**1. Forced nulls — unreadable object creation:**

```java
// which null is for what? impossible to tell at a glance
Order order = new Order("Pizza", 2, "Large", true, false, null, "UPI", null);
```

**2. Telescoping constructors — combination explosion:**

```java
// how many constructors do you write to cover all valid combinations?
public Order(String itemName, int quantity, String paymentMethod) { }
public Order(String itemName, int quantity, String paymentMethod, String size) { }
public Order(String itemName, int quantity, String paymentMethod, String size, String deliveryAddress) { }
// ... keeps growing, never enough
```

---

## Core Idea

> Separate mandatory fields from optional fields.  
> Enforce mandatory fields upfront.  
> Let optional fields be set only when needed.  
> Create the object only when it is in a fully valid state.

---

## The Scenario

A food ordering system where an Order has mandatory and optional fields:

```
Mandatory:
- itemName       → cannot have an order without an item
- quantity       → cannot have an order without quantity
- paymentMethod  → cannot complete an order without payment

Optional:
- size
- extraCheese
- extraSauce
- deliveryAddress
- specialInstructions
```

---

## Progressive Evolution

### v1 — Naive (one big constructor, forced nulls)

```java
/**
 * v1 - naive approach
 * single constructor with all fields
 * caller forced to pass null for fields that do not apply
 * unreadable, error prone, gets worse as fields grow
 */
public NaiveOrder(
        String itemName,
        int quantity,
        String size,
        boolean extraCheese,
        boolean extraSauce,
        String deliveryAddress,
        String paymentMethod,
        String specialInstructions) { }
```

**Usage — the actual pain:**

```java
// which null is for what? caller has no idea without checking the class
NaiveOrder pickupOrder = new NaiveOrder("Pizza", 2, "Large", true, false, null, "UPI", null);
NaiveOrder dineinOrder = new NaiveOrder("Burger", 1, null, false, false, null, "Cash", null);
```

**What is wrong:**

- Caller forced to pass null for fields that do not apply
- Unreadable — impossible to tell what each value represents
- No enforcement of which fields are mandatory
- Gets worse as fields grow

---

### v2 — Telescoping Constructor (multiple constructors, combination explosion)

```java
/**
 * v2 - telescoping constructor approach
 * common attempt to fix the null problem from v1
 * introduces a new problem — constructor explosion
 * no finite set of constructors can cover all valid field combinations
 */

// mandatory fields only
public TelescopedOrder(String itemName, int quantity, String paymentMethod) { }

// with size
public TelescopedOrder(String itemName, int quantity, String paymentMethod, String size) { }

// with size and delivery
public TelescopedOrder(String itemName, int quantity, String paymentMethod,
        String size, String deliveryAddress) { }

// all fields
public TelescopedOrder(String itemName, int quantity, String size,
        boolean extraCheese, boolean extraSauce, String deliveryAddress,
        String paymentMethod, String specialInstructions) { }

// ... and so on
```

**Usage — the confusion:**

```java
// which constructor is this calling?
// is "Large" the size or deliveryAddress?
// impossible to tell without checking the class
TelescopedOrder order = new TelescopedOrder("Pizza", 2, "UPI", "Large");
```

**What is wrong:**

- Cannot determine a definitive number of constructors to cover all combinations
- Two constructors with same parameter types cannot coexist in Java
- Still does not enforce valid object state
- Grows unmanageable fast

---

### v3 — Optimal (Builder Pattern)

**Core idea of the implementation:**

- Private constructor on `Order` — nobody can do `new Order(...)` directly
- Static nested `Builder` class — tightly coupled to `Order`, cannot exist independently
- Mandatory fields enforced in `Builder` constructor — must be provided upfront
- Optional fields set via chained methods — only when needed
- `build()` validates and creates the `Order` object

```java
public class OptimalOrder {

    // mandatory fields
    private String itemName;
    private int quantity;
    private String paymentMethod;

    // optional fields
    private String size;
    private boolean extraCheese;
    private boolean extraSauce;
    private String deliveryAddress;
    private String specialInstructions;

    // private — only Builder can call this
    private OptimalOrder(Builder builder) {
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.paymentMethod = builder.paymentMethod;
        this.size = builder.size;
        this.extraCheese = builder.extraCheese;
        this.extraSauce = builder.extraSauce;
        this.deliveryAddress = builder.deliveryAddress;
        this.specialInstructions = builder.specialInstructions;
    }

    public static class Builder {

        private String itemName;
        private int quantity;
        private String paymentMethod;
        private String size;
        private boolean extraCheese;
        private boolean extraSauce;
        private String deliveryAddress;
        private String specialInstructions;

        // mandatory fields enforced here — must be provided upfront
        private Builder(String itemName, int quantity, String paymentMethod) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.paymentMethod = paymentMethod;
        }

        // optional fields — each sets field and returns Builder for chaining
        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder extraCheese(boolean extraCheese) {
            this.extraCheese = extraCheese;
            return this;
        }

        public Builder extraSauce(boolean extraSauce) {
            this.extraSauce = extraSauce;
            return this;
        }

        public Builder deliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        // validates state then creates the Order — only entry point to new OptimalOrder()
        public OptimalOrder build() {
            if (this.itemName == null || this.itemName.isEmpty())
                throw new IllegalStateException("itemName cannot be empty");
            if (this.quantity <= 0)
                throw new IllegalStateException("Quantity must be greater than 0");
            return new OptimalOrder(this);
        }
    }

    // static entry point — clean and readable
    public static Builder builder(String itemName, int quantity, String paymentMethod) {
        return new Builder(itemName, quantity, paymentMethod);
    }
}
```

**Usage — clean, readable, self explanatory:**

```java
// pickup order — no delivery address
OptimalOrder pickupOrder = OptimalOrder.builder("Pizza", 2, "UPI")
        .size("Large")
        .extraCheese(true)
        .build();

// delivery order
OptimalOrder deliveryOrder = OptimalOrder.builder("Burger", 1, "Cash")
        .deliveryAddress("123 MG Road")
        .specialInstructions("Extra crispy")
        .build();

// minimal order — only mandatory fields
OptimalOrder minimalOrder = OptimalOrder.builder("Coke", 1, "UPI")
        .build();
```

**What improved:**

- No nulls — optional fields simply not set if not needed
- Mandatory fields enforced at compile time via Builder constructor
- Readable — each field name is explicit at call site
- Validation in `build()` — invalid object cannot be created
- Immutable after creation — no setters exposed

---

## Full Evolution Summary

| Version        | Nulls   | Readable | Mandatory Enforced | Validation |
| -------------- | ------- | -------- | ------------------ | ---------- |
| v1 Naive       | forced  | NO       | NO                 | NO         |
| v2 Telescoping | partial | NO       | partial            | NO         |
| v3 Builder     | none    | YES      | YES                | YES        |

---

## Why Builder is a Static Nested Class

Three reasons:

- Needs access to `OptimalOrder`'s private constructor — only possible from within the same class
- Tightly coupled to `OptimalOrder` — makes no sense to exist independently
- `static` means `new OptimalOrder.Builder(...)` does not require an `OptimalOrder` instance first

---

## Lombok @Builder — What It Does Under the Hood

In real projects you use:

```java
@Builder
public class Order { }
```

Lombok generates exactly what we wrote manually — the nested `Builder` class, the private constructor, all the chained methods, and `build()`. Understanding the manual version means you know what Lombok is doing and can explain it in interviews.

---

## Key Design Decisions

**Why private constructor on Builder?**
Forces callers to use the static `builder()` method — reads naturally as `OptimalOrder.builder(...)`.

**Why mandatory fields in Builder constructor, not chained methods?**
If mandatory fields were chained methods, nothing would stop a caller from skipping them. Constructor enforces them at compile time.

**Why validation in `build()` and not in the Builder constructor?**
All fields need to be set before validation makes sense. `build()` is the final step — the right place to check the complete state before creating the object.

---

## Where Builder Appears in LLD Case Studies

| Case Study    | Builder Used For                               |
| ------------- | ---------------------------------------------- |
| Food ordering | `Order` with mandatory and optional fields     |
| HTTP client   | `HttpRequest` with headers, params, body       |
| User profile  | `UserProfile` with optional bio, avatar, links |
| Query builder | `DatabaseQuery` with filters, sort, pagination |

---

## Interview Version to Write

Write **v3 — optimal Builder**.  
Walk the interviewer through v1 null problem and v2 telescoping problem first — shows you understand the motivation, not just the pattern.  
If asked about Lombok, explain that `@Builder` generates this exact structure automatically.
