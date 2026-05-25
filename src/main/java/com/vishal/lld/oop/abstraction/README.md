# Abstraction — Interview Q&A

---

**Q1. What is Abstraction? How is it different from Encapsulation?**

**Abstraction** → Hide the actual implementation logic, expose only what it does.
Like a `sort()` method — caller knows it sorts, not which algorithm it uses internally.

**Encapsulation** → Make fields private, prevent direct modification, and allow controlled access through public methods with validation logic. Control is in the code, not the user.

Key difference:

- Encapsulation → hides **data**
- Abstraction → hides **implementation**

---

**Q2. What is the difference between an Abstract class and an Interface?**

|              | Abstract Class           | Interface                             |
| ------------ | ------------------------ | ------------------------------------- |
| Abstraction  | Partial                  | Full (contract)                       |
| Methods      | Abstract + regular       | Abstract + default + static (Java 8+) |
| Fields       | Can have instance fields | No instance fields                    |
| Constructor  | Yes                      | No                                    |
| Keyword      | `extends`                | `implements`                          |
| Multiple     | Only one                 | Multiple allowed                      |
| Relationship | IS-A                     | CAN-DO                                |

> **Rule of thumb:** Abstract class for shared identity. Interface for shared capability.
> `HybridCar IS-A Car` → abstract class. `HybridCar CAN charge()` → interface.

---

**Q3. Can you instantiate an Abstract class?**
No. `new Shape()` is a compile error.

What looks like instantiation is actually an **anonymous subclass:**

```java
Shape s = new Shape() {       // NOT instantiating Shape
    public double area() {     // creating an unnamed subclass inline
        return 0;
    }
};
```

Java creates an unnamed subclass of `Shape` on the fly and instantiates that — not `Shape` itself.

---

**Q4. Can an Interface have a constructor?**
No. Because:

- Constructor's job is to initialize instance fields
- Interfaces cannot have instance fields
- You can never do `new Chargeable()` — nothing to initialize
- Therefore a constructor makes no sense for an interface

---

**Q5. What is the Diamond Problem and how does Java solve it?**
Assume:

- `B` and `C` both extend `A`
- `A` has method `print()`
- `D` extends both `B` and `C`
- `D.print()` → which version runs? `B`'s or `C`'s? Ambiguous.

C++ solves this via scope resolution operator (`::`).
Java avoids it entirely by **not allowing multiple class inheritance**. A class can only `extend` one class. Multiple `implements` is allowed because interfaces (pre Java 8) had no method bodies — no ambiguity possible.

> Note: Java 8 `default` methods reintroduce potential ambiguity. Java resolves this by throwing a **compile error** forcing the implementing class to explicitly override the conflicting method.

---

**Q6. When would you choose an Abstract class over an Interface?**

| Use Abstract Class                  | Use Interface                             |
| ----------------------------------- | ----------------------------------------- |
| Classes share common fields         | No shared state needed                    |
| Classes share some common behaviour | Every method needs its own implementation |
| Strong IS-A relationship            | CAN-DO / capability relationship          |
| `HybridCar IS-A Car`                | `HybridCar CAN charge()`                  |

---

**Q7. What are `default` and `static` methods in interfaces? (Java 8+)**

**Problem before Java 8:**
Adding a new method to an interface broke every class implementing it — forced to implement the new method.

**`default` method — backward compatibility:**

```java
public interface Chargeable {
    void charge();

    default void checkBattery() {
        System.out.println("Checking battery..."); // has a body
    }
}
```

- Implementing classes get `checkBattery()` for free
- Can override if needed
- Existing implementations don't break

**`static` method — interface level utility:**

```java
public interface Chargeable {
    static int maxBatteryLevel() {
        return 100; // belongs to interface, not object
    }
}

// Called as:
Chargeable.maxBatteryLevel();
// Cannot be overridden
```

---

## Runnable Abstarction demo

```java
    public static void main(String[] args) {
        // half abstraction by abstarct class example
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(7, 4);

        System.out.println("displayArea call for: " + circle);
        circle.displayArea();

        System.out.println("displayArea call for: " + rectangle);
        rectangle.displayArea();
    }
```

---

## Runnable Interface demo

```java
    public static void main(String[] args) {
        // full abstraction by interface

        HybridCar hybridCar = new HybridCar("Mahindra", "Olieve", 140, 75);

        System.out.println("Calling displayInfo method for: " + hybridCar);
        hybridCar.displayInfo();

        System.out.println("Calling charge method for: " + hybridCar);
        hybridCar.charge();

        System.out.println("Calling displayInfo method again to check fuel level for: " + hybridCar);
        hybridCar.displayInfo();
    }
```
