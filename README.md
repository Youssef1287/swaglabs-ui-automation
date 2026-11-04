# Swag Labs UI Tests

End-to-end UI tests for [saucedemo.com](https://www.saucedemo.com) written with
Selenium WebDriver, TestNG and Allure.

## What is covered

| Area | Tests |
|------|-------|
| Login | accepted accounts, wrong credentials, empty fields, locked-out user |
| Product list | listing, cart badge, sorting (name and price), cart icon |
| Product details | view, add, remove, back |
| Cart | contents, remove, continue shopping, checkout |
| Checkout | customer info validation, order total, finish/cancel, confirmation, menu, logout |

Signed-in shopping and checkout tests run with the **standard** account. The suite is designed
as a stable regression baseline; it does not include the demo site's intentionally defective
problem account.

## Run it

Requirements: JDK 21+, Maven 3.9.14+, and Chrome (or Edge/Firefox). Drivers are downloaded by Selenium Manager.

The project targets Java 21 (the JDK patch version can be newer, e.g. 21.0.10) and uses Selenium 4.52.0, TestNG 7.12.0, Allure TestNG 3.0.1 / Allure Report 3.17.0, AspectJ 1.9.26, and Log4j2 2.26.1.

Check your local tools before running:

```bash
java -version
javac -version
mvn -version
```

```bash
mvn clean test                                   # Chrome, visible window
mvn clean test -Dbrowser=edge -Dheadless=true    # Edge, no window
mvn clean test -Denv=qa                          # overlay settings/qa.properties
mvn clean test -Dsuite.file=testng-sequential.xml   # one browser at a time (baseline, and required for video)
mvn clean test -Drecord.video=true               # record the desktop (kept only for failed tests, see below)
```

Every setting in `src/main/resources/settings/default.properties` can be overridden with `-D`.

### Report

```bash
mvn allure:serve
```

Or install the [Allure CLI](https://allurereport.org/docs/install/) and run with
`-Dallure.auto.report=true` to build and open the report after the run.
Failed tests get a screenshot attached automatically.

### Video recording

`-Drecord.video=true` films the whole desktop while a test runs and attaches the video to the
Allure report **only when the test fails** (videos of passed tests are deleted).

- The recording starts with the first `@BeforeMethod`, so it also shows the browser start-up and the sign-in.
- Videos are named after the class, account, method and data-provider row, so runs of the same method never overwrite each other.
- If [ffmpeg](https://ffmpeg.org/download.html) is on the `PATH`, the video is converted to MP4 and plays inside the report.
  Without ffmpeg the original AVI is attached and has to be downloaded.
- It needs a display: with `-Dheadless=true` (or no screen) it is skipped with a warning.
- On GitHub Actions, run the workflow manually and tick **record_video**. The Linux job then runs under Xvfb
  with a visible browser and installs ffmpeg; the Windows job has a desktop already.

## Project layout

```
src/main/java/com/swaglabs
  config/       Settings (properties + -D overrides), Route (page URLs)
  core/         Browser (enum of supported browsers), DriverManager (thread-local driver)
  data/         UserAccount, Product, SortOption, CheckoutInfo
  pages/        Page objects; actions return the page you land on
  components/   Header and SideMenu, shared by several pages
  flows/        CheckoutFlow - reusable journeys through the real UI
  utils/        Evidence (screenshots), VideoRecorder, VideoConverter, AllureReport
src/test/java/com/swaglabs
  support/      BaseTest, SignedInTest, UserProviders
  listeners/    ResultListener
  tests/        auth, shopping, checkout
testng.xml      Suite definition (and listener registration)
```

## Design notes

- **Fluent page objects.** `loginPage.loginAs(STANDARD)` returns an `InventoryPage`,
  `inventory.header().openCart()` returns a `CartPage`, and so on. A page waits for its own URL
  when it is created, so a wrong navigation fails right where it happens.
- **Explicit waits only.** No implicit waits, no `Thread.sleep`.
- **Data in enums, not strings.** Products, accounts and sort options are enums; element ids
  are derived from the product, so there is one locator per action instead of one per product.
- **One test class, several accounts.** `SignedInTest` takes the account in its constructor and
  the classes use a TestNG `@Factory`, keeping account setup centralized without duplicating test classes.
- **Real flows.** Checkout tests walk through the UI (`CheckoutFlow`) instead of jumping to URLs.
