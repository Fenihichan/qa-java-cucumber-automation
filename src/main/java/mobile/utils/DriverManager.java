package mobile.utils;

import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private static AppiumDriver driver;

    private DriverManager() {
    }

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void setDriver(AppiumDriver driver) {
        DriverManager.driver = driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
