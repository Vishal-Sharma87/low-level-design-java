# Elevator System — LLD Case Study

## Problem Statement

Design an elevator system for a building with multiple elevators and floors that:

- Handles hall requests — user presses UP/DOWN on a floor panel
- Handles cabin requests — user presses a destination floor inside an elevator
- Assigns the most suitable elevator to each request using a pluggable strategy
- Moves elevators floor by floor, picking up requests along the way
- Tracks elevator position and state changes in real time

---

## Entities and Responsibilities

### Enums

```
Direction     — UP, DOWN (direction of user's intended travel)
Status        — IDLE, MOVING_UP, MOVING_DOWN (current elevator status for strategy evaluation)
```

---

### Core Class — `Elevator`

- Owns: `elevatorId` (auto-incremented), `currentFloor`, `currentState`, `elevatorStatus`, `upQueue`, `downQueue`, `elevatorObservers`
- Implements `Runnable` — each elevator runs its own thread, drives its own movement loop
- `run()` — continuous loop calling `currentState.handle(this)` until interrupted
- `addFloorToQueue(int floor)` — derives direction from `currentFloor` vs destination; adds to `upQueue` or `downQueue`; calls `notify()` to wake idle elevator
- `moveUp()` / `moveDown()` — increments/decrements `currentFloor`, notifies all observers, sleeps 200ms to simulate real movement
- `updateStatusAndState(ElevatorState, Status)` — single method to keep state and status in sync; prevents drift between the two fields
- `registerObserver(ElevatorObserver)` — adds observer to notification list

**Queue design:**

- `upQueue` — `PriorityBlockingQueue` with natural ordering (min heap); floors processed ascending
- `downQueue` — `PriorityBlockingQueue` with `Comparator.reverseOrder()` (max heap); floors processed descending
- Direction is derived at enqueue time from `currentFloor` vs destination — not from the user's requested direction. This prevents a bug where an elevator at floor 0 would receive a DOWN request for floor 5 and never move upward to reach it.

**CPU efficiency:**

- When both queues are empty, `IdleState` calls `elevator.wait()` inside `synchronized(elevator)` — releases CPU, blocks thread
- `addFloorToQueue()` calls `this.notify()` after enqueue — wakes the idle elevator thread
- Each elevator has its own monitor, so three elevators block and wake independently

---

### State Pattern — `ElevatorState`

Interface with single method: `void handle(Elevator elevator)`

**`IdleState`**

- Checks `upQueue` first, then `downQueue`
- If a queue has entries — determines direction by comparing `currentFloor` to queue head, transitions to `MovingUpState` or `MovingDownState`
- If both queues empty — calls `elevator.wait()` to block until a request arrives

**`MovingUpState`**

- If `upQueue` empty → transition to `IdleState`
- If `currentFloor == upQueue.peek()` → arrived; poll destination; transition to `IdleState` if queue now empty
- If `currentFloor == 10` (top floor) → transition to `IdleState`
- Otherwise → `elevator.moveUp()`
- Arrival and movement are mutually exclusive per `handle()` call — one step at a time

**`MovingDownState`**

- Mirror of `MovingUpState` for downward movement
- If `currentFloor == 0` (ground floor) → transition to `IdleState`

> Each state delegates execution back to `Elevator` (`moveUp`, `moveDown`, `updateStatusAndState`).
> State decides **what** to do; `Elevator` decides **how** to do it.

---

### Strategy Pattern — `ElevatorSelectionStrategy`

Interface: `Elevator select(List<Elevator> elevators, Direction direction, int destination)`

**`NearestElevatorStrategy`**
Calculates cost per elevator based on current status and direction:

| Elevator Status | Request Direction | Condition                  | Cost Formula                               |
| --------------- | ----------------- | -------------------------- | ------------------------------------------ |
| MOVING_UP       | UP                | elevator below destination | `abs(destination - currentFloor)`          |
| MOVING_UP       | UP                | elevator above destination | `(10 - currentFloor) + destination`        |
| MOVING_UP       | DOWN              | any                        | `(10 - currentFloor) + (10 - destination)` |
| MOVING_DOWN     | DOWN              | elevator above destination | `abs(destination - currentFloor)`          |
| MOVING_DOWN     | DOWN              | elevator below destination | `currentFloor + destination`               |
| MOVING_DOWN     | UP                | any                        | `currentFloor + destination`               |
| IDLE            | any               | any                        | `abs(destination - currentFloor)`          |

Returns elevator with minimum cost.

> Strategy receives only `List<Elevator>`, `Direction`, and `destination` — no hidden dependencies.
> All information needed for selection is passed explicitly by `ElevatorAssignmentService`.

---

### Observer Pattern — `ElevatorObserver`

Interface: `void track(int elevatorId, int currentFloor, Status elevatorStatus)`

**`ElevatorTracker`** — Singleton

- Holds `Map<Integer, Elevator>` keyed by `elevatorId` — full references used by assignment service
- `track()` — prints floor and status on each elevator movement
- `getElevators()` — returns elevator list for strategy evaluation
- Registered as observer on each elevator at startup

---

### Panels

**`HallPanel`**

