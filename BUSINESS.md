# POS — Business Overview

A business-oriented introduction to the Point of Sale / retail management platform: what it is,
who it is for, what problem it solves, and how it creates value.

> This document is the **business companion** to the technical [`README.md`](./README.md). It
> focuses on the *why* and the *who*, not the code. Figures such as pricing and market sizing are
> **illustrative assumptions** to be validated — they are marked as such.

---

## Table of Contents

- [Executive Summary](#executive-summary)
- [The Business Problem](#the-business-problem)
- [Our Solution](#our-solution)
- [Target Market & Customer Segments](#target-market--customer-segments)
- [Customer Personas](#customer-personas)
- [What the Product Does (Business Capabilities)](#what-the-product-does-business-capabilities)
- [Business Use Cases](#business-use-cases)
- [Value Proposition & ROI](#value-proposition--roi)
- [Product Maturity](#product-maturity)
- [Business Model & Monetization](#business-model--monetization)
- [Competitive Landscape & Positioning](#competitive-landscape--positioning)
- [Delivery & Operating Model](#delivery--operating-model)
- [Key Business Metrics (KPIs)](#key-business-metrics-kpis)
- [Risks & Considerations](#risks--considerations)
- [Roadmap (Business View)](#roadmap-business-view)

---

## Executive Summary

**The product** is a modern Point of Sale (POS) and retail management **backend platform**. It
is the software engine that powers everything a retail business does: selling at the counter,
tracking stock across warehouses, ordering from suppliers, managing staff, and understanding
profit.

**The opportunity (assumption to validate):** small and medium-sized retailers — especially in
emerging markets — still run on paper, spreadsheets, and disconnected tools. They lack a single
reliable system of record, which costs them money through stock-outs, shrinkage, pricing errors,
and untracked expenses.

**The proposition:** deliver an affordable, secure, exact-accounting POS platform that a
business can trust with its money and its inventory — and that developers can build beautiful
front ends on top of, because the entire retail domain is already modeled in one clean,
API-first system.

**Current state:** the platform's foundational layer (complete retail data model + security and
access control) is built. The catalog module is live via API; the remaining business modules are
modeled and ready to be exposed. This is a **strong technical foundation seeking productization
and go-to-market**, not a finished commercial product.

---

## The Business Problem

Retail is operationally hard, and most small businesses pay for it every day:

1. **Disconnected systems.** Sales are recorded in one place, stock in another, supplier orders
   in someone's head. Numbers never agree, and nobody trusts them.
2. **Invisible inventory.** Without movement-level tracking, businesses cannot explain their stock.
   Goods "disappear" (shrinkage), bestsellers go out of stock, and dead stock ties up cash.
3. **Pricing and margin blindness.** Owners often don't know their true profit per product once
   cost, tax and discounts are accounted for.
4. **Loss of accountability.** With shared logins (or none), there is no record of who discounted a
   sale, voided a line, or took cash from the till.
5. **Compliance and audit exposure.** Paper trails make tax reporting and audits painful and risky.
6. **Scaling friction.** What works for one shop breaks at three; manual processes don't scale.

Existing tools often solve one slice (a cash register, or an inventory app) and demand a
proprietary end-to-end lock-in, with pricing and hardware requirements beyond what a small shop
can bear.

---

## Our Solution

A single, API-first platform that acts as the **operational system of record** for a retail
business across nine connected domains:

| Domain | Business outcome |
|---|---|
| **Point of Sale** | Fast, accurate checkout with split payments, held orders, refunds and exchanges |
| **Catalog** | One trusted product catalogue with variants, images, pricing and tax rules |
| **Inventory** | Explainable stock: every movement tracked, batches/serials, counts and adjustments |
| **Purchasing & Suppliers** | Controlled procurement: orders, receiving, and supplier balances |
| **Customers & Loyalty** | Repeat business through profiles, history and a loyalty points program |
| **Expenses** | Real cost tracking with approval workflows and receipt records |
| **Human Resources** | Staff, shifts (with cash variance), and attendance |
| **Governance** | Audit trails, role-based permissions, backups and store settings |
| **Security** | Login, tokens, and granular permission control for every action |

Three business principles differentiate it:

- **Trustworthy by design** — exact decimal money, database-computed margins, and historical
  prices preserved on every past sale.
- **Accountable by design** — every action is authenticated, permissioned, and stamped with who
  did it and when.
- **Open by design** — it is a clean REST API, not a closed box. Businesses and integrators can
  build any front end, connect any device, and own their data.

---

## Target Market & Customer Segments

**Primary segment — single-store retailers**
Independent shops that have outgrown paper and spreadsheets but cannot afford or justify
enterprise ERP. Examples: convenience stores, cafés, pharmacies, boutique apparel, electronics
kiosks.

**Secondary segment — small chains (2–10 stores)**
Businesses needing consistent pricing, centralized stock visibility, and reporting across
locations. *(Note: multi-store is on the roadmap; today the platform models a single shop.)*

**Tertiary segment — developers & integrators**
Agencies and software teams who need a ready-built, well-structured retail backend they can
white-label or extend instead of building from scratch.

**Underserved geographies (assumption):** markets such as Southeast Asia, where local payment
methods, cash-heavy operations, and price sensitivity make heavyweight Western POS suites a poor
fit — while demand for digital tools is rising sharply.

---

## Customer Personas

| Persona | Role | What they need | How the platform helps |
|---|---|---|---|
| **Dara, the owner** | Runs the business | Profit clarity, control, less admin | Reports, margins, audit trail, one source of truth |
| **Sophea, the cashier** | Sells at the counter | Speed and reliability | Fast search/barcode checkout, safe conflict handling |
| **Sokha, the stock clerk** | Manages inventory | Accurate stock, easy receiving | Movement tracking, counts, adjustments, batch/serial |
| **Maly, the manager** | Supervises staff & shifts | Accountability, scheduling | Permissions, shift cash variance, attendance |
| **Vibol, the developer** | Builds the front end | A real backend, documented | API-first, JWT-secured, full domain modeled |

---

## What the Product Does (Business Capabilities)

- **Sell anything** — products, weighted goods, variants, and services.
- **Get paid any way** — cash, card, wallet, and split payments on a single sale.
- **Serve tables and park baskets** — held orders for restaurants and busy counters.
- **Correct mistakes safely** — proper refunds and exchanges with item-level detail.
- **Know your stock exactly** — per-warehouse quantities, reserved vs. available, min/max alerts.
- **Track what expires and what is unique** — batches (FEFO-ready) and serial numbers.
- **Buy smarter** — supplier terms, credit limits, purchase orders, and goods receiving.
- **Grow loyalty** — customer profiles, purchase history, and a points ledger with tiers.
- **Control costs** — categorized, approvable expenses with receipt records and recurring bills.
- **Manage people** — employee records, cash-register shifts with variance, and attendance.
- **Govern everything** — roles, permissions, audit logs, notifications and backups.

---

## Business Use Cases

**1. Independent café**
Full POS with table service (held orders), split payments, recipe/ingredient stock, and loyalty
cards — replacing a cash register and a paper loyalty book.

**2. Neighbourhood pharmacy**
Batch and expiry tracking (FEFO) to sell older stock first and avoid expired losses, plus
prescription-customer profiles and supplier ordering.

**3. Electronics kiosk**
Serial-number tracking so every individual device (and its warranty) is traceable from purchase
to sale to return.

**4. Boutique apparel**
Variant matrix (size × colour), purchase orders across multiple suppliers, and margin reporting
per collection.

**5. Multi-department convenience store**
Category tree, multiple warehouses (front-of-store / storeroom), stock counts, and shrinkage
visibility per department.

---

## Value Proposition & ROI

**Where the money is made (illustrative, to be validated with real customers):**

| Value lever | How the platform captures it |
|---|---|
| **Reduced shrinkage** | Movement-level tracking makes loss visible and attributable |
| **Fewer stock-outs on bestsellers** | Min/max thresholds + reorder via purchasing |
| **Better prices / margins** | Per-product margin visibility drives smarter pricing |
| **Less expired/wasted stock** | Batch + FEFO support |
| **Labour efficiency** | Faster checkout and automated records free up staff time |
| **Error and fraud reduction** | Permissions, shift variance, and audit trails |
| **Tax and audit readiness** | Clean, timestamped, extractable records |

**Why it wins against doing nothing:** a spreadsheet cannot enforce permissions, track stock
movements, or price a split payment correctly. Every day without a system of record is a day of
unknowable loss.

---

## Product Maturity

An honest view of where the product stands, framed for business planning:

| Layer | Status | Business meaning |
|---|---|---|
| Retail data model (all 9 domains) | ✅ Built | The hard schema design is done — low rework risk |
| Security, RBAC, audit foundations | ✅ Built | Enterprise-grade access control is ready |
| Catalog module (products, categories, brands…) | ✅ Live via API | First sellable module exists |
| POS, inventory, purchasing, customers, HR, reports | 🟡 Modeled, API pending | Delivery effort is incremental, not greenfield |

**Implication:** the remaining work is *product surface* (APIs and front ends) built on an
already-solid foundation — a favourable position for investment and delivery planning.

---

## Business Model & Monetization

*Options to evaluate — none are committed:*

| Model | Description | Best when |
|---|---|---|
| **Subscription (SaaS)** | Monthly fee per store/register, tiered by features | Predictable recurring revenue |
| **One-time license + support** | Perpetual license, paid updates | On-premise / low-connectivity markets |
| **Freemium → paid** | Free single-register core, paid multi-store/reports | Fast user acquisition |
| **Usage-based add-ons** | SMS, loyalty, extra storage/backups billed usage | Aligning cost with value |
| **White-label / OEM** | License the backend to agencies and vendors | High-margin B2B channel |
| **Services** | Implementation, data migration, training, hardware bundling | Higher revenue per customer early on |

**Suggested packaging (illustrative):**
- *Basic* — POS + catalog + one register.
- *Standard* — adds inventory, purchasing, customers/loyalty.
- *Pro* — adds multi-store, advanced reports, audit and backups.

---

## Competitive Landscape & Positioning

| Category | Typical players | Gap we address |
|---|---|---|
| Legacy desktop POS | Traditional register software | Closed, dated, no API, weak reporting |
| Generic SaaS POS | Cloud register apps | Often consumer-simple, weak inventory/back office |
| Enterprise ERP | Large suites | Too expensive and heavy for SMBs |
| Spreadsheets/paper | Manual | No accountability, no automation, error-prone |

**Positioning statement:**
> For small and growing retailers who need to trust their numbers, **[the platform]** is a
> secure, API-first retail management backend that tracks every sale and every stock movement
> with exact accounting — unlike closed POS boxes, it can power any front end and belongs to the
> business that runs it.

**Differentiators:** exact money handling, movement-level inventory, granular RBAC + audit,
developer-friendly open API, and a foundation designed for the whole retail domain from day one.

---

## Delivery & Operating Model

- **Cloud (SaaS):** containerized, ready to deploy; health checks, metrics, and migrations
  automated. Lowest operational burden for customers.
- **On-premise / hybrid:** runs on a single server or the shop's own machine, useful for
  cash-heavy or low-connectivity environments and data-sovereignty requirements.
- **White-label / API-only:** partners embed the backend into their own product.

Operational strengths already built in: automated database migrations, non-root hardened
containers, health/metrics endpoints, structured logging, and configurable store identity,
currency, receipts and hardware — so deployment and localization are low-effort.

---

## Key Business Metrics (KPIs)

**Adoption:** stores onboarded · active registers · daily transactions · staff accounts.
**Engagement:** sales/day per store · loyalty members enrolled · purchase orders raised.
**Retention:** monthly churn · logo retention · expansion to higher tiers.
**Value delivered:** measured shrinkage reduction · stock-out frequency · average margin.
**Commercial:** MRR/ARR · CAC · LTV · gross margin · payback period.
**Quality:** uptime · checkout latency · support tickets per store.

---

## Risks & Considerations

| Risk | Notes / mitigation direction |
|---|---|
| **Incomplete product surface** | Core modules are modeled, not exposed; execution is the main task |
| **Single-shop limitation** | Multi-store requires schema work (roadmap item) for chain deals |
| **Payment integration gaps** | Records payments but doesn't process them; needs partner integrations |
| **Hardware variability** | Receipt/kitchen printers and cash drawers need device testing |
| **No bundled front end** | A UI must be built or partnered to sell to non-technical shops |
| **Market education** | SMB retailers may need onboarding/support investment |
| **Data migration** | Importing legacy data is a recurring services cost |
| **Regulatory/localization** | Tax rules, currencies and languages must be adaptable per market |

---

## Roadmap (Business View)

- **Phase 1 — Complete the core product:** expose sales/checkout, inventory, and purchasing APIs;
  ship a first-party front end. *(Unlocks first real customers.)*
- **Phase 2 — Deepen value:** customers & loyalty, expenses, HR, and reporting. *(Drives retention
  and tier upgrades.)*
- **Phase 3 — Scale:** multi-store support, hardware integrations, payment partnerships, and
  localization. *(Enables chains and new geographies.)*
- **Phase 4 — Platform:** white-label/OEM offering and a public integration ecosystem.

> Detailed, engineering-level sequencing lives in the [technical README's Roadmap](./README.md#roadmap).

---

## License

Proprietary — all rights reserved.
