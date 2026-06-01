# Design Patterns — Learning Approach & Remaining Patterns

---

## How We Learned Each Pattern

Every pattern was built using the same progressive approach. Never theory first — always problem first.

### The Approach

```
Step 1 — Problem First
         Start with a real scenario — notification system, payment system, vending machine
         Ask what goes wrong with the naive solution
         Never reveal the pattern name until the problem is felt

Step 2 — Follow Up Questions
         Ask targeted questions instead of dumping knowledge
         Let the learner arrive at the solution themselves
         Correct the direction only when the thinking goes off track

Step 3 — Build Incrementally
         Start with the broken version — naive, no abstraction
         Fix one problem at a time
         Each version fixes exactly one issue from the previous

Step 4 — Connect to What You Know
         Link every new pattern to SOLID principles already learned
         Link to real projects — Kafka, VirusTotal, Sendgrid, URL shortener
         Point out when a pattern was already being used without knowing the name

Step 5 — Code First, Notes After
         Write the code before the README
         Review the code — find issues, ask why before telling
         Draft README only after code is solid
```

---

### Pattern by Pattern — What Led to Each Solution

**Singleton**

- Problem → multiple Logger instances cause inconsistent state
- Follow up → naive has race condition → synchronized is slow → double checked needs volatile
- Arrived at → Bill Pugh / double checked locking with volatile

**Factory Method**

- Problem → caller uses new directly, tightly coupled to concrete types
- Follow up → abstraction introduced → if-else moves to factory → map replaces if-else → who fills the map?
- Arrived at → self registering factory, each type owns its registration

**Builder**

- Problem → constructor with 8 params, nulls for optional fields
- Follow up → telescoping constructors explode → how to enforce mandatory without nulls?
- Arrived at → nested Builder class, mandatory in constructor, optional via chaining, validation in build()

**Prototype**

- Problem → reorder same object with one field changed — repeat all fields
- Follow up → copy constructor breaks immutability → how to copy and modify before finalizing?
- Arrived at → Builder-based from() method, copies all fields, modify only what changed

**Abstract Factory**

- Problem → separate factory per component, mixing providers silently
- Follow up → who guarantees all components belong to same family?
- Arrived at → one factory per family, all components from one provider instance

**Adapter**

- Problem → third party has different method name and signature
- Follow up → cannot modify either side — what sits in between?
- Arrived at → Adapter implements your interface, wraps third party internally, translates calls

**Facade**

- Problem → controller knows too many services, orchestration duplicated
- Follow up → already knew this — Service layer is Facade
- Arrived at → one entry point, owns orchestration, caller knows nothing about internals

**Decorator**

- Problem → adding behaviors to notification — class explosion for every combination
- Follow up → how to stack behaviors at runtime without new classes?
- Arrived at → each decorator wraps another via same interface, chain terminates at base

**Proxy**

- Problem → VirusTotal is expensive, limited, repeated calls waste quota
- Follow up → what sits in front and decides whether to forward the request?
- Arrived at → Cache Proxy returns early on hit, Rate Limit Proxy throws on exceeded limit

**Observer**

- Problem → order placed triggers many reactions — tight coupling, OCP violation
- Follow up → who maintains the list? how do observers register? how does service stay decoupled?
- Arrived at → self registering observers, publisher broadcasts, OrderService knows nothing

**Strategy**

- Problem → PaymentService decides which algorithm to use AND executes it — SRP violation
- Follow up → Factory already handles creation — what does Strategy add?
- Arrived at → Strategy injected into service, service only calls pay(), OrderService owns Factory interaction

**Template Method**

- Problem → common steps duplicated across every exporter class
- Follow up → already using this in BasePaymentStrategy and BaseOrderObserver
- Arrived at → abstract base with final skeleton, abstract method for varying step

**Command**

- Problem → undo needs different data for each operation — messy flag-based stack
- Follow up → what if each operation knew how to undo itself? redo without new interface method?
- Arrived at → Command interface with execute() and undo(), two stack Invoker, redo reuses execute()

**State**

- Problem → vending machine — if-else in every method, grows with every new state
- Follow up → what if state itself handled the behavior? how does state transition?
- Arrived at → state interface with all actions, machine delegates, states transition via machine getters

---

## Key Recurring Themes Across All Patterns

```
1. Interface first        — always define the contract before implementation
2. Self registration      — implementations register themselves, callers stay clean
3. Abstract base class    — common logic written once, inherited by all
4. Null checks early      — constructor or first line of method, never buried inside
5. OCP always             — adding new type = new class only, existing untouched
6. Polymorphism over if-else — every pattern replaces a conditional with a method call
```

---

## Remaining Patterns — Quick Reference

These patterns are less frequently asked at SDE-1 level but worth knowing conceptually.

---

### Iterator

**What it solves:**
Traverse a collection without exposing its internal structure.

**Core idea:**

```
Collection exposes iterator() method
Iterator has hasNext() and next()
Caller traverses without knowing if it is a List, Set, Tree, or Graph underneath
```

**You already use this:**

```java
for (String item : list) { }  // enhanced for loop uses Iterator internally
list.iterator()               // explicit Iterator
Stream.of(list)               // Stream API builds on Iterator concept
```

**When it appears in LLD:**
Custom data structures — graph traversal, file system traversal, playlist navigation.

**Key insight:**
Java's `Iterable` and `Iterator` interfaces are this pattern. Every collection you have used implements it. Implementing a custom Iterator means implementing `hasNext()` and `next()` on your own data structure.

---

### Composite

**What it solves:**
Treat individual objects and groups of objects uniformly — tree structures.