- Constructed with `ElevatorAssignmentService` and `ElevatorSelectionStrategy`
- `up(int floor)` / `down(int floor)` — calls assignment service with direction and floor; returns void
- User's direction signal is passed to strategy for optimal elevator selection but does not dictate queue insertion

**`ElevatorCabinPanel`**

- Constructed with its `Elevator` — final reference, one panel per elevator
- `pressButton(int destinationFloor)` — guards same-floor press; derives direction from `currentFloor` vs destination; calls `elevator.addFloorToQueue()`

---

### Service — `ElevatorAssignmentService`

- Constructed with `ElevatorTracker`
- `assignElevator(Direction, floor, ElevatorSelectionStrategy)` — fetches elevator list from tracker, delegates to strategy, adds floor to selected elevator's queue
- Prints assignment log for observability

---

## Startup Sequence

```
1. Create Elevator instances (each starts at floor 0, IdleState)
2. Create ElevatorTracker.getInstance(elevatorList) — registers elevators in map
3. elevator.registerObserver(elevatorTracker) — each elevator notifies tracker on movement
4. Start elevator threads — thread1.start(), thread2.start(), thread3.start()
5. Create ElevatorAssignmentService(elevatorTracker)
6. Create HallPanel(assignmentService, selectionStrategy)
7. Simulate requests via hallPanel.up() / hallPanel.down()
```

---

## Flow

### Hall Request — user presses UP on floor 5

```
HallPanel.up(5)
  → ElevatorAssignmentService.assignElevator(UP, 5, strategy)
      → ElevatorTracker.getElevators()
      → NearestElevatorStrategy.select(elevators, UP, 5) → returns best elevator
      → elevator.addFloorToQueue(5)
          → derives direction: currentFloor <= 5 → upQueue.offer(5)
          → notify() — wakes elevator thread if idle
```

### Elevator Movement Loop

```
Elevator.run() — continuous loop
  → currentState.handle(this)
      IdleState       → checks queues → transitions to MovingUpState or MovingDownState
      MovingUpState   → move one floor up → notify observers → check arrival
      MovingDownState → move one floor down → notify observers → check arrival
  → ElevatorTracker.track() fires on each moveUp() / moveDown() call
```

---

## Patterns Applied

| Pattern   | Where                                                             | Why                                                                                                                    |
| --------- | ----------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| State     | `ElevatorState` + `IdleState`, `MovingUpState`, `MovingDownState` | Elevator behavior differs per state; avoids if-else chains inside `Elevator.run()`                                     |
| Strategy  | `ElevatorSelectionStrategy` + `NearestElevatorStrategy`           | Selection algorithm is pluggable; SCAN or other algorithms can be swapped without changing `ElevatorAssignmentService` |
| Observer  | `ElevatorObserver` + `ElevatorTracker`                            | Decouples movement logic from tracking; multiple observers can be registered without changing `Elevator`               |
| Singleton | `ElevatorTracker`                                                 | Single source of truth for all elevator references                                                                     |

---

## Gain vs Loss

### Gains

- **Independent elevator threads** — each elevator drives itself; no central scheduler blocking others
- **CPU-efficient idle blocking** — `wait()`/`notify()` on the elevator monitor; idle elevators consume zero CPU
- **Ordered queues** — min/max heap per direction ensures elevators process floors in travel order, not insertion order
- **Direction-independent enqueue** — `addFloorToQueue` derives direction from position, not user input; prevents the bug where a DOWN request forces an elevator to never reach its pickup floor
- **Pluggable selection** — `NearestElevatorStrategy` can be replaced with SCAN, load-aware, or zone-based strategies without touching any other class
- **Single state+status update** — `updateStatusAndState()` keeps `ElevatorState` and `Status` enum in sync atomically; no partial state inconsistency

### Losses

- **Floor bounds hardcoded** — top floor (10) and ground floor (0) are magic numbers in state classes; should be configurable per building
- **No cabin request direction handling** — cabin requests for floors opposite to current travel direction are added to the wrong queue and processed only after the elevator reverses
- **Strategy receives full Elevator references** — strategy can technically call `addFloorToQueue()` or mutate elevator state; should receive a read-only view
- **ElevatorTracker is a Singleton** — makes multi-building scenarios impossible without refactoring
- **No door open/close simulation** — arrival is logged but no door state modeled; relevant for systems where doors must close before movement resumes

---

## What Changes Under Different Requirements

| Requirement                                        | What breaks                             | Fix                                                                           |
| -------------------------------------------------- | --------------------------------------- | ----------------------------------------------------------------------------- |
| Configurable floor bounds                          | Magic numbers in state classes          | Pass `buildingConfig` (minFloor, maxFloor) into states                        |
| Cabin request for opposite direction               | Wrong queue, processed after reversal   | Add mid-journey queue swap logic or a pending queue                           |
| Zone-based assignment (floors 1-5 → L1, 6-10 → L2) | `NearestElevatorStrategy` ignores zones | Add `ZoneStrategy` implementing `ElevatorSelectionStrategy`                   |
| Multiple buildings                                 | `ElevatorTracker` Singleton             | Replace Singleton with instance per building, managed by a `BuildingRegistry` |
| Load-aware assignment                              | Strategy uses only floor distance       | Add queue size to cost formula in strategy                                    |
