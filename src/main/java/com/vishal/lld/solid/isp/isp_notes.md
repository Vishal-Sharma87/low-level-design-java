# Interface Segregation Principle (ISP)

## Definition

> "A class should not be forced to implement interfaces it does not use."

Do not create one large interface and force every class to implement all of it.

Instead:

- Break interfaces into smaller focused interfaces.
- Ensure each class only implements what is actually relevant to it.

---

# The Real Meaning

ISP is about **honest contracts**.

When a class implements an interface, it is making a promise:

> "I can do everything this interface defines."

ISP says:

> Never put a class in a position where it has to make a promise it cannot keep.

---

# Connection to LSP

| LSP                                                    | ISP                                                            |
| ------------------------------------------------------ | -------------------------------------------------------------- |
| Child class forced to inherit methods it cannot honour | Implementing class forced to implement methods it doesn't need |
| Problem at the inheritance level                       | Problem at the interface level                                 |
| Fix → restructure class hierarchy                      | Fix → split the interface                                      |

Same root cause, different layer.

If you see an `UnsupportedOperationException` in a codebase, it is almost always an LSP or ISP violation.

---

# How Violations Happen

## Signal 1 — `UnsupportedOperationException`

```java
class RobotWorker implements WorkerActions {
    public void eat() {
        throw new UnsupportedOperationException("Robots don't eat");
    }
}
```

---

## Signal 2 — Empty Method Body Just to Satisfy Compiler

```java
class BasicPrinter implements Machine {
    public void fax() {
        // do nothing — forced to implement but doesn't apply
    }
}
```

---

## Signal 3 — Adding a Method Breaks Unrelated Classes

You add:

```java
void exportToPDF()
```

to a `Reportable` interface and suddenly 12 classes that only generate CSV reports are broken.

---

## Signal 4 — Fat Interface Naming Smell

Names like:

- `Manager`
- `Handler`
- `Processor`
- `Helper`

are often red flags.

They usually indicate the interface is doing too many things.

---

# Examples Covered

# Example 1 — Worker System

## Fat Interface

```java
WorkerActions → work(), attendMeeting(), eat(), sleep(), claimExpenses()
```

`RobotWorker` is forced to fake:

- `eat()`
- `sleep()`
- `claimExpenses()`

---

## Fix — Split by Capability

```java
Workable    → work(), attendMeeting()
Biological  → eat(), sleep()
Claimable   → claimExpenses()
```

```java
HumanEmployee  implements Workable, Biological, Claimable
RobotWorker    implements Workable
```

---

# Example 2 — Printer System

## Fat Interface

```java
Machine → print(), scan(), fax(), photocopy()
```

`BasicPrinter` is forced to fake:

- `scan()`
- `fax()`
- `photocopy()`

---

## Fix — Split by Capability

```java
Printable      → print()
Scannable      → scan()
Faxable        → fax()
Photocopiable  → photocopy()
```

```java
BasicPrinter     implements Printable
AllInOnePrinter  implements Printable, Scannable, Faxable, Photocopiable
```

---

# How to Split an Interface — The Thought Process

When you have a fat interface, ask:

## 1. Can I Describe This Interface in One Phrase?

If the answer is **no**, the interface is likely doing too many things.

---

## 2. Group Methods by Capability

Ask:

- Which methods naturally belong together?
- Which methods represent one behaviour/capability?

Each group becomes its own interface.

---

## 3. Would a Future Implementing Class Need to Fake Any Method?

If yes:

> The interface needs splitting before that class is added.

---

## 4. Does Each Interface Represent One Distinct Capability?

If yes, you are done.

---

# ISP in Java Standard Library

Java's own APIs heavily follow ISP.

| Instead of One Fat Interface   | Java Gives You                                               |
| ------------------------------ | ------------------------------------------------------------ |
| One giant stream interface     | `Readable`, `Writable`, `Closeable` separately               |
| One giant collection interface | `Iterable → Collection → List` in layers                     |
| One giant IO class             | `InputStream`, `OutputStream`, `Reader`, `Writer` separately |

