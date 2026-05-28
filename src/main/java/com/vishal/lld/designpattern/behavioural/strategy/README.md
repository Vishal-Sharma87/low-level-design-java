# Strategy Pattern

## Problem It Solves

A service knowing too much — responsible for both deciding which algorithm to use and executing it.

**Without Strategy:**

```java
public class PaymentService {
    public void pay(String type, double amount) {
        if ("upi".equals(type)) {
            System.out.println("Paying via UPI...");
        } else if ("card".equals(type)) {
            System.out.println("Paying via Card...");
        }
        // adding new type = modifying PaymentService — OCP violation
        // PaymentService knows about types, knows about Factory, knows about execution
        // SRP violation — doing too much
    }
}
```

**What is wrong:**

- `PaymentService` responsible for deciding algorithm + executing it — SRP violation
- Adding new payment type requires modifying `PaymentService` — OCP violation
- No abstraction — tightly coupled to concrete payment logic
- Cannot swap payment behavior at runtime without if-else

---

## Core Idea

> Define a family of algorithms, encapsulate each one, and make them interchangeable.  
> The service that uses them does not know which one it is using — just calls the contract.

---

## Factory vs Strategy — The Distinction

Both are often confused and used together. They solve different problems:

```
Factory   → solves CREATION problem
            who creates UPIPayment, CreditCardPayment objects
            converts type string to strategy object

Strategy  → solves SWITCHING problem
            how does PaymentService execute different algorithms
            without knowing concrete implementations
            service just calls pay() — delegates completely
```

**Without Strategy — PaymentService does too much:**

```
receives type string → calls factory → gets object → calls pay()
PaymentService knows about Factory, knows about types
```

**With Strategy — PaymentService does one thing:**

```
calls strategy.pay()
PaymentService knows nothing — just executes whatever is injected
```

---

## Real World Flow

```
User selects "UPI" on UI
→ OrderService receives request with paymentType = "UPI"
→ Factory converts type string to UPIStrategy object
→ Strategy injected into PaymentService
→ PaymentService calls pay() — zero knowledge of UPI
```

---

## The Structure

```
PaymentStrategy         ← interface — pay(), register()
BasePaymentStrategy     ← abstract — null check, common register() logic
UpiStrategy             ← extends Base, implements pay()
CardStrategy            ← extends Base, implements pay()
PaymentStrategyFactory  ← map based factory, self registration
PaymentService          ← only knows PaymentStrategy, calls pay()
OrderService            ← uses Factory to get strategy, injects into PaymentService
```

---

## The Implementation

**Strategy interface:**

```java
public interface PaymentStrategy {
    void pay();
    void register();
}
```

**Abstract base — register() written once:**

```java
public abstract class BasePaymentStrategy implements PaymentStrategy {

    protected String type;

    public BasePaymentStrategy(String type) {
        this.type = type;
    }

    @Override
    public void register() {
        PaymentStrategyFactory.register(type, this); // written once, inherited by all
    }
}
```

**Concrete strategies — only implement pay():**

```java
public class UpiStrategy extends BasePaymentStrategy {

    public UpiStrategy() {
        super("upi");
    }

    @Override
    public void pay() {
        // real signature would be pay(double amount, User user)
        // simplified here to focus on the pattern
        System.out.println("Paying via UPI...");
    }
}

public class CardStrategy extends BasePaymentStrategy {

    public CardStrategy() {
        super("card");
    }

    @Override
    public void pay() {
        System.out.println("Paying via Card...");
    }
}
```

**Factory — map based, self registration:**

```java
public class PaymentStrategyFactory {

    /*
     * reusing strategy instances from map is safe only when strategies are stateless
     * if pay() needs request specific data like amount, user, transactionId
     * create fresh strategy per request instead of reusing from map
     */
    private static Map<String, PaymentStrategy> payMap = new HashMap<>();

    public static void register(String type, PaymentStrategy paymentStrategy) {
        payMap.put(type, paymentStrategy);
    }

    public static PaymentStrategy getStrategy(String type) {
        if (payMap.containsKey(type))
            return payMap.get(type);
        throw new IllegalArgumentException("Undefined type: " + type);
    }
}
```

