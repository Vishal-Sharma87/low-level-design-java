# State Pattern

## Problem It Solves

An object's behavior changes based on its internal state. Without State pattern, every method has growing if-else blocks checking current state — OCP violated every time a new state is added.

**Without State — if-else in every method:**

```java
public class VendingMachine {

    private MachineState state = MachineState.IDLE;

    public void insertCoin() {
        if (state == MachineState.IDLE) {
            state = MachineState.HAS_COIN;
        } else if (state == MachineState.HAS_COIN) {
            System.out.println("Coin already inserted");
        } else if (state == MachineState.ITEM_SELECTED) {
            System.out.println("Item already selected");
        }
        // new state OUT_OF_STOCK → add else if here
    }

    public void selectItem() {
        if (state == MachineState.IDLE) {
            System.out.println("Insert coin first");
        } else if (state == MachineState.HAS_COIN) {
            state = MachineState.ITEM_SELECTED;
        } else if (state == MachineState.ITEM_SELECTED) {
            System.out.println("Item already selected");
        }
        // new state OUT_OF_STOCK → add else if here too
    }

    public void dispense() {
        if (state == MachineState.IDLE) {
            System.out.println("Insert coin first");
        } else if (state == MachineState.HAS_COIN) {
            System.out.println("Select item first");
        } else if (state == MachineState.ITEM_SELECTED) {
            System.out.println("Dispensing item...");
            state = MachineState.IDLE;
        }
        // new state OUT_OF_STOCK → add else if here too
    }
}
```

**What is wrong:**

- Every method has if-else checking current state
- Adding new state `OUT_OF_STOCK` requires adding else-if in every method — OCP violation
- `VendingMachine` knows behavior of every state — SRP violation
- Hard to read — behavior of one state scattered across multiple methods

---

## Core Idea

> Instead of the machine checking its state and deciding behavior —  
> the state object itself **is** the behavior.  
> Machine delegates every action to its current state object.

---

## The Structure

```
VendingMachineState    ← interface — insertCoin(), selectItem(), dispense()
IdleState              ← implements VendingMachineState
HasCoinState           ← implements VendingMachineState
DispenseState          ← implements VendingMachineState
VendingMachine         ← holds currentState, delegates all actions to it
                         exposes setState() for state transitions
                         exposes getters so states can transition to each other
```

---

## The Implementation

**State interface — contract for all states:**

```java
public interface VendingMachineState {
    void insertCoin();
    void selectItem();
    void dispense();
}
```

**VendingMachine — holds state, delegates, exposes transitions:**

```java
public class VendingMachine {

    private VendingMachineState currentState;
    private VendingMachineState idleState;
    private VendingMachineState hasCoinState;
    private VendingMachineState dispenseState;

    public VendingMachine() {
        // all states created once — reused across transitions, no object creation overhead
        idleState     = new IdleState(this);
        hasCoinState  = new HasCoinState(this);
        dispenseState = new DispenseState(this);
        currentState  = idleState; // machine starts in idle
    }

    public void setState(VendingMachineState state) {
        if (state == null)
            throw new IllegalArgumentException("VendingMachineState cannot be null");
        currentState = state;
    }

    // getters — states use these to transition without creating new objects
    public VendingMachineState getIdleState()     { return idleState; }
    public VendingMachineState getHasCoinState()  { return hasCoinState; }
    public VendingMachineState getDispenseState() { return dispenseState; }

    public VendingMachineState getCurrentState()  { return currentState; }

    // all actions delegated to current state — zero if-else
    public void insertCoin() { currentState.insertCoin(); }
    public void selectItem() { currentState.selectItem(); }
    public void dispense()   { currentState.dispense(); }
}
```

**IdleState — behavior when machine is waiting:**

```java
public class IdleState implements VendingMachineState {

    private VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("[Idle] Coin inserted — moving to HasCoin state");
        machine.setState(machine.getHasCoinState());
    }

    @Override
    public void selectItem() {
        System.out.println("[Idle] Insert coin first");
    }

    @Override
    public void dispense() {
        System.out.println("[Idle] Insert coin first");
    }
}
```

**HasCoinState — behavior when coin is inserted:**

