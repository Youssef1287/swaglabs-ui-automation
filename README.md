# Ecommerce Automation Framework

A Web UI Test Automation Framework built using Java 21, Selenium WebDriver, TestNG, Maven, and Allure Reports.

This project automates end-to-end user journeys on [SauceDemo](https://www.saucedemo.com/), following a maintainable framework architecture with Page Object Model (POM), reusable UI components, and business flows.

## Tech Stack

- **Programming Language:** Java 21
- **Automation Tool:** Selenium WebDriver 4.52.0
- **Test Framework:** TestNG 7.12.0
- **Build Tool:** Maven
- **Reporting:** Allure Reports 3.17.0
- **Logging:** Log4j2
- **Design Pattern:** Page Object Model (POM)
- **Driver Management:** ThreadLocal WebDriver
- **AOP Integration:** AspectJ
- **CI/CD:** GitHub Actions
- **IDE:** IntelliJ IDEA

## Project Structure

```text
Ecommerce-Automation-Framework
│
├── .github
│   └── workflows
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.swaglabs
│   │   │       ├── config
│   │   │       ├── core
│   │   │       ├── data
│   │   │       ├── pages
│   │   │       ├── components
│   │   │       ├── flows
│   │   │       └── utils
│   │   │
│   │   └── resources
│   │       └── settings
│   │
│   └── test
│       └── java
│           └── com.swaglabs
│               ├── support
│               ├── listeners
│               └── tests
│
├── pom.xml
├── testng.xml
├── testng-sequential.xml
└── README.md
```

## Framework Features

- Page Object Model (POM) architecture.
- ThreadLocal WebDriver management.
- Support for Chrome, Edge, and Firefox.
- Headless browser execution.
- Selenium Manager for browser driver management.
- Explicit Waits for reliable synchronization.
- Fluent Page Objects.
- Reusable UI components and business flows.
- Centralized configuration management.
- Environment-specific configuration with system property overrides.
- Enum-based test data management.
- TestNG Factory for account-based test execution.
- Automatic screenshots on test failures.
- Optional desktop video recording for failed tests.
- Allure reporting with test evidence.
- Log4j2 logging integration.
- GitHub Actions CI/CD integration.
- Support for parallel test execution.

## What is Covered

| Area | Tests |
|------|-------|
| Login | Accepted accounts, invalid credentials, empty fields, locked-out user |
| Product List | Product listing, cart badge, sorting by name and price, cart icon navigation |
| Product Details | View product details, add to cart, remove from cart, back navigation |
| Cart | Cart contents, remove items, continue shopping, checkout navigation |
| Checkout | Customer information validation, order total, finish/cancel checkout, order confirmation, menu, logout |

## Test Execution

### Prerequisites

- Java JDK 21 or higher.
- Maven 3.9.14 or higher.
- Chrome, Edge, or Firefox.
- IntelliJ IDEA or another Java IDE.

### Clone the Repository

```bash
git clone https://github.com/Youssef1287/Ecommerce-Automation-Framework.git
```

Navigate to the project directory:

```bash
cd Ecommerce-Automation-Framework
```

### Run Tests

Run the default test suite:

```bash
mvn clean test
```

Run tests using Edge in headless mode:

```bash
mvn clean test -Dbrowser=edge -Dheadless=true
```

Run tests with QA environment configuration:

```bash
mvn clean test -Denv=qa
```

Run tests sequentially:

```bash
mvn clean test -Dsuite.file=testng-sequential.xml
```

Enable video recording:

```bash
mvn clean test -Drecord.video=true
```

## Test Reports

This framework integrates Allure Reports for detailed test execution reporting.

Generate and open the Allure report:

```bash
mvn allure:serve
```

Failed tests automatically include screenshots in the Allure report.

## Video Recording

The framework supports optional desktop video recording during test execution.

- Recording can be enabled using `-Drecord.video=true`.
- Videos are attached to Allure reports only when tests fail.
- Videos of passed tests are automatically deleted.
- FFmpeg can be used to convert recordings to MP4.
- Video recording requires a visible desktop environment.

## CI/CD Integration

The project integrates GitHub Actions to automate test execution.

The workflow supports browser-based test execution and optional video recording.


---

**Author:** Youssef
