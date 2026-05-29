# Template Method Pattern

## Problem It Solves

Multiple classes follow the same algorithm skeleton but differ in one or more steps. Without Template Method, common steps are duplicated across every class — changing shared logic requires updating every class.

**Without Template Method — duplicated fetch and export logic:**

```java
public class CSVExporter {
    public void export() {
        // fetchData — same across all exporters
        System.out.println("Fetching data from DB...");
        // processData — different
        System.out.println("Processing as CSV...");
        // exportData — same across all exporters
        System.out.println("Exporting to S3...");
    }
}

public class PDFExporter {
    public void export() {
        // fetchData — duplicated
        System.out.println("Fetching data from DB...");
        // processData — different
        System.out.println("Processing as PDF...");
        // exportData — duplicated
        System.out.println("Exporting to S3...");
    }
}
```

**What is wrong:**

- `fetchData()` and `exportData()` duplicated across every exporter
- Tomorrow fetch logic changes — update every class — OCP violation
- No enforcement of step order — subclass can accidentally skip or reorder steps
- Common logic has no single home — scattered across classes

---

## Core Idea

> Define the skeleton of an algorithm in a base class — written once.  
> Let subclasses override only the steps that need to change.  
> The overall structure and order of steps stays fixed.

---

## You Already Used This Pattern

Throughout this pattern journey, Template Method appeared naturally multiple times:

**In Strategy pattern — BasePaymentStrategy:**

```java
public abstract class BasePaymentStrategy implements PaymentStrategy {

    protected String type;

    public BasePaymentStrategy(String type) {
        this.type = type;
    }

    // common step — written once, inherited by all strategies
    @Override
    public void register() {
        PaymentStrategyFactory.register(type, this);
    }

    // varying step — each strategy overrides this
    public abstract void pay();
}
```

```
register() → common step → base class owns it
pay()      → varying step → UpiStrategy, CardStrategy override it
```

**In Observer pattern — BaseOrderObserver:**

```java
public abstract class BaseOrderObserver implements OrderObserver {

    protected OrderObservable orderObservable;

    public BaseOrderObserver(OrderObservable orderObservable) {
        if (orderObservable == null)
            throw new IllegalArgumentException("OrderObservable cannot be null");
        this.orderObservable = orderObservable;
    }

    // common step — written once, inherited by all observers
    @Override
    public void register() {
        orderObservable.register(this);
    }

    // varying step — each observer overrides this
    public abstract void trigger();
}
```

```
register() → common step → base class owns it
trigger()  → varying step → EmailObserver, NotificationObserver override it
```

Template Method was the underlying structure making both of these work cleanly.

---

## The Classic Example — Data Exporter

```java
public abstract class DataExporter {

    // template method — skeleton fixed, final prevents subclass from changing order
    public final void export() {
        fetchData();    // common — same for all exporters
        processData();  // varies — subclass must override
        exportData();   // common — same for all exporters
    }

    // common step — written once
    private void fetchData() {
        System.out.println("Fetching data from DB...");
    }

    // varying step — subclass decides implementation
    protected abstract void processData();

    // common step — written once
    private void exportData() {
        System.out.println("Exporting to S3...");
    }
}
```

**Subclasses override only what changes:**

```java
public class CSVExporter extends DataExporter {
    @Override
    protected void processData() {
        System.out.println("Processing data as CSV...");
    }
}

public class PDFExporter extends DataExporter {
    @Override
    protected void processData() {
        System.out.println("Processing data as PDF...");
    }
}

public class ExcelExporter extends DataExporter {
    @Override
    protected void processData() {
        System.out.println("Processing data as Excel...");
    }
}
```

**Usage:**

```java
DataExporter csvExporter = new CSVExporter();
csvExporter.export();
// Fetching data from DB...
// Processing data as CSV...
// Exporting to S3...

DataExporter pdfExporter = new PDFExporter();
pdfExporter.export();
// Fetching data from DB...
// Processing data as PDF...
// Exporting to S3...
```

---

## Two Critical Keywords

**`final` on template method:**

```java
public final void export() { ... }
```

Subclasses cannot override `export()` — they cannot change the order of steps. Skeleton is locked. If a subclass could reorder steps, the whole purpose of Template Method is broken.

**`abstract` on varying step:**

```java
protected abstract void processData();
```

Subclasses are forced to implement it — cannot skip it. Compiler enforces the contract.

---

## Template Method vs Strategy

Both solve the problem of varying behavior — but differently:

```
Template Method → inheritance based
                  base class defines skeleton
                  subclass overrides steps
                  algorithm structure locked in base class

Strategy        → composition based
                  algorithm fully encapsulated in separate class
                  injected into service at runtime
                  algorithm can be swapped without changing service
```

```
Template Method → use when steps are fixed, only implementation varies
Strategy        → use when entire algorithm needs to be swappable at runtime
```

---

## Hook Methods

Template Method also supports **optional steps** called hooks — methods with a default empty implementation that subclasses can optionally override:

```java
public abstract class DataExporter {

    public final void export() {
        fetchData();
        preProcess();   // hook — optional step
        processData();
        exportData();
        postExport();   // hook — optional step
    }

    // hook — default does nothing, subclass overrides if needed
    protected void preProcess() { }
    protected void postExport() { }

    protected abstract void processData();

    private void fetchData() { System.out.println("Fetching..."); }
    private void exportData() { System.out.println("Exporting..."); }
}

// CSVExporter needs post export notification — overrides hook
public class CSVExporter extends DataExporter {
    @Override
    protected void processData() {
        System.out.println("Processing as CSV...");
    }

    @Override
    protected void postExport() {
        System.out.println("Notifying team after CSV export...");
    }
}
```

Hooks give subclasses flexibility to add behavior at specific points without making every step mandatory.

---

## Gain vs Loss

```
Gain:
  common logic written once in base class — no duplication
  step order enforced via final — subclasses cannot break the skeleton
  adding new exporter = new class only, base class untouched — OCP
  hooks provide optional extension points without forcing implementation

Loss:
  inheritance based — tight coupling between base and subclass
  hard to change skeleton — final method locks the order for all subclasses
  can lead to deep inheritance hierarchies if overused
  Strategy is often preferred over Template Method in modern design
    because composition is more flexible than inheritance
```

---

## Where Template Method Appears in LLD Case Studies

| Case Study         | Template Method Used For             |
| ------------------ | ------------------------------------ |
| Data export        | fetch → process → export skeleton    |
| Payment processing | validate → charge → notify skeleton  |
| Report generation  | gather → format → deliver skeleton   |
| Observer pattern   | register() common, trigger() varying |
| Strategy pattern   | register() common, pay() varying     |

---

## Interview Version to Explain

Show duplicated common steps across multiple classes — point out maintenance risk.  
Introduce abstract base class with final template method — skeleton locked.  
Abstract method for varying step — subclass forced to implement.  
Connect to what you already built — BasePaymentStrategy and BaseOrderObserver are Template Method in practice.  
Mention hooks for optional steps — shows depth beyond the basic pattern.  
Compare with Strategy — Template Method uses inheritance, Strategy uses composition.
