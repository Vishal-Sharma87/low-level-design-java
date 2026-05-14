# Liskov Substitution Principle (LSP)

> "Objects of a subclass should be substitutable for objects of the parent class without breaking the program." — Barbara Liskov

## Definition

If `B` extends `A`, then everywhere `A` is used, `B` should work correctly — no exceptions thrown, no behavior broken, and no surprises for callers.

## The real meaning

LSP is a behavioural quality check on inheritance. When a child extends a parent, it makes an implicit promise: "I will honour everything my parent promised." LSP says that promise must never be broken.

## Two types of "is-a" — the core trap

| Type | Meaning | Example |
|------|---------|---------|
| Categorical is-a | Natural-language categorization | "Penguin is a Bird"; "Contract employee is an Employee" |
| Behavioural is-a | Can the child satisfy the parent's code contract? | Can `Penguin` implement `fly()` safely? |

LSP only cares about behavioural is-a. Real-world categories can mislead you into wrong inheritance hierarchies — English meaning doesn't automatically imply correct inheritance.

## How violations happen

The parent class advertises a contract via its method signatures; a child can break that contract by:

1. Throwing an exception for an inherited method

```java
class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly"); // breaks the contract
    }
}
```

2. Silently changing behaviour the caller depends on

```java
class ContractEmployee extends Employee {
    @Override
    public void applyLeave() {
        // does nothing (or throws) — caller expected this to work
    }
}
```

3. Forcing callers to check the concrete type before calling

```java
if (emp instanceof FullTimeEmployee) {
    emp.applyLeave(); // you shouldn't need to do this
}
```

The moment you write `instanceof` to decide whether a method is safe to call, the hierarchy is probably wrong.

## Examples

### Example 1 — Bird and Penguin

- Problem: `Penguin` extends `Bird` where `Bird` defines `fly()`. `Penguin.fly()` throws an exception and callers crash at runtime.
- Fix: Split the hierarchy into `FlyingBird` and `NonFlyingBird` so substitution is safe at compile time.

```
Bird
├─ FlyingBird -> Sparrow, Eagle, Parrot
└─ NonFlyingBird -> Penguin, Ostrich
```

### Example 2 — Employee and ContractEmployee

- Problem: `ContractEmployee` extends `Employee` where `Employee` has `applyLeave()`; `ContractEmployee.applyLeave()` throws or is a no-op and HR modules crash.
- Fix: Split `Employee` into a minimal base (name, salary) and a `LeaveEligibleEmployee` that contains leave-related behavior. Only eligible employees extend `LeaveEligibleEmployee`.

```
Employee (name, salary)
├─ LeaveEligibleEmployee (leavesLeft, applyLeave())
│  ├─ FullTimeEmployee
│  └─ InternEmployee
└─ ContractEmployee
```

## Warning signs of LSP violation

| Signal | What it tells you |
|--------|-------------------|
| Child throws `UnsupportedOperationException` | Child cannot honour parent's contract |
| Use of `instanceof` before calling a method | Hierarchy is wrong — type checks leaked into caller |
| Caller gets unexpected results when child is passed | Child silently changed behaviour |
| Overridden method does nothing or returns dummy value | Child is faking the contract |

## The fix strategy

When you spot an LSP violation, ask these questions in order:

1. Does every child fully honour every parent method?
   - If not: the parent models too much — split it.
2. What do *all* children genuinely share?
   - Keep only that in the base class.
3. What do only *some* children share?
   - Move that behavior into an intermediate class or interface; let only eligible children extend/implement it.

This results in clean, substitutable hierarchies.

## Connection to other SOLID principles

- **LSP + SRP**: Lean base classes (SRP) make it easier for all children to honour the contract (LSP).
- **LSP + OCP**: OCP encourages adding new behaviour via new classes; LSP ensures those new classes don't break existing callers.
- **LSP + ISP**: ISP is LSP applied to interfaces — don't force implementors to promise things they can't deliver.

## One line to remember

> If substituting a child for a parent surprises the caller — your inheritance is wrong.

## Illustrative code (bad vs good)

Bad (breaks LSP):

```java
// creating HR object to approve leave requests
HR hr = new HR("vishal", 150000, 10);

// leave request for an "Employee"
hr.processLeaveRequest(new Employee("Emp1", 10, 10));

// leave request for a "ContractEmployee" -> compiles, but fails at runtime
hr.processLeaveRequest(new ContractEmployee("CEmp1", 15000, 5));
```

Good (LSP-compliant):

```java
HR hr = new HR("Vishal", 1500000);

// Compile-time safety: only leave-eligible types are processed without surprises
hr.processLeaveRequest(new Employee("Not eligible Employee", 0));
hr.processLeaveRequest(new LeaveEligiableEmployee("eligible Employee", 0, 15));
```

---

Reference: Conceptual notes on LSP and common refactoring strategies.