```java
public class HasCoinState implements VendingMachineState {

    private VendingMachine machine;

    public HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("[HasCoin] Coin already inserted — select item");
    }

    @Override
    public void selectItem() {
        System.out.println("[HasCoin] Item selected — moving to Dispense state");
        machine.setState(machine.getDispenseState());
    }

    @Override
    public void dispense() {
        System.out.println("[HasCoin] Select item first");
    }
}
```

**DispenseState — behavior when item is selected:**

```java
public class DispenseState implements VendingMachineState {

    private VendingMachine machine;

    public DispenseState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("[Dispense] Item already selected — dispensing shortly");
    }

    @Override
    public void selectItem() {
        System.out.println("[Dispense] Item already selected");
    }

    @Override
    public void dispense() {
        System.out.println("[Dispense] Dispensing item — moving back to Idle state");
        machine.setState(machine.getIdleState());
    }
}
```

---

## Usage

```java
VendingMachine vendingMachine = new VendingMachine();

vendingMachine.dispense();     // [Idle] Insert coin first
vendingMachine.insertCoin();   // [Idle] Coin inserted — moving to HasCoin state
vendingMachine.insertCoin();   // [HasCoin] Coin already inserted — select item
vendingMachine.selectItem();   // [HasCoin] Item selected — moving to Dispense state
vendingMachine.dispense();     // [Dispense] Dispensing item — moving back to Idle state
```

---

## Adding New State — OCP Respected

Adding `OutOfStockState`:

```java
public class OutOfStockState implements VendingMachineState {

    @Override
    public void insertCoin() {
        System.out.println("[OutOfStock] Machine is out of stock — refund initiated");
    }

    @Override
    public void selectItem() {
        System.out.println("[OutOfStock] Machine is out of stock");
    }

    @Override
    public void dispense() {
        System.out.println("[OutOfStock] Machine is out of stock");
    }
}
```

Changes needed:

- New class `OutOfStockState` 
- Add field and getter in `VendingMachine` 
- Zero changes to existing states 
- Zero changes to `VendingMachine` action methods 

---

## Adding New Action — Default Method Approach

Adding `refund()` action:

```java
public interface VendingMachineState {
    void insertCoin();
    void selectItem();
    void dispense();

    // default — only states that support refund override this
    default void refund() {
        System.out.println("Refund not supported in current state");
    }
}
```

Only `HasCoinState` overrides it — others get default behavior. No forced implementation across all states.

---

## Why States Are Created Once in VendingMachine

```java
// Option A — created once, reused (current approach)
idleState = new IdleState(this);
machine.setState(machine.getIdleState()); // reuse existing object

// Option B — new object on every transition
machine.setState(new IdleState(machine)); // new object every time
```

For a finite known set of states — Option A is cleaner. No object creation overhead on every transition. All states share the same machine reference.

---

## Gain vs Loss

```
Gain:
  zero if-else in VendingMachine — pure delegation to current state
  adding new state = new class only — OCP respected
  each state owns its own behavior — SRP respected
  state transitions explicit and traceable — easy to debug

Loss:
  more classes — one class per state
  states hold machine reference — tight coupling between state and machine
  adding new action requires updating every state class — interface change
  default methods soften this but add interface complexity
```

---

## Where State Appears in LLD Case Studies

| Case Study      | State Used For                                          |
| --------------- | ------------------------------------------------------- |
| Vending machine | Idle, HasCoin, Dispense, OutOfStock states              |
| ATM machine     | Idle, CardInserted, PinEntered, Dispensing states       |
| Traffic light   | Red, Yellow, Green states with auto transition          |
| Order lifecycle | Placed, Confirmed, Shipped, Delivered, Cancelled states |

---

## Interview Version to Write

Show if-else in every method — point out OCP violation on every new state.  
Introduce `VendingMachineState` interface — each state owns its own behavior.  
Show `VendingMachine` delegating all actions — zero if-else.  
Show states transitioning via machine's getters — no new object creation.  
Demonstrate adding `OutOfStockState` — new class only, zero existing changes.  
Mention default methods for new actions — shows awareness of ISP concern.
