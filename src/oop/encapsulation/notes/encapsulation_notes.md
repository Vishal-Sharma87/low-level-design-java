# Encapsulation — Interview Q&A

## Q1. What is Encapsulation and why do we need it?

Encapsulation means making object fields private to prevent direct external access. It is needed to prevent objects from reaching an invalid or inconsistent state — for example, setting a speed to a negative value or assigning `null` to a required name — by adding validation logic in controlled getters and setters.

## Q2. What is the difference between private, public, and protected access modifiers?

| Modifier                    | Same Class | Same Package | Subclass | Everywhere |
| --------------------------- | :--------: | :----------: | :------: | :--------: |
| `private`                   |     ✅     |      ❌      |    ❌    |     ❌     |
| `protected`                 |     ✅     |      ✅      |    ✅    |     ❌     |
| `public`                    |     ✅     |      ✅      |    ✅    |     ✅     |
| (default / package-private) |     ✅     |      ✅      |    ❌    |     ❌     |

Default (no modifier) = same package only.

## Q3. Can you have a class with only getters and no setters?

Yes. Such classes are called immutable — read-only objects whose state cannot be changed after creation. `String` in Java is the most famous example.

## Q4. Is encapsulation only about private fields and getters/setters?

No. Encapsulation is about preventing invalid or direct access to object state and adding validation logic that controls how fields are accessed or modified.

## Q5. What is the difference between Encapsulation and Abstraction?

- **Encapsulation:** hides the data (fields) and controls access through methods. Protects object integrity.
- **Abstraction:** hides implementation details and exposes only what is necessary. For example, a `sort()` method exposes sorting behavior without revealing the internal algorithm.