**Core idea:**

```
Component   ← common interface
Leaf        ← individual object, implements Component
Composite   ← group of Components, also implements Component
             can contain both Leafs and other Composites
```

**Real world:**

```
File system  → File (Leaf) and Folder (Composite) both have size(), delete()
UI framework → Button (Leaf) and Panel (Composite) both have render()
Organization → Employee (Leaf) and Manager (Composite) both have getSalary()
```

**When it appears in LLD:**
File system design, org chart, menu systems, UI component trees.

**Key insight:**
Caller treats a single file and an entire folder the same way — both respond to `size()`. Folder's `size()` recursively sums its children. Caller never needs to know if it is a leaf or a composite.

---

### Chain of Responsibility

**What it solves:**
Pass a request along a chain of handlers — each handler decides to process or pass it forward.

**Core idea:**

```
Handler interface → handle(request), setNext(handler)
Each handler     → process if it can, else forward to next
Chain            → assembled at startup, caller just sends to first handler
```

**Real world you already know:**

```
Spring filter chain  → auth filter → logging filter → rate limit filter → controller
VirusTotal scan      → cache check → rate limit check → actual scan
Exception handling   → catch specific, rethrow general
```

**When it appears in LLD:**
Approval workflows, ATM cash dispensing, logging level handling, middleware chains.

**Key insight:**
Similar to Decorator — both chain objects with same interface. But Decorator always calls next, Chain of Responsibility may stop at any handler. Decorator adds behavior, CoR decides who handles.

---

### Mediator

**What it solves:**
Many objects communicating with each other — replace direct references with a central mediator.

**Core idea:**

```
Without Mediator → A talks to B, C, D. B talks to A, C. C talks to A, B, D — tangled mesh
With Mediator   → A, B, C, D all talk to Mediator only — Mediator coordinates
```

**Real world:**

```
Air traffic control  → planes do not talk to each other, all talk to tower
Chat room            → users do not message each other directly, all go through chat room
Event bus            → components publish to bus, subscribe from bus
```

**When it appears in LLD:**
Chat systems, auction systems, air traffic control, UI form coordination.

**Key insight:**
Similar to Observer — both decouple components. Observer is one-to-many notification. Mediator is many-to-many coordination. Mediator knows about all participants, Observer does not.

---

## Patterns You Can Skip for SDE-1

These are rarely asked and have limited LLD application:

```
Flyweight   → share common state across many fine-grained objects
              relevant for game engines, rendering — not typical LLD interviews

Bridge      → separate abstraction from implementation
              complex to explain, rarely appears in LLD case studies

Memento     → capture and restore object state (undo without Command)
              overlaps with Command — Command is more commonly asked

Visitor     → add operations to objects without changing their classes
              complex, academic, rarely practical in LLD interviews
```

---

## Design Patterns — Complete Status

```
Creational (5/5 done):
✅ Singleton
✅ Factory Method
✅ Builder
✅ Prototype
✅ Abstract Factory

Structural (4/7 done — remaining skippable for SDE-1):
✅ Adapter
✅ Facade
✅ Decorator
✅ Proxy
⬜ Composite     ← know conceptually
⬜ Bridge        ← skip for SDE-1
⬜ Flyweight     ← skip for SDE-1

Behavioral (6/8 done — remaining skippable for SDE-1):
✅ Observer
✅ Strategy
✅ Command
✅ Template Method
✅ State
⬜ Iterator      ← know conceptually, already using it
⬜ Chain of Responsibility ← know conceptually
⬜ Mediator      ← know conceptually
```

---

## What Comes Next — LLD Case Studies

Now that patterns are done, LLD case studies apply multiple patterns together to solve real system design problems.

### Approach for Case Studies (same as patterns)

```
Step 1 — Understand the problem
          what entities exist? what are their relationships?
          what are the core operations?

Step 2 — Identify entities and responsibilities
          what classes are needed?
          what does each class own?

Step 3 — Apply patterns naturally
          do not force patterns — let the problem reveal them
          Singleton where one instance needed
          Factory where creation logic is complex
          Observer where one event triggers many reactions

Step 4 — Code incrementally
          start with interfaces and core entities
          add complexity one feature at a time

Step 5 — Discuss tradeoffs
          what did this design gain?
          what did it lose?
          what would change under different requirements?
```

### Case Studies to Cover

```
1. Parking Lot           ← Singleton, Factory, Strategy
2. Elevator System       ← State, Strategy, Observer
3. Notification System   ← Factory, Observer, Decorator, Strategy
4. Library Management    ← Factory, Observer, Strategy
5. Food Delivery (Swiggy)← Factory, Strategy, Observer, Builder
6. ATM Machine           ← State, Strategy, Singleton
```

---

## One Line Per Pattern — Quick Revision

```
Singleton        → one instance, global access, thread safe
Factory Method   → move object creation out of caller, self registering
Builder          → mandatory upfront, optional chained, validate in build()
Prototype        → clone existing, modify only what changed
Abstract Factory → one factory per family, guarantees consistency

Adapter          → translate incompatible interfaces, caller untouched
Facade           → one entry point over complex subsystem, service layer
Decorator        → stack behaviors at runtime, same interface, chain terminates at base
Proxy            → control access, may short circuit, caller unaware

Observer         → one event, many reactions, publisher knows nothing about observers
Strategy         → swap algorithm at runtime, service knows only the interface
Command          → encapsulate request as object, owns its own undo
Template Method  → skeleton fixed in base, varying step overridden by subclass
State            → object delegates to current state, state owns behavior, no if-else
Iterator         → traverse collection without exposing internals
```
