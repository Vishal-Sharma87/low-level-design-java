# Dependency Inversion Principle (DIP)

## Definition

> _"High-level modules should not depend on low-level modules. Both should depend on abstractions.
> Abstractions should not depend on details. Details should depend on abstractions."_

---

## High-level vs Low-level modules

| Type              | Meaning                                        | Example                                     |
| ----------------- | ---------------------------------------------- | ------------------------------------------- |
| High-level module | Contains business logic, orchestrates the flow | `OrderService`, `NotificationService`       |
| Low-level module  | Does the actual technical work                 | `MySQLDatabase`, `EmailSender`, `SMSSender` |

---

## The Real Meaning

Your business logic should never know or care **which specific tool** it is using.
It should only know **what the tool can do.**

The dependency arrow should always point toward the abstraction — never from high-level directly to low-level.

```
Before DIP:
OrderService  →  MySQLDatabase
(high level)     (low level — tightly coupled)

After DIP:
OrderService  →  Database  ←  MySQLDatabase
(high level)  (abstraction)   (low level)
```

Both sides depend on the abstraction in the middle. Neither depends on each other directly.
This is what **"inversion"** means — the dependency arrow is inverted through an abstraction.

---

## Warning Signs of Violation

| Signal                                                   | What it tells you                      |
| -------------------------------------------------------- | -------------------------------------- |
| `new ConcreteClass()` inside a service or business class | Hard dependency on implementation      |
| Changing infrastructure requires editing business logic  | High and low level are tightly coupled |
| Cannot write a unit test without real infrastructure     | No abstraction layer exists            |
| Class imports full of concrete implementation names      | Depending on details, not abstractions |

---

## Dependency Injection — the technique to achieve DIP

DIP is the **principle.** Dependency Injection is the **technique** used to implement it.

| Type                  | When to use                                      |
| --------------------- | ------------------------------------------------ |
| Constructor Injection | Required dependencies — preferred approach       |
| Setter Injection      | Optional dependencies, changeable after creation |
| Interface Injection   | Rare — uncommon in Java                          |

> Always prefer **constructor injection** — it makes dependencies explicit, visible,
> and forces the object to be fully ready at creation time.

---

## Connection to Other Principles

| Principle | How it connects to DIP                                                           |
| --------- | -------------------------------------------------------------------------------- |
| **SRP**   | Each class has one job, so the abstraction it exposes is clean and focused       |
| **OCP**   | New implementations plug in without modifying existing business logic            |
| **LSP**   | Any implementation of the abstraction can substitute another safely              |
| **ISP**   | Lean interfaces mean implementing classes are never burdened with unused methods |

> SOLID is not five separate rules. It is one coherent design philosophy seen from five angles.

---

## The Biggest Practical Benefit — Testability

When business logic directly creates `new MySQLDatabase()`, testing it requires:

- A real running database
- A real network connection
- Real infrastructure setup

After DIP, you inject a `MockDatabase` in tests.
Business logic is tested in **complete isolation** — no database, no network, no setup overhead.

---

## Runnable Working Code

```java
public class Main {

    public static void main(String[] args) {

        // Creating DatabaseService at the start of program by passing the "database" ->
        // can be MySQL, PostgreSQL or any other
        DatabaseService databaseService = new DatabaseService(new MySQLDatabase());

        // creating "OrderService" by passing the above created "DatabaseService"
        OrderService orderService = new OrderService(databaseService);

        // Placing one order
        int orderId = orderService.placeOrder("Name:vishal");

        // checking the order associated with above orderId
        System.out.printf("Order with orderId %d is %s", orderId, String.valueOf(orderService.findById(orderId)));
    }
}
```

---

## Output

```
[OrderService] Placing order: Name:vishal
[MySQL] Saved record with id=1 data=Name:vishal
[OrderService] Order placed successfully. orderId=1
[MySQL] Fetching record with id=1
Order with orderId 1 is Name:vishal
```

Switching to PostgreSQL — change exactly **one line** in `main()`:

```java
DatabaseService databaseService = new DatabaseService(new PostgreSQLDatabase());
```

`OrderService` and `DatabaseService` are **never touched.**
Output prefix changes from `[MySQL]` to `[PostgreSQL]`.
Business logic is completely unaffected.

---

## One Line to Remember

> Your business logic should never know or care which specific tool it is using.
> It should only know what the tool can do.
