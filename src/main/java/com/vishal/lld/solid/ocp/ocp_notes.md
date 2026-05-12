# Open/Closed Principle (OCP)

## Definition

A class should be open for extension but closed for modification.

- Open for extension → you can add new behaviour
- Closed for modification → you don't touch existing, already-tested code to do it

New requirements should mean new code, not edited old code.

## Real Life Analogy

A power strip. When you want to plug in a new device, you don't rewire the strip.
You just plug in. The strip is closed for modification (internal wiring stays the same)
but open for extension (new devices can connect anytime).

## What the bad/ code does wrong

`PaymentService` has a growing if-else block inside processPayment():

    if type == "UPI"    → handle UPI
    if type == "CARD"   → handle Card
    if type == "WALLET" → handle Wallet

Every new payment method forces you to open and edit this class.
One typo in the new block can break UPI and Card flows that already worked.

## What the good/ code fixes

Introduced a `PaymentStrategy` interface as the fixed contract.
Each payment method is its own class implementing that interface.
`PaymentStrategyFactory` handles the decision of which strategy to build.
`PaymentService` just calls strategy.pay() — no if-else, never changes.

### Flow

```
    PaymentController
    ↓  (receives "UPI" from request)
    PaymentService.processPayment("UPI", amount)
    ↓  (asks factory for the right object)
    PaymentStrategyFactory.getStrategy("UPI")
    ↓  (returns UpiPayment instance)
    PaymentService calls strategy.pay(amount)
    ↓
    UpiPayment.pay() executes
```

### Adding a new payment method (e.g. Crypto)

- Create `CryptoPayment implements PaymentStrategy`
- Add one line in `PaymentStrategyFactory`
- `PaymentService` is never touched

## Patterns Used

- Strategy Pattern → behaviour is encapsulated behind an interface
- Factory Pattern → object creation decision is isolated in one place

## Key Takeaway

If adding a new feature requires editing a class that already works, OCP is violated.
The if-else should not live in business logic — isolate it in a factory whose
only job is to decide which object to create.

## Connection to SRP

A class that violates SRP almost always violates OCP too.
When multiple concerns live in one place, every new requirement forces you to edit it.
SRP and OCP work together — split first, then close for modification.