This is why you can write a class that is only `Iterable` without being forced to implement the entire `List` contract.

---

# Access Modifiers Inside Interfaces — Quick Recap

| Member          | Default               | Changeable       |
| --------------- | --------------------- | ---------------- |
| Abstract method | `public abstract`     | No               |
| Default method  | `public`              | No               |
| Static method   | `public`              | No               |
| Private method  | `private`             | Already explicit |
| Fields          | `public static final` | No               |

## Example — Bad vs Good (code)

Below are compact Java examples that demonstrate an ISP violation (bad, fat interface)
and an ISP-compliant refactor (good, segregated interfaces).

### Bad — Fat Interface (ISP violation)

```java
// Fat interface forcing unrelated methods onto all implementors
interface WorkerActions {
    void work();
    void attendMeeting();
    void eat();
    void sleep();
    void claimExpenses();
}

class RobotWorker implements WorkerActions {
    @Override public void work() { /* robot work */ }
    @Override public void attendMeeting() { /* attends meetings */ }

    @Override
    public void eat() {
        throw new UnsupportedOperationException("Robots don't eat");
    }

    @Override
    public void sleep() {
        throw new UnsupportedOperationException("Robots don't sleep");
    }

    @Override
    public void claimExpenses() {
        throw new UnsupportedOperationException("Robots don't claim expenses");
    }
}
```

### Good — Segregated Interfaces (ISP-compliant)

```java
// Split interfaces by capability
interface Workable {
    void work();
    void attendMeeting();
}

interface Biological {
    void eat();
    void sleep();
}

interface Claimable {
    void claimExpenses();
}

class HumanWorker implements Workable, Biological, Claimable {
    @Override public void work() { /* human work */ }
    @Override public void attendMeeting() { /* attends meetings */ }
    @Override public void eat() { /* eats */ }
    @Override public void sleep() { /* sleeps */ }
    @Override public void claimExpenses() { /* claims expenses */ }
}

class RobotWorker implements Workable {
    @Override public void work() { /* robot work */ }
    @Override public void attendMeeting() { /* attends meetings */ }
}
```

// Note: prefer small, focused interfaces so implementations never need to
// throw UnsupportedOperationException or provide empty method bodies.

Splitting interfaces mirrors how you would split a class under SRP.

---

## ISP + DIP

When dependencies are injected via interfaces:

- lean interfaces make swapping implementations easier
- testing becomes simpler
- mocking becomes cleaner

Fat interfaces make all of these painful.

---

# One Line to Remember

> Many small focused interfaces beat one large bloated interface.
>
> Each interface should represent one capability, not a collection of unrelated abilities.

---

# Good vs Bad code main method

```java
    public static void main(String[] args) {
        // both HumenWorker and RobotWorker implements WorkerEmployee interface
        HumenWorker badHumenWorker = new HumenWorker();
        RobotWorker badRobotWorker = new RobotWorker();

        // human worker methods
        badHumenWorker.work();
        badHumenWorker.attendMeeting();
        badHumenWorker.eat();
        badHumenWorker.sleep();
        badHumenWorker.claimExpenses();

        // robot worker methods
        badRobotWorker.work();
        badRobotWorker.attendMeeting();

        // eat() method will throw "UnsupportedOperationException"
        badRobotWorker.eat();
        // these will also be throw "UnsupportedOperationException"
        badRobotWorker.sleep();
        badRobotWorker.claimExpenses();
    }
```

```java
    public static void main(String[] args) {
        // Humen worker -> implements "Workable", "Biological" and "Claimable"
        // thus gets all methods
        HumenWorker goodHumenWorker = new HumenWorker();

        // Robot worker -> implements only "Workable" interface
        // thus gets only work related methods
        RobotWorker goodRobotWorker = new RobotWorker();

        goodHumenWorker.work();
        goodHumenWorker.attendMeeting();
        goodHumenWorker.eat();
        goodHumenWorker.sleep();
        goodHumenWorker.claimExpenses();

        goodRobotWorker.work();
        goodRobotWorker.attendMeeting();
    }
```
