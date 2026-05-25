# `static` and `final` Keywords — Interview Q&A

---

## `static` Keyword

**Core idea:** `static` members belong to the **class**, not to any individual object.

---

**Q1. What is the `static` keyword? Where can it be applied?**
`static` means the member belongs to the class itself, shared across all objects. Can be applied to:

- Fields → one copy shared across all instances
- Methods → called on class, not object
- Blocks → runs once when class is loaded
- Nested classes → belongs to outer class, not instance

---

**Q2. Can a `static` method access non-static fields?**
No. Static methods are class-bound, non-static fields are instance-bound. A static method has no `this` reference — it doesn't know which object's field to point to. `this` keyword is also not allowed inside static methods.

---

**Q3. What is a static initializer block?**
A block that runs **once when the class is loaded** — before any object is created. Used to initialize complex static fields.

```java
private static AtomicInteger totalCars;

static {
    totalCars = new AtomicInteger(0); // runs once at class load
}
```

For simple fields, inline initialization is enough:

```java
private static int totalCars = 0;
```

---

**Q4. Why use `AtomicInteger` instead of `int` for a shared counter?**
A regular `int` counter is not thread safe:

```java
totalCars++; // two threads can increment simultaneously and lose a count
```

`AtomicInteger.incrementAndGet()` is **thread safe** — guarantees correct count even when multiple threads create objects simultaneously. Use `AtomicInteger` for shared counters in real systems.

---

**Q5. What is the difference between static and instance methods?**

| Aspect                       | Static Method                        | Instance Method             |
| ---------------------------- | ------------------------------------ | --------------------------- |
| Belongs To                   | Class                                | Object                      |
| Memory Association           | Class area                           | Object memory               |
| Invocation Style             | `ClassName.method()`                 | `object.method()`           |
| Requires Object Creation     | No                                   | Yes                         |
| Access to Instance Variables | No                                   | Yes                         |
| Access to Static Variables   | Yes                                  | Yes                         |
| Access to `this` Keyword     | Not allowed                          | Available                   |
| Method Overriding            | Not truly overridden (method hiding) | Supports runtime overriding |
| Polymorphism Support         | Limited                              | Fully supported             |
| Execution Nature             | Common/shared behavior               | Object-specific behavior    |
| Typical Usage                | Utility/helper methods               | Object behavior             |
| Example                      | `Math.max()`                         | `student.getName()`         |

---

## `final` Keyword

**Core idea:** `final` means **cannot be changed.**

---

**Q6. Where can `final` be applied and what does it mean in each case?**

**`final` field** → cannot be reassigned after initialization:

```java
public static final double PI = 3.14159; // constant — never changes
```

Non-static `final` fields must be initialized **exactly once** — either at declaration or in the constructor.

**`final` method** → cannot be overridden by child classes:

```java
public final String getPolicy() {
    return "No negative speed allowed"; // child cannot override this
}
```

**`final` class** → cannot be extended:

```java
public final class VehicleConstants {
    public static final int MAX_SPEED = 300;
}
// No class can extend VehicleConstants
```

---

**Q7. Why is `String` a `final` class in Java?**
Three reasons:

1. **Security** → Passwords, file paths, network connections use Strings. If subclassable, malicious code could override behaviour and leak sensitive data.

2. **String Pool** → Java reuses String objects in memory via the String pool:

```java
String a = "hello";
String b = "hello"; // same object from pool, not new
```

If Strings were mutable or subclassable, one reference changing the value would corrupt all others pointing to the same object.

3. **Thread Safety** → Immutable objects are inherently thread safe — no synchronization needed. Multiple threads can safely share the same String object.

> **One liner:** `String` is `final` to guarantee immutability, security, and safe reuse via the String pool.

---

**Q8. What is the difference between `final`, `finally`, and `finalize`?**

|            | Meaning                                                                       |
| ---------- | ----------------------------------------------------------------------------- |
| `final`    | Keyword — prevents modification of field, method, or class                    |
| `finally`  | Block in try-catch — always executes regardless of exception                  |
| `finalize` | Method — called by GC before object is garbage collected (deprecated Java 9+) |

---

## `static` Keyword practical working Demo

```java
    public static void main(String[] args) {

        // creating normal car
        Car coupe = new Car("BMW", "Blue", 240);

        // creating ElectricCar
        Car m440i = new ElectricCar("BMW", "White", 200, 94);

        // creating HybridCar
        Car tesla = new HybridCar("Tesla", "Red", 150, 50);

        // Counting total cars created using static totalCars static field
        System.out.println("total cars: " + Car.getToTalCars());

    }
```
