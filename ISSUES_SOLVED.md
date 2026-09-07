# CRM Application - Issues Solved

## 1. ZUL files reference "Car Inventory" instead of Customer fields

**Cause:** The ZUL files (`customer.zul` and `details.zul`) were copied from a car inventory application. They referenced properties like `vm.car`, `vm.cars`, `make`, `model`, `year`, `price` — none of which exist in the `CustomerViewModel` or `CustomerEntity`.

**Solution:** Replaced all car-related bindings, labels, columns, and form fields with customer fields (`firstName`, `lastName`, `email`, `phoneNumber`, `companyName`, `tinNumber`, `nationalIdentification`) in both ZUL files.

---

## 2. `zkspringboot-starter` declared as `<type>pom</type>`

**Cause:** In `pom.xml`, the ZK Spring Boot starter dependency had `<type>pom</type>`. This tells Maven to only import dependency management (BOM) but not include the actual JAR. The ZK auto-configuration, servlet registration, and all ZK libraries were never loaded — resulting in 404 errors when accessing the app.

**Solution:** Removed `<type>pom</type>` from the dependency so it's a regular jar dependency:

```xml
<!-- Before -->
<dependency>
    <groupId>org.zkoss.zkspringboot</groupId>
    <artifactId>zkspringboot-starter</artifactId>
    <type>pom</type>
    <version>${zkspringboot.version}</version>
</dependency>

<!-- After -->
<dependency>
    <groupId>org.zkoss.zkspringboot</groupId>
    <artifactId>zkspringboot-starter</artifactId>
    <version>${zkspringboot.version}</version>
</dependency>
```

---

## 3. `zk.homepage` and redirect paths had wrong prefix

**Cause:** The `zk.homepage` property and `Executions.sendRedirect()` calls used inconsistent URL paths. After investigation, the original `/zkau/web/` prefix was correct for ZK's URL scheme (the ZK Au Engine servlet maps at `/zkau/*`).

**Solution:** Kept the original `/zkau/web/zul/customer/` path format for all redirects and the homepage configuration.

---

## 4. Blank line before XML declaration in `customer.zul`

**Cause:** `customer.zul` had a blank line (line 1) before the `<?xml version="1.0"?>` declaration on line 2. ZK's XML parser requires the XML declaration to be the very first content in the file. This caused:

```
SAXParseException: The processing instruction target matching "[xX][mM][lL]" is not allowed.
```

**Solution:** Removed the blank line so `<?xml version="1.0" encoding="UTF-8"?>` is on line 1.

---

## 5. Missing closing parenthesis in `@init()` expression

**Cause:** Both ZUL files had a missing `)` in the `BindComposer` `@init` expression:

```xml
<!-- Before (broken) -->
viewModel="@id('vm') @init('com.example.crm.customer.viewmodel.CustomerViewModel'"

<!-- After (fixed) -->
viewModel="@id('vm') @init('com.example.crm.customer.viewmodel.CustomerViewModel')"
```

Without the closing `)`, the `BindComposer` failed to initialize properly, and ZK tried to set `viewModel` as a regular property on `Window`:

```
Method setViewModel not found for class org.zkoss.zul.Window
```

**Solution:** Added the missing `)` in both `customer.zul` and `details.zul`.

---

## 6. `CustomerViewModel` incompatible with `DelegatingVariableResolver`

**Cause:** The ViewModel used `@RequiredArgsConstructor` with `final` fields (`customer`, `search`). The `DelegatingVariableResolver` requires a no-arg constructor to instantiate the ViewModel via reflection. The `@RequiredArgsConstructor` generated only an all-args constructor, preventing instantiation.

**Solution:** Removed `@RequiredArgsConstructor` and the `final` modifier from fields. Changed to `@Getter`/`@Setter` only, allowing ZK to create an instance via no-arg constructor and inject `@WireVariable` dependencies afterward.

---

## 7. Missing `@Init` method — data never loaded on page open

**Cause:** The original ViewModel had no `@Init` method. When a page loaded, no data was fetched — the list was always empty and the form had no customer to edit.

**Solution:** Added an `@Init` method with `@QueryParam` parameters to handle three use cases:

- **List page** (no params): calls `refresh()` to load paginated customer list
- **Add form** (`add=true`): creates a new empty `CustomerEntity`
- **Edit form** (`customerId=xxx`): loads existing customer by ID

---

## 8. `customer` field is null — NullPointerException on save

**Cause:** When clicking "Add Customer", the `add()` command navigated to `details.zul` without any query parameters. The `@Init` method received `null` for `customerId`, and without a way to distinguish "list page" from "add form", it failed to initialize the `customer` object. Clicking Save then called `customer.getId()` on a null reference.

**Solution:** Two-part fix:

1. Updated `add()` to pass `?add=true` query parameter
2. Updated `@Init` to check for the `add` parameter and create a new `CustomerEntity`:

```java
@Init
public void init(@QueryParam("customerId") String customerId, @QueryParam("add") String add) {
    if (customerId != null && !customerId.isEmpty()) {
        UUID id = UUID.fromString(customerId);
        customer = customerQueryService.findCustomerByIdAndState(id, EEntityLifeCycle.ACTIVE).getData();
    } else if ("true".equals(add)) {
        customer = new CustomerEntity();
    } else {
        refresh();
    }
}
```

---

## 9. Missing ViewModel commands

**Cause:** The original ViewModel only had `add()` and `search()` commands. The ZUL files referenced commands (`edit`, `delete`, `save`, `cancel`, `prevPage`, `nextPage`) that didn't exist.

**Solution:** Added all missing `@Command` methods with proper `@BindingParam`, `@NotifyChange`, and `@QueryParam` annotations to support the full CRUD workflow.

---

## 10. `updateCustomer()` only saved 2 of 7 fields

**Cause:** The `CustomerService.updateCustomer()` method only copied `email` and `nationalIdentification` from the form entity to the database entity, ignoring `firstName`, `lastName`, `phoneNumber`, `companyName`, and `tinNumber`.

**Solution:** Updated the method to copy all editable fields:

```java
found.setEmail(customerEntity.getEmail());
found.setNationalIdentification(customerEntity.getNationalIdentification());
found.setFirstName(customerEntity.getFirstName());
found.setLastName(customerEntity.getLastName());
found.setPhoneNumber(customerEntity.getPhoneNumber());
found.setCompanyName(customerEntity.getCompanyName());
found.setTinNumber(customerEntity.getTinNumber());
```
