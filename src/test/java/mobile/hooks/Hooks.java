package mobile.hooks;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import mobile.utils.DriverManager;

import java.net.URL;
import java.nio.file.Paths;

public class Hooks {

    private static final String APP_PACKAGE = "com.saucelabs.mydemoapp.android";

    @Before
    public void setup() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("Android Emulator");
        options.setNoReset(false);

        options.setCapability("androidInstallTimeout", 300000);
        options.setCapability("uiautomator2ServerInstallTimeout", 300000);
        options.setCapability("uiautomator2ServerLaunchTimeout", 180000);
        options.setCapability("adbExecTimeout", 300000);
        options.setCapability("appWaitDuration", 120000);

        options.setCapability("ignoreHiddenApiPolicyError", true);
        options.setCapability("skipDeviceInitialization", true);

        String udid = System.getProperty("udid");
        if (udid != null && !udid.isBlank()) {
            options.setUdid(udid);
        }

        String apkPath = Paths.get(
                "apps", "mda-2.2.0-25.apk"
        ).toAbsolutePath().toString();

        if (java.nio.file.Files.exists(Paths.get(apkPath))) {
            options.setApp(apkPath);
        } else {
            options.setAppPackage(APP_PACKAGE);
        }

        // Apply for both local and GitHub Actions
        options.setAppWaitActivity("*");

        DriverManager.setDriver(new AndroidDriver(
                new URL("http://127.0.0.1:4723"),
                options
        ));

        AndroidDriver driver = (AndroidDriver) DriverManager.getDriver();

        System.out.println("====================================");
        System.out.println("Current package : " + driver.getCurrentPackage());
        System.out.println("Current activity: " + driver.currentActivity());
        System.out.println("====================================");

        try {
            System.out.println(driver.getPageSource());
        } catch (Exception e) {
            System.out.println("Unable to get page source: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (DriverManager.getDriver() instanceof AndroidDriver androidDriver) {
            androidDriver.terminateApp(APP_PACKAGE);
        }
        DriverManager.quitDriver();
    }
}