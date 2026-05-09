# Inheritance & Method Overriding — Interview Q&A

---

**Q1. What is Inheritance and why do we use it?**
Inheritance is an OOP concept where a child class inherits fields and methods of a parent class using the `extends` keyword. It reduces code redundancy — common fields and behaviour are written once in the parent and reused or modified by child classes as needed.

---

**Q2. What is the difference between `super()` and `super.method()`?**

- `super()` → calls the **parent class constructor**. Must be the first line in the child constructor.
- `super.method()` → calls a **specific method** of the parent class from inside the child class. Used when the child has overridden that method but still wants to reuse the parent's logic.

---

**Q3. Can a child class access `private` fields of the parent directly?**
No. `private` fields are not inherited — child classes cannot access them directly. However, the fields still exist in the object and can be accessed indirectly through `public` getters and setters inherited from the parent.

---

**Q4. What is Method Overriding? What are its rules?**
Method Overriding is a runtime polymorphism concept where a child class provides its own implementation of a method already defined in the parent class.

Rules:

- Same method name
- Same parameter list
- Same or covariant return type
- Cannot reduce the access modifier (e.g. `public` → `private` not allowed)
- Cannot override `final` or `static` methods

---

**Q5. What is the difference between Method Overriding and Method Overloading?**

|             | Overriding             | Overloading                          |
| ----------- | ---------------------- | ------------------------------------ |
| Type        | Runtime polymorphism   | Compile-time polymorphism            |
| Class       | Parent and child class | Same class                           |
| Method name | Same                   | Same                                 |
| Parameters  | Same                   | Must differ (number, type, or order) |
| Return type | Same or covariant      | Not a differentiator                 |
| Purpose     | Custom logic per child | Multiple versions of same method     |

> Note: Return type alone is **not enough** to overload a method. Only the parameter list must differ.

---

**Q6. What does the `@Override` annotation do? Is it mandatory?**
`@Override` is written in the child class above the overriding method. It tells the compiler to verify that this method is actually overriding a parent method. If the method name is mistyped, the compiler catches it immediately. Without `@Override`, a mistyped method silently becomes a new method — a hard bug to find. It is not mandatory but is strongly recommended as good practice.

---

**Q7. Can you override a `static` method in Java?**
No. Static methods belong to the class, not to an instance, so they cannot be overridden. What appears to be overriding a static method is actually **method hiding** — a different concept where the version called depends on the **reference type**, not the object type at runtime.

---

## Runnable Main class

```java
    public static void main(String[] args) {
        // car object
        Car coupe = new Car("Mercedes", "Blue", 120);

        // eletric car object
        ElectricCar m440i = new ElectricCar("BMW", "Black", 150, 15);

        // Displaing info for Car object
        System.out.println("Displaying Car info for: " + coupe);
        coupe.displayInfo();

        // Displaing info for Electric car object
        System.out.println("Displaying ElectriCar info for: " + m440i);
        m440i.displayInfo();

        // charging electric car
        m440i.charge();
    }
```
