# POS — Point of Sale Backend

A **Point of Sale (POS) and retail management system**, delivered as a REST API built with
Spring Boot and PostgreSQL.

In plain terms: this is the **brain behind a shop's checkout and back office**. It keeps the
single source of truth for everything that happens in a retail business — from the product
catalog and stock levels, to the moment a customer pays at the counter, to the purchase orders
that refill the shelves and the reports the owner reads at the end of the day.

It is **not** a user interface. There is no screen or button. Instead, it exposes a clean,
secured JSON API that any front end (a web dashboard, a cashier tablet app, a mobile POS, or a
third-party integration) can talk to.

> **Status / honest snapshot**
> - The **foundation is complete**: the full data model (**46 tables across 9 business domains**)
>   and the **security + role-based access control** layer are done and wired up.
> - The **REST API currently exposes authentication and catalog management** (products,
>   categories, brands, units, taxes).
> - Every other domain (sales, inventory, purchasing, customers, expenses, HR, …) is **fully
>   modeled in the database** and has entities and repositories, but its controllers/services are
>   still to be built. See the [Roadmap](#roadmap).

---

## Table of Contents

- [The Goal of This Project](#the-goal-of-this-project)
- [What This System Does](#what-this-system-does)
- [Feature Status at a Glance](#feature-status-at-a-glance)
- [Who It Is For](#who-it-is-for)
- [Glossary](#glossary)
- [How a Sale Flows Through the System](#how-a-sale-flows-through-the-system)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Domain Model](#domain-model)
- [Database](#database)
- [Security & Authentication](#security--authentication)
- [API Reference](#api-reference)
- [Configuration](#configuration)
- [Getting Started](#getting-started)
- [Docker](#docker)
- [Observability](#observability)
- [Testing](#testing)
- [Design Decisions](#design-decisions)
- [Roadmap](#roadmap)
- [License](#license)

---

## The Goal of This Project

Retail businesses are messy. Stock is counted by eye, sales are written on paper or kept in a
spreadsheet, supplier orders live in someone's head, and at the end of the month nobody can say
with confidence how much was sold, what is left on the shelf, or where the money went.

**The goal of this project is to replace that mess with one reliable system of record.**

The project is built around a few firm goals:

1. **One source of truth.** Every product, sale, payment, stock movement, purchase, expense and
   employee exists in exactly one place, in a normalized relational database. No more reconciling
   three spreadsheets that disagree.

2. **Accurate money, always.** All monetary values are stored as exact `NUMERIC` decimals (never
   floating point), profit margin is computed by the database itself, and every historical sale
   keeps the price and tax it was sold at — even if the product's price changes later.

3. **An explainable inventory.** Stock never just "is" a number. Every unit that enters or leaves
   a warehouse is recorded as an immutable movement (received, sold, adjusted, transferred,
   wasted, returned), so the current count can always be traced back to real events.

4. **Security and accountability by default.** Every endpoint is authenticated, every action is
   gated by a fine-grained permission, and the data model carries the audit fields needed to
   answer "who changed this, and when?".

5. **A strong foundation to build on.** The schema and security model are designed for the whole
   retail domain up front, so new features (checkout, purchasing, reporting) can be added without
   re-architecting anything.

6. **It must be operable in the real world.** Time-ordered UUID keys, optimistic locking, soft
   deletes, database migrations, health checks, metrics and containerized deployment are all in
   place from day one — not bolted on later.

### What this project is *not*

- **Not a front end.** It is an API only; the cashier screen, kitchen display and admin dashboard
  are separate apps that consume this API.
- **Not a payment processor.** It records that a payment of a given type (cash, card, wallet)
  occurred; it does not talk to a bank or card network.
- **Not (yet) multi-tenant.** It models a single shop (`shop_info` is one row). See the roadmap.

---

## What This System Does

At a business level, the system models the entire lifecycle of a retail operation:

| Business area | What it handles |
|---|---|
| **Authentication & users** | Login, registration, JWT access/refresh tokens, logout, RBAC with roles and permissions |
| **Catalog** | Products with variants, images and three price points; hierarchical categories; brands; units of measure with conversions; tax classes |
| **Point of Sale** | Sales/invoices with line items, split and multi-method payments, held (parked) orders, refunds and exchanges |
| **Inventory** | Per-warehouse stock, immutable stock movements, batches, serial numbers, stock counts and reason-coded adjustments |
| **Purchasing** | Suppliers with credit terms, purchase orders, receiving (receipts), and supplier payments |
| **Customers & loyalty** | Customer profiles, multiple addresses, loyalty points ledger with tiers (bronze → platinum) |
| **Expenses** | Categorized expenses with receipt attachments, approval workflow, and recurring bills |
| **Human resources** | Employee profiles, cash-register shifts with variance tracking, and attendance/clock-in records |
| **Governance** | Audit logs (before/after values), notification preferences, system backups, shop profile |

Technically, the system is:

- a **stateless REST API** (JWT-secured, context path `/api/v1`),
- backed by **PostgreSQL 17** with a schema owned by **Flyway migrations**,
- accelerated by a **Redis** cache,
- observable via **Actuator + Prometheus** on a separate management port,
- and packaged to run as a **non-root Docker container** or directly on the JVM.

---

## Feature Status at a Glance

| Area | Database model | Entities & repositories | REST API |
|---|:---:|:---:|:---:|
| Auth, roles, permissions | ✅ | ✅ | ✅ |
| Products, categories, brands, units, tax classes | ✅ | ✅ | ✅ |
| Sales / checkout / payments | ✅ | ✅ | ⛔ planned |
| Refunds, exchanges, held orders | ✅ | ✅ | ⛔ planned |
| Inventory, warehouses, batches, serials, counts, adjustments | ✅ | ✅ | ⛔ planned |
| Customers & loyalty | ✅ | ✅ | ⛔ planned |
| Suppliers, purchase orders, receipts, payments | ✅ | ✅ | ⛔ planned |
| Expenses | ✅ | ✅ | ⛔ planned |
| Employees, shifts, attendance | ✅ | ✅ | ⛔ planned |
| Reports, settings, audit logs, backups | ✅ | ✅ | ⛔ planned |

Permissions for every "planned" area are **already seeded**, so the endpoints can be added
without touching the security setup.

---

## Who It Is For

- **Store owners / management** — want accurate sales, stock and profit figures without manual
  bookkeeping.
- **Cashiers** — need a fast, reliable checkout that fails safely (never sells the same serial
  number twice, never silently overwrites another cashier's edit).
- **Back-office / inventory staff** — receive stock, run counts, record waste, and reconcile the
  numbers.
- **Developers & integrators** — get a well-structured, documented, permissioned API to build a
  front end or integration on top of.

---

## Glossary

Retail and POS terminology used throughout this document:

| Term | Meaning |
|---|---|
| **POS** | Point of Sale — the counter/terminal (and software) where a sale is rung up. |
| **SKU** | Stock Keeping Unit — a unique code identifying a specific sellable item. |
| **Barcode** | A machine-readable code (e.g. EAN/UPC/CODE128) on a product. |
| **Variant** | A specific version of a product, e.g. size/color (S/Blue vs M/Red). |
| **Batch / Lot** | A group of stock made/received together, with shared expiry. |
| **Serial number** | A unique identifier for one individual unit (e.g. a specific phone). |
| **FEFO** | First-Expired-First-Out — pick stock closest to expiry first. |
| **RBAC** | Role-Based Access Control — users get roles, roles bundle permissions. |
| **JWT** | JSON Web Token — a signed, stateless credential proving who you are. |
| **Soft delete** | Marking a record deleted without physically removing it, to preserve history. |
| **Stock movement** | An immutable record of stock changing (received, sold, adjusted…). |
| **Split tender** | Paying one sale with more than one payment method. |
| **WMS terms** (warehouse, inventory) | Location-based stock storage and control. |

---

## How a Sale Flows Through the System

To make the purpose concrete, here is the intended end-to-end path of a single transaction
(parts marked *planned* are modeled in the DB but not yet exposed by a controller):

```
1. Cashier logs in            → POST /auth/login → access + refresh tokens         (live)
2. Cashier scans a product    → GET  /products/search?keyword=...                  (live)
3. Cashier builds the basket  → (client side)
4. Cashier checks out         → POST /sales   (creates Sale + SaleItem rows)        (planned)
                                  • stock is reserved and a StockMovement 'sale'
                                    row is written per line
5. Customer pays              → POST /sales/{id}/payments
                                  • one or many Payment rows (cash, card, wallet)
6. Stock leaves the warehouse → Inventory quantities decremented, movement logged  (planned)
7. Loyalty points earned      → LoyaltyTransaction ledger entry                     (planned)
8. Receipt printed            → uses pos.receipt.* / pos.printing.* config          (planned)
9. Later, if returned         → POST /sales/{id}/refunds (Refund + RefundItem)      (planned)
```

Every step is secured, audited, and traceable, which is the whole point of the project.

---

## Tech Stack

| Layer | Choice | Notes |
|---|---|---|
| Language | **Java 21** | Virtual threads enabled by default |
| Framework | **Spring Boot 4.1.1** | |
| Web | Spring WebMvc | REST, context path `/api/v1`, HTTP/2, response compression |
| Persistence | **Spring Data JPA + Hibernate 7** | `ddl-auto: validate` — Flyway owns the schema |
| Database | **PostgreSQL 17** | JSONB, arrays, generated columns, partial indexes |
| Migrations | **Flyway** | 11 versioned migrations, `baseline-on-migrate` |
| Security | **Spring Security 6 + JWT (jjwt 0.12.7)** | Stateless, HS256, access + refresh tokens |
| Cache | **Redis 7 (Lettuce)** | Spring Cache abstraction, 1-hour TTL, `pos:cache:` prefix |
| Validation | Jakarta Bean Validation | `@Valid` on all request DTOs |
| Mail | Spring Mail (SMTP) | STARTTLS required |
| Observability | Actuator + **Micrometer/Prometheus** | Separate management port `8098` |
| API docs | **springdoc-openapi 3.1.1** | Swagger UI + OpenAPI 3.1 JSON |
| Build | **Maven** (wrapper included) | `mvnw` / `mvnw.cmd` |
| Container | Docker + Docker Compose | Multi-stage, non-root runtime |
| Dev tooling | **BootUI** (`/bootui`) | In-app database browser, auth-exempt chain |
| JSON | Jackson | Pretty print, ignore-unknown, non-null inclusion |

---

## Architecture

### Layering

The code follows a classic, strict layered architecture. A request passes down through the
layers, and a response passes back up — each layer only talks to the one directly beneath it.

```
HTTP request
   │
   ▼
┌──────────────────────────────────────────────────────────┐
│ controller/     @RestController, @PreAuthorize per method │
│                 Maps HTTP ↔ DTO, never touches entities    │
└───────────────────────┬──────────────────────────────────┘
                        ▼
┌──────────────────────────────────────────────────────────┐
│ service/         Interface + service/impl/                │
│                 @Transactional business logic,            │
│                 slug generation, integrity translation    │
└───────────────────────┬──────────────────────────────────┘
                        ▼
┌──────────────────────────────────────────────────────────┐
│ repository/      Spring Data JPA interfaces               │
│                 Some native/modifying queries for bulk    │
└───────────────────────┬──────────────────────────────────┘
                        ▼
┌──────────────────────────────────────────────────────────┐
│ entity/          JPA entities + BaseEntity mapped         │
│                 superclass (audit, soft delete, version)   │
└───────────────────────┬──────────────────────────────────┘
                        ▼
                  PostgreSQL 17
```

Cross-cutting concerns live outside the layers:

- `security/` — JWT issuance/parsing, the auth filter, user loading, principal
- `config/` — `SecurityConfig` (3 filter chains), JPA auditing
- `web/GlobalExceptionHandler` — one place that turns exceptions into HTTP responses
- `web/dto/` — request/response records; `mapper/` — hand-written entity→DTO mappers
- `exception/` — `BusinessException` hierarchy carrying an HTTP status

### Request lifecycle (authenticated call)

```
Client
  │ Authorization: Bearer <accessToken>
  ▼
JwtAuthenticationFilter          parses + validates JWT, rejects typ=refresh
  │  → CustomUserDetailsService loads user, builds authorities
  ▼
SecurityContextHolder           AppUserPrincipal { userId, UserDetails }
  │
  ▼
@PreAuthorize("hasAuthority('products:create')")   method-level RBAC
  │
  ▼
Controller → Service (@Transactional) → Repository → DB
  │
  ▼
Mapper enriches response with AuditUserRef (createdBy/updatedBy display info)
  │
  ▼
ApiResponse<T> { success, message, data }
```

---

## Project Structure

```
pos/
├── pom.xml                     Maven build (Java 21, Spring Boot 4.1.1)
├── Dockerfile                  Multi-stage: JDK 21 build → JRE 21 runtime
├── docker-compose.yml          postgres + redis + app
├── .env.example                All environment variables, documented
├── scripts/run-local.sh        Run app on host against Dockerized DB/Redis
├── HELP.md                     Spring Boot reference notes
├── logs/                       Rotating log output (pos-system.log)
└── src/
    ├── main/
    │   ├── java/com/den/pos/
    │   │   ├── PosApplication.java
    │   │   ├── common/          UserIdAware — lets any class ask "who is calling?"
    │   │   ├── config/          SecurityConfig, JpaAuditingConfig
    │   │   ├── controller/      Auth, Brand, Category, Product, TaxClass, Unit
    │   │   ├── entity/          46 tables grouped by domain
    │   │   │   ├── auth/        User, Role, Permission, UserRole, RolePermission
    │   │   │   ├── catalog/     Product, ProductVariant, ProductImage,
    │   │   │   │                Category, Brand, Unit, TaxClass
    │   │   │   ├── customer/    Customer, CustomerAddress, LoyaltyTransaction
    │   │   │   ├── expense/     Expense, ExpenseCategory, ExpenseAttachment
    │   │   │   ├── hr/          Employee, Shift, Attendance
    │   │   │   ├── inventory/   Inventory, Warehouse, StockMovement, Batch,
    │   │   │   │                SerialNumber, StockCount(+Item),
    │   │   │   │                StockAdjustment(+Item)
    │   │   │   ├── sales/       Sale, SaleItem, Payment, Refund(+Item),
    │   │   │   │                Exchange(+Item), HeldOrder
    │   │   │   ├── supplier/    Supplier, PurchaseOrder(+Item),
    │   │   │   │                PurchaseReceipt(+Item), SupplierPayment
    │   │   │   └── system/      ShopInfo, AuditLog,
    │   │   │                    NotificationPreference, SystemBackup
    │   │   ├── exception/       BusinessException, ResourceNotFound,
    │   │   │                    Conflict, Unauthorized
    │   │   ├── mapper/          Entity → DTO mappers (hand-written)
    │   │   ├── repository/      Spring Data repositories, grouped by domain
    │   │   ├── security/        JwtService, JwtAuthenticationFilter,
    │   │   │                    CustomUserDetailsService, AppUserPrincipal,
    │   │   │                    JwtProperties, JwtDateAdapter
    │   │   ├── service/         Business logic (+ service/impl/)
    │   │   └── web/             GlobalExceptionHandler + dto/
    │   └── resources/
    │       ├── application.yaml         All configuration, env-var driven
    │       └── db/migration/            Flyway V1 → V11
    └── test/java/com/den/pos/
        ├── PosApplicationTests              Context loads
        └── config/MethodSecurityEnforcementTest   @PreAuthorize 200 vs 403
```

---

## Domain Model

The data model is organized into nine business domains. The diagram below shows the main
relationships (arrows point from parent to child / referenced entity).

```
                        ┌──────────┐
                        │   User   │◀──┐ employee_id
                        └────┬─────┘   │
                             │         │
              ┌──────────────┼─────────┴────────────────┐
              │              │                         │
        ┌─────▼─────┐  ┌─────▼──────┐            ┌─────▼─────┐
        │UserRole   │  │ AuditLog   │            │ Employee  │
        └─────┬─────┘  └────────────┘            └─────┬─────┘
              │                                       │
        ┌─────▼─────┐                          ┌──────┴───────┐
        │   Role    │──▶ RolePermission ──▶ Permission
        └───────────┘                          └──────────────┘

  CATALOG                INVENTORY                    SALES
  ┌──────────┐   ┌──────────┐  ┌───────────┐    ┌──────────┐
  │Category  │   │  Brand   │  │ Warehouse │    │   Sale   │──▶ Payment
  │  (tree)  │   └──────────┘  └─────┬─────┘    └────┬─────┘
  └────┬─────┘                        │               │
       │  ┌──────────┐                │        ┌──────▼──────┐
       └──▶ Product  │◀───────────────┼────────│  SaleItem    │
          └────┬─────┘                │        └─────────────┘
               ├──▶ ProductVariant    │        ┌──────────────┐
               ├──▶ ProductImage      │        │  Refund      │──▶ RefundItem
               │                     │        │  Exchange    │──▶ ExchangeItem
               ├──▶ Inventory ───────┤        │  HeldOrder   │
               ├──▶ Batch             │        └──────────────┘
               ├──▶ SerialNumber      │
               └──▶ StockMovement     │        PURCHASING
  ┌──────────┐  ┌──────────┐  ┌──────┴──┐    ┌──────────────┐
  │   Unit   │  │ TaxClass │  │StockCount│    │  Supplier    │
  └──────────┘  └──────────┘  │Adjustment│    └──────┬───────┘
                               └──────────┘           │
                                              ┌───────▼────────┐
  CUSTOMER                EXPENSE              │ PurchaseOrder │
  ┌───────────┐  ┌────────────────┐           └───────┬────────┘
  │ Customer  │  │    Expense     │◀──category     ┌────▼─────────────┐
  │ + loyalty │  │  + Attachment  │   ┌───────────┴──┐               │
  │ + address │  └────────────────┘   │PurchaseReceipt│──▶ SupplierPayment
  └───────────┘                       └───────────────┘
```

`shop_info`, `notification_preferences`, `system_backups` hang off the shop/user and are
intentionally not shown above.

### Entity conventions

Every business table extends `BaseEntity`, which provides:

| Feature | Implementation |
|---|---|
| **IDs** | `UUID` **version 7** (`@UuidGenerator(style = VERSION_7)`) — time-ordered, so indexes stay dense and B-tree inserts stay sequential |
| **Optimistic locking** | `@Version Long version` — concurrent edits fail loudly instead of silently overwriting |
| **Timestamps** | `createdAt` / `updatedAt`, set in `@PrePersist` / `@PreUpdate` |
| **Actor tracking** | `createdBy` / `updatedBy` UUIDs, populated by `AuditingEntityListener` via the authenticated principal |
| **Soft delete** | Hibernate `@SoftDelete` + `deletedAt` / `deletedBy` for forensic tombstones |
| **Batch-friendly** | `order_inserts`, `order_updates`, batch size 50 |

Small lookup tables (`units`, `roles`, `permissions`) and junction tables (`user_roles`,
`role_permissions`) deliberately do **not** extend `BaseEntity` — the junction tables implement
`Persistable` so JPA can distinguish insert-from-update on composite keys.

---

## Database

**PostgreSQL 17**, 46 tables created by `V1__init.sql`. Host port **5434** (mapped to avoid
conflicts with a local Postgres on 5432).

### Migration history

| Version | Purpose |
|---|---|
| `V1__init.sql` | 46 tables, indexes, enums/checks, and the `employees ↔ users` circular FK resolved via `ALTER` |
| `V2__user_roles.sql` | `user_roles` junction table; seeds roles `owner`, `admin`, `manager`, `cashier` |
| `V3__add_fk_indexes_and_not_null.sql` | Indexes on every FK column + `NOT NULL` hardening |
| `V4__additional_indexes.sql` | Extra composite indexes for hot query paths |
| `V5__fix_fk_indexes.sql` | Corrected FK index coverage |
| `V6__optimistic_locking_version.sql` | Adds `version` columns for optimistic locking |
| `V7__seed_admin_role_and_user.sql` | 100+ permission catalogue, grants all to `admin`, seeds the admin user |
| `V8__add_created_by_updated_by.sql` | Audit columns `created_by` / `updated_by` |
| `V9__soft_delete_tombstones.sql` | `deleted_at`, `deleted_by`, `is_deleted` across tables |
| `V10__is_deleted_boolean.sql` | Normalized soft-delete flags to boolean |
| `V11__audit_fk_indexes_and_fix_sales_is_deleted.sql` | Indexes for audit FKs; fixed `sales.is_deleted` |

### Notable schema choices

- **`products.profit_margin`** — a PostgreSQL *generated column*, so the DB always computes it
  and the entity marks it `insertable = false, updatable = false`
- **`attendance`** — `UNIQUE (employee_id, attendance_date)` prevents double clock-ins
- **`sale_items.serial_numbers`**, **`held_orders.items`**, **`audit_logs.old_values/new_values`** —
  `text[]` / `jsonb` for variable-shape data
- **`stock_adjustment_items.difference`** and **`stock_count_items.difference`** — system vs actual
  quantity variance, per line
- **Currency** — all money columns are `NUMERIC(12,2)` (exact, never float); `NUMERIC(5,2)` for
  rates, `NUMERIC(10,2)` for product prices
- **Cascade policy** — master data (categories, brands) is *referenced*, so deletes are guarded
  by FKs and translated to `409 Conflict` rather than cascading silently

### Default credentials

Seeded by `V7` for first login:

```
email:    admin@pos.local
password: Admin@123
role:     admin  (all 100+ permissions)
```

> Change this immediately in any non-local environment. `/auth/register` auto-assigns
> `owner` to the first user and `cashier` to everyone after.

---

## Security & Authentication

### Token design

| | Access token | Refresh token |
|---|---|---|
| Lifetime | 15 min (`JWT_EXPIRATION=900000`) | 7 days (`REFRESH_TOKEN_EXPIRATION=604800000`) |
| Claim `typ` | `access` | `refresh` |
| Purpose | Authorizes every API call | Mints new access tokens only |
| Send as | `Authorization: Bearer <token>` | `POST /auth/refresh` body |

Signed with **HS256** using `JWT_SECRET` (required, ≥ 32 bytes). Claims: `sub` (email),
`uid` (user UUID), `typ`, `iat`, `exp`.

`JwtAuthenticationFilter` runs once per request, rejects refresh tokens presented as access
tokens, loads the user, and populates the `SecurityContext` with `AppUserPrincipal`
(which also carries the user ID, so any class can resolve "who is calling" for auditing).

### Refresh-token safety

Refresh tokens are compared with `MessageDigest.isEqual(...)` — a **constant-time** comparison
that removes timing side-channels. On refresh, the stored token is replaced (rotation), so a
used refresh token is immediately invalidated. Logout nulls the stored token, revoking the
whole session chain server-side despite stateless JWTs.

### Three security filter chains

| Order | Matcher | Policy |
|---|---|---|
| `Integer.MIN_VALUE` | `/bootui`, `/bootui/**` | Permit all (dev database browser, own chain) |
| `1` | `/actuator/**` | `/health` and `/info` public; rest authenticated |
| `2` | everything else | `/auth/**` public; all else authenticated |

Main chain: CSRF disabled (token auth), CORS enabled from `cors.allowed-origins`,
`STATELESS` sessions.

### Response headers

Every response carries a hardened header set:

```
Content-Security-Policy: default-src 'self'; base-uri 'self'; frame-ancestors 'none'; object-src 'none'
Referrer-Policy:         strict-origin-when-cross-origin
Permissions-Policy:      camera=(), microphone=(), geolocation=(), payment=(), usb=(), speaker=()
Cross-Origin-Opener-Policy:   same-origin
Cross-Origin-Embedder-Policy: require-corp
```

### Role-based access control

Four seeded system roles: **`owner`**, **`admin`**, **`manager`**, **`cashier`**.

Permissions are fine-grained `module:action` strings, granted to roles through
`role_permissions`, and resolved into Spring authorities **verbatim** (no `ROLE_` prefix for
permissions) so `@PreAuthorize` can check them directly:

```java
@PreAuthorize("hasAuthority('products:create')")
```

Modules covered by the seeded catalogue: `pos`, `products`, `inventory` (incl. `count`,
`adjust`), `customers`, `suppliers`, `purchases` (incl. `approve`), `expenses` (incl.
`approve`), `employees`, `attendance`, `shifts`, `warehouses`, `categories`, `brands`,
`units`, `taxes`, `reports` (incl. `export`), `settings`, `audit`, `backups` (incl. `restore`),
`roles` (`manage`), `users`, `loyalty`, `refunds`, `exchanges`.

### Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) maps everything consistently:

| Exception | Status |
|---|---|
| `MethodArgumentNotValidException` | `400` + field-error map |
| `ResourceNotFoundException` | `404` |
| `ConflictException` | `409` |
| `UnauthorizedException` | `401` |
| `AccessDeniedException` | `403` |
| `ResponseStatusException` | its own status |
| anything else | `500` (logged with stack trace, never leaked to client) |

Service code translates raw database failures into domain errors — e.g.
`DataIntegrityViolationException` is inspected for SQLSTATE `23505` (unique violation) and
`23503` (FK violation) and re-thrown as a `ConflictException` with a readable message.

---

## API Reference

Base URL: `http://localhost:8080/api/v1`

### Interactive docs (Swagger UI)

| Resource | URL |
|---|---|
| Swagger UI | `http://localhost:8080/api/v1/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/api/v1/v3/api-docs` |

Every request body is pre-filled with a runnable example, and the document declares a
`bearerAuth` (JWT) security scheme, so the full flow can be exercised from the browser:

1. Call `POST /auth/login` with the seeded admin (`admin@pos.local` / `Admin@123`) and copy
   the `accessToken` from the response.
2. Click **Authorize** (top right) and paste the token — no `Bearer ` prefix needed.
3. Use **Try it out** on any endpoint; the token is sent automatically.

The docs are public by default so they work as a testing tool. Set `SWAGGER_ENABLED=false` to
disable both the UI and the OpenAPI JSON in production.

### Authentication — `/auth` (public)

| Method | Path | Body | Response |
|---|---|---|---|
| `POST` | `/auth/login` | `{ email, password }` | `200` `{ accessToken, refreshToken, tokenType, expiresInSeconds }` |
| `POST` | `/auth/register` | `{ email, password }` (min 8) | `200` tokens — first user → `owner`, later → `cashier` |
| `POST` | `/auth/refresh` | `{ refreshToken }` | `200` new token pair (rotated) |
| `POST` | `/auth/logout` | — | `204` clears stored refresh token |

```bash
# 1. Log in with the seeded admin
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@pos.local","password":"Admin@123"}'

# 2. Use the access token
curl http://localhost:8080/api/v1/products/search?keyword=coffee \
  -H "Authorization: Bearer <accessToken>"
```

### Products — `/products`

Class-level `@PreAuthorize("hasAuthority('products:read')")`.

| Method | Path | Requires | Description |
|---|---|---|---|
| `GET` | `/products/search?keyword=&page=&size=&sort=` | `products:read` | Case-insensitive search across name / SKU / barcode. Default page size 20, sorted `createdAt DESC` |
| `GET` | `/products/{id}` | `products:read` | Fetch one product (includes computed `profitMargin`) |
| `POST` | `/products` | `products:create` | Create → `201` + `Location` header |
| `PUT` | `/products/{id}` | `products:update` | Update |
| `DELETE` | `/products/{id}` | `products:delete` | Soft delete (`isActive = false`) |

### Categories — `/categories` (hierarchical)

| Method | Path | Requires | Description |
|---|---|---|---|
| `GET` | `/categories` | `categories:read` | Full tree (roots with nested children) |
| `GET` | `/categories/roots` | `categories:read` | Root categories only |
| `GET` | `/categories/by-parent/{parentId}` | `categories:read` | Direct children of a parent |
| `GET` | `/categories/{id}` | `categories:read` | One node as a tree |
| `POST` | `/categories` | `categories:create` | Create — auto-generates a unique `slug` |
| `PUT` | `/categories/{id}` | `categories:update` | Update |
| `DELETE` | `/categories/{id}` | `categories:delete` | Soft delete |

### Brands — `/brands`

| Method | Path | Requires | Description |
|---|---|---|---|
| `GET` | `/brands` | `brands:read` | All brands (including inactive) |
| `GET` | `/brands/active` | `brands:read` | Active brands only |
| `GET` | `/brands/{id}` | `brands:read` | One brand |
| `POST` | `/brands` | `brands:create` | Create — auto-generates a unique `slug` |
| `PUT` | `/brands/{id}` | `brands:update` | Update |
| `DELETE` | `/brands/{id}` | `brands:delete` | Soft delete |

### Units — `/units`

| Method | Path | Requires | Description |
|---|---|---|---|
| `GET` | `/units` | `units:read` | All units |
| `GET` | `/units/base` | `units:read` | Base units only (pcs, kg, …) |
| `GET` | `/units/{id}` | `units:read` | One unit |
| `POST` | `/units` | `units:create` | Create with `conversionFactor` / `baseUnitId` |
| `PUT` | `/units/{id}` | `units:update` | Update |
| `DELETE` | `/units/{id}` | `units:delete` | Hard delete |

### Tax classes — `/tax-classes`

| Method | Path | Requires | Description |
|---|---|---|---|
| `GET` | `/tax-classes` | `taxes:read` | All tax classes |
| `GET` | `/tax-classes/{id}` | `taxes:read` | One tax class |
| `POST` | `/tax-classes` | `taxes:create` | Create |
| `PUT` | `/tax-classes/{id}` | `taxes:update` | Update |
| `DELETE` | `/tax-classes/{id}` | `taxes:delete` | Hard delete |

### Response shape

Every response uses the same envelope: `success`, a human-readable `message`, and `data`.

```json
{
  "success": true,
  "message": "Product created",
  "data": {
    "id": "0192c1f4-...",
    "name": "Espresso Beans 1kg",
    "sku": "BEAN-ESP-1KG",
    "barcode": "8851001000015",
    "costPrice": 9.50,
    "sellingPrice": 15.00,
    "wholesalePrice": 12.00,
    "profitMargin": 36.67,
    "createdBy": "0192c1ab-...",
    "createdByUser": {
      "id": "0192c1ab-...",
      "name": "Shop Owner",
      "email": "owner@shop.com",
      "position": "Owner",
      "isActive": true
    }
  }
}
```

`createdByUser` / `updatedByUser` are resolved by `UserReferenceService`, which joins
`users` + `employees` in a **deduplicated bulk lookup** — no N+1 queries when listing records.

### Not yet exposed

The following domains exist fully in the database with repositories and entities, but have no
controllers yet: **sales/checkout, payments, refunds, exchanges, held orders, inventory,
warehouses, batches, serial numbers, stock counts, stock adjustments, customers, loyalty,
suppliers, purchase orders, purchase receipts, supplier payments, expenses, employees, shifts,
attendance, reports, settings, audit logs, backups**. Permissions for all of them are already
seeded, so the endpoints can be added without touching the RBAC setup.

---

## Configuration

Everything is environment-variable driven. `spring.config.import` loads `.env` directly as a
properties file, so no `.env` library is needed.

### Environment variables

| Variable | Default | Purpose |
|---|---|---|
| `SERVER_PORT` | `8080` | HTTP port |
| `MANAGEMENT_PORT` | `8098` | Actuator port |
| `SPRING_PROFILES_ACTIVE` | `dev` | Spring profile |
| `DB_HOST` / `DB_PORT` | `localhost` / `5434` | PostgreSQL host/port |
| `DB_NAME` / `DB_USERNAME` / `DB_PASSWORD` | `pos_db` / `pos_user` / — | Credentials (`DB_PASSWORD` **required**) |
| `DDL_AUTO` | `validate` | Hibernate schema mode — keep `validate` |
| `REDIS_HOST` / `REDIS_PORT` / `REDIS_PASSWORD` | `localhost` / `6379` / — | Redis |
| `MAIL_HOST` / `MAIL_PORT` / `MAIL_USERNAME` / `MAIL_PASSWORD` | `smtp.gmail.com` / `587` | SMTP |
| `JWT_SECRET` | — | **Required.** `openssl rand -base64 64` |
| `JWT_EXPIRATION` | `900000` | Access token ms (15 min) |
| `REFRESH_TOKEN_EXPIRATION` | `604800000` | Refresh token ms (7 days) |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:4200` | Frontend origins |
| `LOG_LEVEL` / `LOG_LEVEL_POS` / `SHOW_SQL` | `INFO` / `DEBUG` / `false` | Logging |
| `VIRTUAL_THREADS` | `true` | Java 21 virtual threads |
| `LAZY_INITIALIZATION` | `true` | Lazy bean startup |
| `HTTP2_ENABLED` | `true` | HTTP/2 |
| `MAIL_HEALTH_ENABLED` | `false` | Include mail in health check |
| `SHOP_NAME` / `SHOP_ADDRESS` / `SHOP_PHONE` / `SHOP_EMAIL` / `SHOP_TAX_ID` | placeholders | Store identity |
| `CURRENCY_SYMBOL` / `CURRENCY_CODE` | `$` / `USD` | Currency |
| `RECEIPT_HEADER` / `RECEIPT_FOOTER` | friendly defaults | Receipt text |
| `BARCODE_TYPE` | `CODE128` | Barcode symbology |
| `DEFAULT_PRINTER` / `KITCHEN_PRINTER` / `CASH_DRAWER_PORT` | empty / `COM1` | Hardware targets |

The `pos.shop.*`, `pos.currency.*`, `pos.receipt.*`, `pos.barcode.*`, `pos.printing.*` keys
exist so receipt rendering, barcode generation, and cash-drawer kicking can be added without
touching code — the config surface is already in place.

### Performance settings

| Setting | Value | Effect |
|---|---|---|
| HikariCP pool | max 20 / min idle 5 | 20 concurrent DB connections |
| `open-in-view` | `false` | No lazy-loading leakage into view rendering |
| Hibernate batch | size 50, ordered inserts/updates | Fewer round-trips on bulk writes |
| Slow query log | `> 25 ms` | Visible in `org.hibernate.SQL` |
| Cache | Redis, 1 h TTL, no null caching | Reference data (units, tax classes) |
| Compression | JSON/XML/HTML/text | Smaller POS payloads |
| Virtual threads | on | Cheap concurrency for blocking JDBC/Redis IO |

---

## Getting Started

### Prerequisites

- **Java 21+**
- **Docker + Docker Compose** (for PostgreSQL and Redis)
- `openssl` (to generate a JWT secret)

### 1. Configure environment

```bash
cd pos
cp .env.example .env
```

Edit `.env` and set at minimum:

```bash
POSTGRES_PASSWORD=<strong password>
DB_PASSWORD=<same strong password>
JWT_SECRET=<output of: openssl rand -base64 64>
```

### 2. Start infrastructure

```bash
docker compose up -d db redis
```

Postgres is on `localhost:5434`, Redis on `localhost:6379`. Both have health checks, so
`docker compose ps` should show `healthy`.

### 3. Run the app

```bash
./scripts/run-local.sh
```

or manually:

```bash
./mvnw spring-boot:run -DskipTests
```

With devtools (hot reload):

```bash
./mvnw spring-boot:run -Pdev
```

### 4. Verify

```bash
# Health (public, on the management port)
curl http://localhost:8098/actuator/health

# Log in
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@pos.local","password":"Admin@123"}'

# Browse the database (dev only)
open http://localhost:8080/api/v1/bootui
```

Flyway migrates automatically on startup — no manual schema step.

---

## Docker

`docker-compose.yml` runs the full stack:

```bash
cp .env.example .env      # then set POSTGRES_PASSWORD and JWT_SECRET
docker compose up --build -d
docker compose logs -f app
```

| Service | Image | Host port | Purpose |
|---|---|---|---|
| `db` | `postgres:17-alpine` | `5434 → 5432` | Database, persisted in the `pgdata` volume |
| `redis` | `redis:7-alpine` | `6379` | Cache, password-protected |
| `app` | built from `Dockerfile` | `8080`, `8098` | The API |

`app` waits for both `db` and `redis` to report healthy before starting.

The `Dockerfile` is a **multi-stage** build: `eclipse-temurin:21-jdk-alpine` resolves
dependencies and packages with tests skipped, then only the JAR is copied into
`eclipse-temurin:21-jre-alpine`. The runtime stage runs as a **non-root `spring` user**, exposes
8080/8098, sets a container `HEALTHCHECK` against `/actuator/health`, and sizes the heap with
`MaxRAMPercentage=75.0`. Log output is streamed to stdout and also written to `/app/logs`.

---

## Observability

- **Health** — `GET :8098/actuator/health` (public). Component detail is shown only to
  authenticated `ADMIN` users. Mail is excluded by default (`MAIL_HEALTH_ENABLED=false`) so a
  missing SMTP server doesn't fail the container health check.
- **Metrics** — `/actuator/metrics`, `/actuator/prometheus` (authenticated) for Prometheus
  scraping. HikariCP pool usage and JVM stats come for free.
- **Logs** — `logs/pos-system.log`, rolling at 10 MB with 30 files retained. Package level
  `com.den.pos` defaults to `DEBUG`; Spring Security and Hibernate stay at `INFO`.
- **Audit trail** — `audit_logs` records `user_id`, `action`, `entity_type`, `entity_id`,
  `old_values`/`new_values` JSON, `ip_address`, and `user_agent`, indexed on every lookup path.

---

## Testing

```bash
./mvnw test
```

Two test classes ship today:

| Test | Type | Verifies |
|---|---|---|
| `PosApplicationTests` | `@SpringBootTest` | The full Spring context boots |
| `MethodSecurityEnforcementTest` | `@WebMvcTest(ProductController)` | A principal with `products:read` gets `200`; a principal without it gets `403` — i.e. `@PreAuthorize` is genuinely enforced, not just present |

The security test is the important one: it is a regression guard against accidentally widening
access on any catalog endpoint.

---

## Design Decisions

**UUID v7 primary keys.** Time-ordered UUIDs generated by the database-friendly Hibernate
generator. They avoid key enumeration attacks that sequential integers invite, while staying
monotonic so B-tree index inserts remain sequential instead of scattering across the page cache.

**Optimistic locking over pessimistic.** A `@Version` column means two cashiers editing the same
product produce a clear conflict error on the second save, instead of the last write silently
winning.

**Soft delete with tombstones.** Deleting a product sets `isActive = false` (Hibernate
`@SoftDelete`) and records `deleted_at` / `deleted_by`. Sales history referencing that product
stays intact and auditable — you cannot delete your way out of a tax obligation.
`SoftDeletePurgeService` exists for eventual physical cleanup.

**Flyway owns the schema, Hibernate only validates it.** `ddl-auto: validate` means Hibernate
refuses to start if entities and migrations disagree, and no accidental `update` can mutate
production DDL. Every schema change is a reviewable, ordered migration.

**Permissions are authorities, not roles.** `@PreAuthorize("hasAuthority('products:create')")`
checks the exact permission string. Roles become bundles of permissions, so adding a
"shift supervisor" role later requires no controller changes.

**Numerically exact money.** `NUMERIC(12,2)` throughout, and `profit_margin` is a database
generated column — never a float, never a client-supplied value.

**Circular reference between employees and users.** A user logs in; an employee is on the
payroll; a user account belongs to an employee. Rather than forcing a direction, both FKs exist
and V1 resolves the cycle with an `ALTER`.

**Deliberate hard deletes on lookup tables.** Units and tax classes are hard-deleted because
they are referenced by `ON DELETE RESTRICT` from products; the database will refuse if in use,
which is safer than silently deactivating a unit that existing products point at. Catalog
entities (products, categories, brands) are soft-deleted because they appear on historical
documents.

---

## Roadmap

The foundation is in place. Ordered by value:

1. **Sales & checkout** — cart → sale creation, split payments, invoice numbering, receipt rendering
2. **Inventory APIs** — stock movements on sale/receipt, stock counts, adjustments with approval
3. **Customer & loyalty APIs** — customer CRUD, points earn/burn on sale
4. **Purchasing APIs** — purchase orders, receiving, supplier balances
5. **Expense APIs** — with approval workflow and receipt upload
6. **HR APIs** — employees, shifts with cash-variance tracking, attendance
7. **Reports** — sales by day/cashier/product, inventory valuation, profit margin, tax summaries
8. **Audit log capture** — an AOP aspect or entity listener to populate `audit_logs` automatically
9. **Refresh-token hashing** — store a hash of the refresh token instead of the token itself, so a
   database leak cannot be replayed
10. **File storage** — product images, expense receipts (currently only URLs are stored)
11. **Account lockout** — `login_attempts` / `locked_until` columns exist and `CustomUserDetailsService`
    reads them, but failed-login increment logic is not yet wired
12. **Hardware integration** — receipt/label printing, barcode rendering, cash-drawer kick, using the
    existing `pos.printing.*` / `pos.barcode.*` config
13. **Multi-shop** — `shop_info` is single-row today; adding a `shop_id` to business tables would enable it

---

## License

Proprietary — all rights reserved.
