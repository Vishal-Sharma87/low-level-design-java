# Constructors & this Keyword — Interview Q&A

## Q1. What is the difference between a constructor and a method?

| Aspect | Constructor | Method |
|---|---:|---:|
| Purpose | Initialize object fields at creation | Define object behavior |
| Return type | None | Has a return type |
| Invocation | Once, automatically at `new` | Multiple times, explicitly called |
| Name | Same as class name | Any valid identifier |

## Q2. What is the default constructor? What happens when you define your own?

Java provides a default no-argument constructor only when you do not declare any constructors. Once you define any constructor, the default no-arg constructor is no longer generated. Therefore, `new Car()` will fail to compile unless you explicitly add a no-arg constructor.

## Q3. Can a constructor have a return type?

No. Adding a return type makes the declaration a regular method, not a constructor. The object returned by construction is handled implicitly by the JVM when `new` is used.

## Q4. What is constructor overloading?

Constructor overloading means defining multiple constructors in the same class with different parameter lists (different number, types, or order of parameters). This is a form of compile-time polymorphism: the compiler selects the appropriate constructor based on the arguments passed to `new`.

## Q5. What are the uses of the `this` keyword?

- `this.field` — refers to the current object's field and resolves ambiguity with parameter names.
- `this()` — calls another constructor in the same class (constructor chaining). Must be the first statement in the constructor.
- Passing `this` as an argument — passes the current object reference to another method or constructor.