**PaymentService — zero knowledge of payment types:**

```java
public class PaymentService {

    private PaymentStrategy strategy;

    public PaymentService(PaymentStrategy strategy) {
        if (strategy == null)
            throw new IllegalArgumentException("Payment Strategy cannot be null");
        this.strategy = strategy;
    }

    public void pay() {
        strategy.pay(); // just delegates — does not know what runs underneath
    }
}
```

**OrderService — owns Factory interaction, injects strategy:**

```java
public class OrderService {

    public void placeOrder(String type) {
        System.out.println("Placing order with payment type: " + type);

        // Factory converts user selection to strategy object
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(type);

        // strategy injected — PaymentService never touches Factory
        PaymentService paymentService = new PaymentService(strategy);
        paymentService.pay();
    }
}
```

---

## Usage

```java
// registration phase — Spring handles via @PostConstruct in production
new UpiStrategy().register();
new CardStrategy().register();

OrderService orderService = new OrderService();

// user selected UPI
orderService.placeOrder("upi");

// user selected Card
orderService.placeOrder("card");
```

---

## Key Design Decisions

**Why does PaymentService not call Factory directly?**
PaymentService's job is to execute payment — not to decide which payment type to use. Factory interaction is OrderService's responsibility. Keeping them separate is pure SRP.

**Why reuse strategy instances from map carefully?**
Stateless strategies — just behavior, no request specific fields — are safe to reuse. Stateful strategies holding amount, user, transactionId must be created fresh per request to avoid data leaking between orders.

**Why abstract BasePaymentStrategy?**
`register()` is identical across all strategies. Abstract base writes it once, all strategies inherit it — same reasoning as BaseOrderObserver in Observer pattern.

**Can strategy be swapped at runtime?**
Yes — add a setter on PaymentService:

```java
public void setStrategy(PaymentStrategy strategy) {
    this.strategy = strategy;
}
```

Now same service instance can switch strategy mid-execution — useful for retry with different payment method on failure.

---

## Factory and Strategy Together

```
Factory   → creates the right strategy object from user selection string
Strategy  → injected into service, service executes without knowing the type

Combined flow:
user input "upi"
→ Factory.getStrategy("upi") → UpiStrategy object
→ PaymentService(upiStrategy)
→ paymentService.pay()
→ UpiStrategy.pay() executes
```

Most real world production code uses both together. They are complementary — Factory handles creation, Strategy handles execution.

---

## Real World Connection

Production code relies heavily on inheritance and polymorphism — Strategy is a structured application of both:

```
Interface          → PaymentStrategy defines the contract
Abstract class     → BasePaymentStrategy shares common logic
Concrete classes   → UpiStrategy, CardStrategy provide specific behavior
Polymorphism       → PaymentService calls pay() without knowing the subtype
```

Every pattern is a proven recipe for where and how to apply inheritance and polymorphism. Strategy is polymorphism applied to algorithm selection.

---

## Where Strategy Appears in LLD Case Studies

| Case Study          | Strategy Used For                                       |
| ------------------- | ------------------------------------------------------- |
| Payment system      | UPI, Card, NetBanking payment algorithms                |
| Sorting service     | BubbleSort, MergeSort, QuickSort strategies             |
| Discount engine     | FlatDiscount, PercentageDiscount, NoDiscount strategies |
| Compression service | ZIP, RAR, GZIP compression algorithms                   |

---

## Interview Version to Write

Show PaymentService with if-else — point out SRP and OCP violations.  
Introduce PaymentStrategy interface — family of algorithms behind common contract.  
Show PaymentService depending only on interface — just calls pay().  
Show OrderService owning Factory interaction — converts type string to strategy.  
Explain stateless vs stateful strategy reuse from map.  
Mention runtime swapping via setStrategy() — useful for retry scenarios.
