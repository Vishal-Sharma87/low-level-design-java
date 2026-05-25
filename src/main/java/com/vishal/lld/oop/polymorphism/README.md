# Polymorphism — Interview Q&A

---

**Q1. What is Polymorphism? What are its two types?**
Polymorphism means one reference type behaving differently based on the actual object it holds at runtime. Same method call, multiple behaviours depending on the actual object.

```java
Car car1 = new Car("BMW", "Black", 120);
Car car2 = new ElectricCar("Tesla", "White", 200, 90);

car1.displayInfo(); // Car's version
car2.displayInfo(); // ElectricCar's version — same call, different behaviour
```

Types:

- **Compile-time polymorphism** → Method Overloading. Compiler decides which method to call at compile time.
- **Runtime polymorphism** → Method Overriding. JVM decides which implementation to run at runtime based on actual object type.

---

**Q2. What is the difference between compile-time and runtime polymorphism?**

|                    | Compile-time       | Runtime           |
| ------------------ | ------------------ | ----------------- |
| Also called        | Static binding     | Dynamic binding   |
| Resolved by        | Compiler           | JVM at runtime    |
| Achieved via       | Method Overloading | Method Overriding |
| Inheritance needed | No                 | Yes               |

---

**Q3. What is Dynamic Method Dispatch?**
The mechanism by which a call to an overridden method is resolved at **runtime** based on the actual object type, not the reference type.

```java
Car car = new ElectricCar("Tesla", "White", 200, 90);
car.displayInfo(); // JVM checks actual object (ElectricCar) at runtime and calls its version
```

The reference type is `Car` but JVM dispatches the call to `ElectricCar`'s `displayInfo()` dynamically. This is dynamic method dispatch.

---

**Q4. If a parent reference holds a child object, which methods can you call on it?**
Only methods defined in the **parent class** (reference type). The compiler only sees the reference type. Even if the actual object has extra methods, they are not accessible through the parent reference.

```java
Car car = new ElectricCar("Tesla", "White", 200, 90);
car.displayInfo(); //    defined in Car
car.charge();      //   compile error — charge() not in Car
```

---

**Q5. What is the difference between `instanceof` and casting? When would you use them?**

**Blind casting — dangerous:**

```java
Car car = new Car("BMW", "Black", 120);
ElectricCar ec = (ElectricCar) car; //   ClassCastException at runtime
```

Compiler allows it but JVM crashes at runtime.

**Old style — safe but verbose:**

```java
if (car instanceof ElectricCar) {
    ElectricCar ec = (ElectricCar) car; // check then cast separately
    ec.charge(); //
}
```

**Modern Java 16+ — pattern matching — cleanest:**

```java
if (car instanceof ElectricCar ec) { // check + cast in one line
    ec.charge(); //    ec is already cast and ready
}
```

| Aspect                  | Blind Cast              | Old `instanceof`          | Pattern Matching `instanceof` |
| ----------------------- | ----------------------- | ------------------------- | ----------------------------- |
| Checks Type Before Cast | No                      | Yes                       | Yes                           |
| Separate Cast Needed    | Yes                     | Yes                       | No                            |
| Readability             | Poor                    | Moderate                  | High                          |
| Boilerplate Code        | High                    | Moderate                  | Low                           |
| Risk                    | `ClassCastException`    | Safe                      | Safe                          |
| Type Safety             | Low                     | High                      | High                          |
| Performance             | Normal                  | Normal                    | Normal                        |
| Java Version            | All                     | All                       | Java 16+                      |
| Typical Syntax          | `(Dog) animal`          | `if (obj instanceof Dog)` | `if (obj instanceof Dog d)`   |
| Best Use Case           | When type is guaranteed | Traditional safe checking | Modern concise polymorphism   |

---

**Q6. Can polymorphism work without inheritance?**

- **Runtime polymorphism** → No. Requires inheritance (or interfaces) to override methods.
- **Compile-time polymorphism** → Yes. Method overloading works within a single class with no inheritance at all.

---

## Bonus — Primitive Casting vs Object Casting

**Primitive casting — converts the value:**

```java
// Widening — automatic, no data loss
int a = 10;
float b = a; //    automatic

// Narrowing — manual, data loss possible
float x = 9.99f;
int y = (int) x; //    y = 9, decimal lost
```

Widening order:

```
byte → short → int → long → float → double
```

Left to right = automatic. Right to left = explicit cast required.

**Key difference:**

- Primitive casting → **converts the value** from one type to another
- Object casting → does **not** convert anything, just changes how Java **views** the same object in memory
- `ClassCastException` can never happen with primitives — worst case is data loss

---

## Runnable Main method

```java
    public static void main(String[] args) {

        // Parent reference and children object
        // enforces to use Parent methods but child's implementation
        Car hybridCar = new ElectricCar("Tesla", "blach", 200, 90);

        System.out.println("Calling display info for: " + hybridCar);
        hybridCar.displayInfo(); // ElectricCar's displayInfo will be called
    }
```
