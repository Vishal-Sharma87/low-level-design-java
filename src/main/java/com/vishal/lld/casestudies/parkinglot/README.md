# Parking Lot — LLD Case Study

## Problem Statement

Design a parking lot system that:

- Supports multiple vehicle types — BIKE, CAR, TRUCK
- Assigns an available slot to an incoming vehicle
- Tracks parking duration via a ticket issued on entry
- Processes payment on exit using a pluggable billing strategy
- Frees the slot only after successful payment

---

## Entities and Responsibilities

### Enums

```
VehicleType       — BIKE, CAR, TRUCK (used for both vehicle and slot classification)
```

> `SlotType` was intentionally merged into `VehicleType` since the base design enforces 1-to-1
> vehicle-to-slot matching. Separate `SlotType` becomes necessary in v2 when overflow parking
> (e.g., BIKE parked in TRUCK slot) is introduced.

---

### Models

**`Vehicle`**

- Owns: `vehicleId` (auto-incremented), `vehicleType`
- Pure data class — no behavior, no dependencies

**`Slot`**

- Owns: `slotId` (auto-incremented), `vehicleType`
- Pure data class — no behavior, no dependencies
- Registration with `SlotManager` is the caller's responsibility (not the slot's)

**`ParkingTicket`**

- Owns: `slotId`, `vehicleId`, `vehicleType`, `arrivalTime`
- Created on entry, consumed on exit
- Passed to `BillingSystem` for fee calculation context

---

### Core Classes

**`SlotManager`** — Singleton

- Owns two maps: `unOccupiedSlotMap` and `occupiedSlotMap`, both keyed by `VehicleType`
- Inner sets use `LinkedHashSet<Integer>` for O(1) insert, O(1) removal, and predictable ordering
- `populateUnoccupiedSlot(VehicleType, slotId)` — called at startup to register slots
- `getAvailableSlot(VehicleType)` — removes from unoccupied, adds to occupied, returns slotId; throws if none available
- `freeSlot(VehicleType, slotId)` — moves slot back from occupied to unoccupied on exit

**`ParkingLot`** — Singleton

- Owns: `SlotManager` reference, `Map<Integer, ParkingTicket>` keyed by slotId
- Initialized once via `getInstance(List<VehicleType>, int totalSlots, BillingSystem)`
- Creates and registers all slots during initialization
- `park(Vehicle)` — gets available slot, creates ticket, stores in map, returns slotId
- `exit(int slotId, String paymentType)` — validates ticket, processes billing, frees slot, removes ticket

**`BillingSystem`**

- Stateless service — single instance
- `processBilling(PaymentStrategy, ParkingTicket)` — delegates payment to the strategy
- Slot is freed only if this method completes without exception

---

### Strategy Pattern — Payment

**`PaymentStrategy`** (interface)

- `pay()` — executes the payment
- `register()` — registers the strategy with `PaymentStrategyFactory`

> **TODO:** `register()` on the interface is a design smell — it couples each strategy to the
> factory. Refactor to self-registering via static initializer blocks or factory-side
> registration to remove this dependency.

**`PaymentStrategyFactory`**

- Holds `Map<String, PaymentStrategy>` — strategies registered by name
- `getPaymentStrategy(String name)` — returns strategy or throws `IllegalArgumentException`

> **TODO:** Key type should be an enum, not a `String`, to eliminate typo-based runtime failures.

**`UpiPaymentStrategy`**

- Implements `PaymentStrategy`
- Registered manually in `Main` before `ParkingLot` is initialized

---

## Flow

### Entry Flow

```
Vehicle arrives
  → ParkingLot.park(vehicle)
      → SlotManager.getAvailableSlot(vehicleType)       // throws if full
      → new ParkingTicket(slotId, vehicleId, vehicleType)
      → parkingTickets.put(slotId, ticket)
      → return slotId  // this is the ticket reference for exit
```

### Exit Flow

```
Vehicle exits
  → ParkingLot.exit(slotId, paymentType)
      → validate slotId exists in parkingTickets        // throws IllegalArgumentException if not
      → PaymentStrategyFactory.getPaymentStrategy(paymentType)
      → BillingSystem.processBilling(strategy, ticket)  // throws on payment failure
      → SlotManager.freeSlot(vehicleType, slotId)       // only reached if billing succeeds
      → parkingTickets.remove(slotId)
```

---

## Patterns Applied

| Pattern   | Where                               | Why                                                                                      |
| --------- | ----------------------------------- | ---------------------------------------------------------------------------------------- |
| Singleton | `SlotManager`, `ParkingLot`         | Both manage shared mutable state — multiple instances would cause slot double-assignment |
| Strategy  | `PaymentStrategy` + implementations | Fee processing algorithm varies per payment type without changing `BillingSystem`        |
| Factory   | `PaymentStrategyFactory`            | Decouples `ParkingLot` and `BillingSystem` from concrete strategy classes                |

---

## Gain vs Loss

### Gains

- **O(1) slot assignment and release** — `LinkedHashSet` per vehicle type eliminates iteration over all slots
- **Payment strategies are open for extension** — adding CashPaymentStrategy requires zero changes to `BillingSystem` or `ParkingLot`
- **Slot release is conditional** — slot is only freed after confirmed payment; no orphaned occupied slots
- **Single responsibility across classes** — `SlotManager` manages inventory, `ParkingLot` manages assignment, `BillingSystem` manages payment; each has one reason to change

### Losses

- **String-keyed strategy factory** — a typo in payment type causes a runtime exception, not a compile-time error; should be enum-keyed
- **Manual strategy registration** — caller must call `register()` before using the system; easy to forget, no enforcement
- **`pay()` takes no arguments** — strategy cannot calculate fee based on vehicle type or duration; sufficient for learning but incomplete for production
- **`totalSlots` is flat across vehicle types** — initialization assumes equal slot count per type; real lots have asymmetric distribution

---

## What Changes Under Different Requirements

| Requirement                           | What breaks                                 | Fix                                                              |
| ------------------------------------- | ------------------------------------------- | ---------------------------------------------------------------- |
| Overflow parking (BIKE in TRUCK slot) | 1-to-1 VehicleType assumption               | Separate `SlotType` from `VehicleType`, add compatibility matrix |
| Time-based billing                    | `pay()` has no ticket context               | Pass `ParkingTicket` into `pay()`                                |
| Multiple parking lots                 | Singleton assumption                        | Replace Singleton with instance-managed registry                 |
| Reserved slots                        | `SlotManager` has no concept of reservation | Add `reservedSlotMap` and reservation logic to `SlotManager`     |
