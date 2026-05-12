# Single Responsibility Principle (SRP)

## Definition

A class should have only one reason to change.

The keyword is **reason to change** — not "one method" or "one feature".
Ask yourself: "Who would ask me to change this class?"
If the answer is more than one type of person or concern — SRP is violated.

## Real Life Analogy

Think of a restaurant. The chef cooks. The waiter serves. The cashier bills.
Nobody does all three jobs — because if the chef is also billing customers,
one mistake messes up two completely unrelated things at once.

## What the bad/ code does wrong

`InvoiceManager` handles three unrelated responsibilities in one class:

- Business logic → calculateTotal(), applyDiscount()
- Persistence → saveToDatabase()
- Presentation → generatePDF()

Three different stakeholders (Finance, DBA, Design) all have reason
to change the same class. One change risks breaking the other two concerns.

## What the good/ code fixes

Split into three focused classes:

| Class               | Responsibility | Changed by    |
| ------------------- | -------------- | ------------- |
| InvoiceCalculator   | Business logic | Finance team  |
| InvoiceRepository   | Persistence    | DBA / Backend |
| InvoicePresentation | Presentation   | Design team   |

`InvoiceService` orchestrates — it uses the three classes but owns no logic itself.

## Key Takeaway

If you can describe a class's job using the word "AND", it is doing too much.
Split it until each class has exactly one reason to change.
