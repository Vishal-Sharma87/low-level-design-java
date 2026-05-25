# Abstract Factory Pattern

## Problem It Solves

When your system needs multiple related objects that must belong to the same family — and mixing families causes inconsistency, authentication failures, or silent bugs.

**Naive approach — separate factory per component:**

```java
// three separate factories, three separate string decisions
Storage storage   = StorageFactory.getStorage("aws");
Queue queue       = QueueFactory.getQueue("azure");   // wrong provider — silent bug
Database database = DatabaseFactory.getDatabase("aws");
```

**What is wrong:**

- Nothing enforces consistency — mixing providers compiles and runs without error
- Provider decision repeated on every factory call — one typo breaks the system
- Adding a new provider means updating every separate factory
- Caller must know and coordinate across multiple factories

---

## Core Idea

> Instead of one factory per component — one factory per **family**.  
> The family factory creates all related objects guaranteed to belong together.  
> Caller picks the family once — never worries about consistency again.

---

## Difference from Factory Method

```
Factory Method   → one factory, one product type
                   NotificationFactory → Email or SMS or Push

Abstract Factory → one factory, family of related product types
                   AWSFactory → S3Storage + SQSQueue + DynamoDB
                   AzureFactory → BlobStorage + ServiceBus + CosmosDB
```

---

## The Scenario

An application supporting two cloud providers — AWS and Azure. Each provides three services:

```
Storage  → S3 (AWS)        vs BlobStorage (Azure)
Queue    → SQS (AWS)       vs ServiceBus (Azure)
Database → DynamoDB (AWS)  vs CosmosDB (Azure)
```

All three services must always belong to the same provider — mixing causes authentication failures and billing issues.

---

## Progressive Evolution

### v1 — Naive (separate factory per component)

**Three separate factories:**

```java
public class StorageFactory {
    public static Storage getStorage(String provider) {
        if ("aws".equals(provider))   return new S3Storage();
        if ("azure".equals(provider)) return new BlobStorage();
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }
}

public class QueueFactory {
    public static Queue getQueue(String provider) {
        if ("aws".equals(provider))   return new SQSQueue();
        if ("azure".equals(provider)) return new ServiceBus();
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }
}

public class DatabaseFactory {
    public static Database getDatabase(String provider) {
        if ("aws".equals(provider))   return new DynamoDB();
        if ("azure".equals(provider)) return new CosmosDB();
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }
}
```

**Usage — the problem:**

```java
// register phase
new SQSQueue().register();
new ServiceBus().register();
new S3Storage().register();
new BlobStorage().register();
new DynamoDB().register();
new CosmosDB().register();

// AWS related
Storage storage1  = StorageFactory.getStorage("aws");
Queue queue1      = QueueFactory.getQueue("aws");
Database database1= DatabaseFactory.getDatabase("aws");

// Azure related — silent bug introduced here
Storage storage2  = StorageFactory.getStorage("azure");
Queue queue2      = QueueFactory.getQueue("aws");    //   should be "azure" — typo, no error
Database database2= DatabaseFactory.getDatabase("azure");
```

**What is wrong:**

- Provider string repeated on every factory call — one typo mixes providers silently
- No compile time or runtime error when providers are mixed
- Adding new provider requires updating every factory separately
- Caller responsible for coordinating consistency across factories

---

### v2 — Optimal (Abstract Factory)

**Product interfaces — one per service type:**

```java
public interface Storage {
    void store();
}

public interface Queue {
    void push();
}

public interface Database {
    void save();
}
```

**CloudProvider interface — the Abstract Factory:**

```java
// family factory — groups all related components together
// caller gets everything from one instance — no coordination needed
public interface CloudProvider {
    void register();
    Storage getStorage();
    Queue getQueue();
    Database getDatabase();
}
```

**AWS — concrete factory for AWS family:**

```java
public class AWS implements CloudProvider {

    public void register() {
        CloudProviderFactory.register("aws", this);
    }

    public Storage getStorage() {
        return new S3Storage();
    }

    public Queue getQueue() {
        return new SQSQueue();
    }

    public Database getDatabase() {
        return new DynamoDB();
    }
}
```

**Azure — concrete factory for Azure family:**

```java
public class Azure implements CloudProvider {

    public void register() {
        CloudProviderFactory.register("azure", this);
    }

    public Storage getStorage() {
        return new BlobStorage();
    }

    public Queue getQueue() {
        return new ServiceBus();
    }

    public Database getDatabase() {
        return new CosmosDB();
    }
}
```

**CloudProviderFactory — resolves provider by name:**

```java
public class CloudProviderFactory {

    private static Map<String, CloudProvider> registry = new HashMap<>();

    public static void register(String name, CloudProvider provider) {
        registry.put(name, provider);
    }

    public static CloudProvider getCloudProvider(String name) {
        if (registry.containsKey(name)) {
            return registry.get(name);
        }
        throw new IllegalArgumentException("Unknown provider: " + name);
    }
}
```

**Usage — consistent, safe, readable:**

```java
// register phase — each provider registers itself
new AWS().register();
new Azure().register();

// provider decision made once — no string repeated after this
CloudProvider awsProvider   = CloudProviderFactory.getCloudProvider("aws");
CloudProvider azureProvider = CloudProviderFactory.getCloudProvider("azure");

// all components guaranteed to belong to same provider
// no string to mistype, no accidental mixing
awsProvider.getStorage().store();
awsProvider.getQueue().push();
awsProvider.getDatabase().save();

azureProvider.getStorage().store();
azureProvider.getQueue().push();
azureProvider.getDatabase().save();
```

---

## Full Comparison

|                             | v1 Naive                     | v2 Abstract Factory         |
| --------------------------- | ---------------------------- | --------------------------- |
| Consistency guaranteed      |                              | YES                         |
| Provider decision made once |                              | YES                         |
| Silent mixing possible      | YES                          |                             |
| Adding new provider         | update every factory         | one new class               |
| Caller complexity           | high — coordinates factories | low — one provider instance |

---

## Key Design Decisions

**Why put all component methods inside CloudProvider interface?**
The whole point of Abstract Factory is that one provider instance gives you everything. If methods were split across interfaces, the caller would need to coordinate multiple instances — same problem as v1.

**Why not reuse component instances inside AWS/Azure?**
For learning purposes we use `new` each time. In production, cloud client objects are expensive to create and hold connection configs — they should be created once and reused. Both approaches are valid depending on whether the object is stateful or stateless.

**How does this combine with Factory Method?**
In reality each provider offers multiple services per type — AWS alone has SQS, SNS, and Kinesis for queues. Abstract Factory picks the provider, Factory Method picks the specific service within that provider:

```java
CloudProvider provider = CloudProviderFactory.getCloudProvider("aws");
Queue queue = provider.getQueue("sqs"); // Factory Method inside Abstract Factory
```

---

## Where Abstract Factory Appears in LLD Case Studies

| Case Study           | Abstract Factory Used For                  |
| -------------------- | ------------------------------------------ |
| Cloud infrastructure | AWS vs Azure vs GCP provider families      |
| UI framework         | Light vs Dark theme component families     |
| Payment gateway      | Domestic vs International payment families |
| Database layer       | SQL vs NoSQL provider families             |

---

## Interview Version to Write

Start with v1 naive — show the mixing problem with a comment on the wrong string.  
Explain that the problem is repeated provider decision across multiple factories.  
Write CloudProvider interface with all component methods — that is the Abstract Factory.  
Show that after `getCloudProvider()` the caller never touches a string again.  
Mention that in real systems this combines with Factory Method for service-level selection within a provider.
