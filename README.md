# 🔗 URL Shortener

## 🚀 Overview
A high-performance URL Shortener built using **Spring Boot**.

This service:
- Converts long URLs → short URLs
- Redirects short URLs → original URLs
- Uses **ID obfuscation + Base62 encoding** (no randomness, no collisions)

---

## ⚙️ System Flow

```mermaid
flowchart TD
    A[Client sends long URL] --> B[POST /generate]
    B --> C[Save in DB]
    C --> D[Generate ID]
    D --> E[Obfuscate ID]
    E --> F[Base62 Encode]
    F --> G[Return short URL]

    H[User hits short URL] --> I["GET /fetch/{shortUrl}"]
    I --> J[DB Lookup]
    J --> K[Return 301 Redirect]
```

---

## 🧠 Algorithm Deep Dive

### 🔹 Step 1: DB ID
Each URL gets a unique auto-generated ID.

---

### 🔹 Step 2: ID Obfuscation

```mermaid
flowchart LR
    A[Original ID] --> B[XOR with SALT]
    B --> C[MurmurHash Mix]
    C --> D[Bit Rotation]
    D --> E[Repeat 7 rounds]
    E --> F[Obfuscated ID]
```

```java
private static final long SALT = 0x9E3779B97F4A7C15L;

public static Long encode(Long id) {
    Long x = id;

    for (int i = 0; i < 7; i++) {
        x ^= mix(x ^ (SALT + i));
        x = Long.rotateLeft(x, 27);
    }
    return x;
}
```

---

### 🔹 Step 3: Base62 Encoding

```mermaid
flowchart LR
    A[Obfuscated Number] --> B[Divide by 62]
    B --> C[Map remainder to charset]
    C --> D[Repeat]
    D --> E[Reverse string]
    E --> F[Short Code]
```

```java
private static final String CHARS =
"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
```

---

## 🔁 Redirect Flow

```mermaid
sequenceDiagram
    participant User
    participant Controller
    participant Service
    participant DB

    User->>Controller: GET /fetch/{shortUrl}
    Controller->>Service: resolve(shortUrl)
    Service->>DB: findByShortUrl()
    DB-->>Service: longUrl
    Service-->>Controller: 301 Redirect
    Controller-->>User: Location: longUrl
```

---

## 🏗️ Architecture

```mermaid
graph LR
    Client --> Controller
    Controller --> Service
    Service --> Repository
    Repository --> Database
```

---

## 📦 API

### 🔹 Generate Short URL

```http
POST /generate
```

```json
{
  "url": "https://example.com"
}
```

---

### 🔹 Redirect

```http
GET /fetch/{shortUrl}
```

Returns:
- HTTP 301
- Location header with original URL

---

## 🗃️ Data Model

```mermaid
erDiagram
    URLS {
        Long id PK
        String long_url
        String short_url
    }
```

```java
@Entity
@Table(name = "urls")
public class DBRecord {
    @Id
    @GeneratedValue
    private Long id;

    private String longUrl;
    private String shortUrl;
}
```

---

## ⚡ Features

- Deterministic short URLs
- No collisions
- Obfuscated IDs (anti-enumeration)
- Fast O(1) lookup
- Clean layered architecture

---

## 🧠 Design Insight

```mermaid
flowchart TD
    A[Naive Random Strings] -->|Issues| B[Collisions]
    A --> C[Storage Overhead]

    D[Hash Only] --> E[Not Reversible]

    F[Your Approach] --> G[Unique IDs]
    F --> H[Obfuscation]
    F --> I[Compact Encoding]
```

This approach ensures:
- Predictable performance
- Zero collision risk
- Secure URL generation