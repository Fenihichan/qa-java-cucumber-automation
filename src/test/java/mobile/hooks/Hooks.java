package mobile.hooks;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.net.URL;
import java.nio.file.Paths;

import mobile.utils.DriverManager;

public class Hooks {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    @Before
    public void setup() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("Android Emulator");
        options.setNoReset(false);

        String udid = System.getProperty("udid");
        if (udid != null && !udid.isBlank()) {
            options.setUdid(udid);
        }

        // If APK exists, install it automatically.
        String apkPath = Paths.get(
                "apps", "mda-2.2.0-25.apk"
        ).toAbsolutePath().toString();

        if (java.nio.file.Files.exists(Paths.get(apkPath))) {
            options.setApp(apkPath);
        } else {
            // Fallback for local emulator where app is already installed.
            options.setAppPackage(APP_PACKAGE);
            options.setAppWaitActivity("*");
        }

        DriverManager.setDriver(new AndroidDriver(
                new URL("http://127.0.0.1:4723"),
                options
        ));
    }

    @After
    public void tearDown() {
        if (DriverManager.getDriver() instanceof AndroidDriver androidDriver) {
            androidDriver.terminateApp(APP_PACKAGE);
        }
        DriverManager.quitDriver();
    }
}