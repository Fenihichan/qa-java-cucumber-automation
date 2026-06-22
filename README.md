# QA Java Cucumber Automation

This repository contains automated tests for three areas:

- Web UI tests with Selenium and Cucumber
- Mobile tests with Appium and Cucumber
- API tests with REST Assured and Cucumber

## Prerequisites

Install the following tools before running the tests:

- Java Development Kit 17
- Apache Maven 3.9 or newer
- Google Chrome
- Android Studio with Android SDK and an Android emulator, or a physical Android device
- Appium Server 2.x

## Project Requirements

### Web tests

- Chrome is required because the tests use `ChromeDriver`
- `WebDriverManager` downloads and manages the correct browser driver automatically
- Internet access is needed because the tests open `https://app.bibit.id`

### Mobile tests

- Appium Server must be running locally
- The project connects to `http://127.0.0.1:4723/wd/hub`
- The Appium server should be started with the `/wd/hub` base path, for example:

```bash
appium --base-path /wd/hub
```

- An Android emulator or device must be available
- The Sauce Labs demo app package `com.saucelabs.mydemoapp.android` must already be installed on the device/emulator
- If you have more than one Android device connected, pass the target device id with `-Dudid=<device-id>`

### API tests

- No extra local application is required
- Internet access is needed to reach the public API used by the tests
- The API scenarios validate responses against JSON Schemas stored in `src/test/resources/schemas`
- The current schema coverage includes create, retrieve, and delete post responses

## Setup

1. Clone the repository.
2. Install Java 17 and verify it is available:

```bash
java -version
```

3. Install Maven and verify it is available:

```bash
mvn -version
```

4. Install Google Chrome.
5. For mobile tests, install Android Studio, set up the Android SDK, start an emulator or connect a device, and start Appium.
6. Review `src/test/resources/config.properties` if you need to update the web authentication values or user id used by the web tests.

## Running the Tests

The project uses JUnit 5 and Cucumber suite runners. Run each suite explicitly with Maven:

### Web

```bash
mvn clean test -Dtest=web.runners.WebTestRunner
```

### Mobile

```bash
mvn clean test -Dtest=mobile.runners.TestRunner
```

If needed, include the device id:

```bash
mvn clean test -Dtest=mobile.runners.TestRunner -Dudid=<device-id>
```

### API

```bash
mvn clean test -Dtest=api.runners.ApiTestRunner
```

If you prefer, you can also run the runner classes directly from your IDE.

## Reports

- Web Cucumber report: `target/web-report.html`
- API Cucumber report: `target/cucumber-reports/api-report.html`

## Notes

- The web tests inject authentication data from `src/test/resources/config.properties` before running.
- The mobile tests terminate the app after each scenario.
- The mobile tests assume the Sauce Labs demo app is already installed on the target device or emulator.
- The suite runner class names do not match Maven Surefire's default test naming pattern, so the `-Dtest=...` form is the safest way to execute them from the command line.
