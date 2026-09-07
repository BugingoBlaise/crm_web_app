# CRM Web Application

A customer relationship management (CRM) web application built with **Spring Boot 4** and **ZK Framework 10** using the MVVM pattern. Provides a server-rendered UI for managing customers with full CRUD, search, pagination, and soft-delete.

## Tech Stack

- **Java 25** / Spring Boot 4.1.1
- **ZK Framework 10.0.0** (Jakarta) with ZK Spring Boot Starter 3.2.7.1
- **Spring Data JPA** + Hibernate
- **PostgreSQL** (database: `crm_db`)
- **Lombok**
- **Tailwind CSS** (CDN) + custom `crm.css`

## Project Structure

```
src/main/java/com/example/crm/
├── CrmApplication.java                     # Spring Boot entry point
├── base/
│   ├── AbstractBaseEntity.java             # [Deprecated] UUID + lifecycle base entity
│   ├── AuditEntity.java                    # [Deprecated] Audit fields base entity
│   ├── IMessage.java                       # Status code constants
│   └── Response.java                       # Generic response wrapper DTO
└── customer/
    ├── domain/
    │   ├── CustomerEntity.java             # JPA entity (customer_entity table)
    │   └── EEntityLifeCycle.java           # Lifecycle enum: CREATED, ACTIVE, APPROVED, DEACTIVATED
    ├── repository/
    │   └── ICustomerRepository.java        # Spring Data JPA repository with search
    ├── service/
    │   ├── CustomerService.java            # Write operations (create, update)
    │   └── CustomerQueryService.java       # Read operations (find, list, search, paginate)
    └── viewmodel/
        └── CustomerViewModel.java          # ZK MVVM ViewModel (UI controller)

src/main/resources/
├── application.properties                  # Database + ZK config
└── web/
    ├── styles/crm.css                      # Custom CSS overrides
    └── zul/customer/
        ├── customer.zul                    # Customer list view (table, search, pagination)
        └── details.zul                     # Customer add/edit form
```

## Features

- **Customer CRUD** — create, view, edit, and soft-delete customers
- **Soft Deletes** — customers are deactivated (state set to `DEACTIVATED`), never physically removed
- **Search** — case-insensitive search across first name, last name, and company name
- **Pagination** — server-side pagination with configurable page size (default: 10)
- **Customer Number Generation** — random number assigned on creation (placeholder for sequential numbering)
- **Lifecycle States** — `CREATED → ACTIVE → APPROVED / DEACTIVATED`

## Customer Entity Fields

| Field | Column | Type | Constraints |
|---|---|---|---|
| `id` | `id` | UUID | PK, auto-generated |
| `customerNumber` | `customer_number` | double | NOT NULL, UNIQUE |
| `firstName` | `first_name` | String | NULLABLE |
| `lastName` | `last_name` | String | NULLABLE |
| `email` | `email` | String | NULLABLE, UNIQUE |
| `phoneNumber` | `phone_number` | String | NOT NULL, UNIQUE |
| `companyName` | `company_name` | String | NULLABLE, UNIQUE |
| `tinNumber` | `tin_number` | String | NULLABLE, UNIQUE |
| `nationalIdentification` | `national_identification` | String(100) | NULLABLE, UNIQUE |
| `state` | `customer_state` | EEntityLifeCycle | DEFAULT ACTIVE |

## Architecture

### MVVM Pattern with Spring DI

This application uses ZK's **MVVM (Model-View-ViewModel)** pattern integrated with Spring Boot's dependency injection:

```
ZUL View (customer.zul / details.zul)
    ↕  MVVM bindings (@bind, @command, @load)
CustomerViewModel (@Init, @Command, @NotifyChange)
    ↓  method calls
CustomerService / CustomerQueryService (@Service)
    ↓  JPA calls
ICustomerRepository (Spring Data JPA)
    ↓  Hibernate
PostgreSQL (crm_db)
```

- **`BindComposer`** connects ZUL views to the ViewModel
- **`@VariableResolver(DelegatingVariableResolver.class)`** bridges ZK and Spring for DI
- **`@WireVariable`** injects Spring beans into the ViewModel
- **`@NotifyChange`** triggers selective UI re-rendering
- **`@QueryParam`** extracts URL parameters for page-mode routing (list / add / edit)

### No REST Layer

This is a pure **ZK server-side rendering** application. There are no REST controllers or JSON APIs. The browser communicates with the server via ZK's AU (Asynchronous Update) engine at `/zkau/*`.

## URL Routes

| Path | Purpose |
|---|---|
| `/` | Redirects to customer list |
| `/zkau/web/zul/customer/customer.zul` | Customer list (table, search, pagination) |
| `/zkau/web/zul/customer/details.zul?add=true` | Add new customer form |
| `/zkau/web/zul/customer/details.zul?customerId={uuid}` | Edit existing customer form |

## Database Configuration

Configured in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Requires a running PostgreSQL instance with a `crm_db` database. Hibernate auto-creates/updates the schema on startup.

## Getting Started

### Prerequisites

- Java 25
- Maven 3.8+
- PostgreSQL running on `localhost:5432`

### Setup

1. Create the database:
   ```sql
   CREATE DATABASE crm_db;
   ```

2. Build and run:
   ```bash
   ./mvnw spring-boot:run
   ```

3. Open [http://localhost:8080](http://localhost:8080) — redirects to the customer list.

## ViewModel Commands

| Command | Trigger | Behavior |
|---|---|---|
| `add()` | "+ Add Customer" button | Redirects to add form |
| `search()` | Search box Enter / button | Resets to page 0, refreshes list |
| `edit(id)` | "Edit" button per row | Redirects to edit form with customer ID |
| `delete(id)` | "Delete" button per row | Soft-deletes (sets state to DEACTIVATED), refreshes list |
| `save()` | "Save Customer" on form | Creates or updates customer, redirects to list |
| `cancel()` | "Cancel" on form | Redirects back to list |
| `prevPage()` | "Prev" pagination button | Previous page |
| `nextPage()` | "Next" pagination button | Next page |
