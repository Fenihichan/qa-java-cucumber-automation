package mobile.pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;

public class LoginPage extends BasePage {

    private static final Duration WAIT = Duration.ofSeconds(30);

    private static final By MENU_BUTTON = AppiumBy.accessibilityId("View menu");
    private static final By LOGIN_MENU_ITEM = AppiumBy.accessibilityId("Login Menu Item");
    private static final By LOGIN_MENU_ITEM_ALT = AppiumBy.accessibilityId("Login");
    private static final By LOGIN_MENU_ITEM_TEXT = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/itemTV\").text(\"Log In\")"
    );
    private static final By LOGIN_MENU_ITEM_TEXT_ALT = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/itemTV\").textContains(\"Log\")"
    );
    private static final By LOGIN_MENU_ITEM_TEXT_ANY = AppiumBy.androidUIAutomator(
            "new UiSelector().textContains(\"Log\")"
    );
    private static final By LOGOUT_MENU_ITEM = AppiumBy.accessibilityId("Logout Menu Item");
    private static final By LOGOUT_MENU_ITEM_TEXT = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/itemTV\").text(\"Log Out\")"
    );
    private static final By CATALOG_MENU_ITEM = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Catalog\")"
    );
    private static final By USERNAME_FIELD = AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameET");
    private static final By PASSWORD_FIELD = AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordET");
    private static final By LOGIN_BUTTON = AppiumBy.id("com.saucelabs.mydemoapp.android:id/loginBtn");

    public void openLoginPage() {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(
                    src.toPath(),
                    new File("before-login.png").toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(driver.getPageSource());

        clickWhenReady(MENU_BUTTON);

        pause(500);

        clickFirstAvailable(
                LOGIN_MENU_ITEM,
                LOGIN_MENU_ITEM_ALT,
                LOGIN_MENU_ITEM_TEXT,
                LOGIN_MENU_ITEM_TEXT_ALT,
                LOGIN_MENU_ITEM_TEXT_ANY
        );
    }

    public void login(String username, String password) {
        typeWhenReady(USERNAME_FIELD, username);
        typeWhenReady(PASSWORD_FIELD, password);
        clickWhenReady(LOGIN_BUTTON);
    }

    public boolean isLogoutMenuDisplayed() {
        pause(500);
        clickWhenReady(MENU_BUTTON);
        pause(500);
        return isVisible(LOGOUT_MENU_ITEM) || isVisible(LOGOUT_MENU_ITEM_TEXT);
    }

    public void closeMenu() {
        tapOutsideMenuBar();
        pause(300);
    }

    public void openCatalogPage() {
        clickWhenReady(CATALOG_MENU_ITEM);
        pause(500);
    }

    private void clickWhenReady(By locator) {
        new WebDriverWait(driver, WAIT)
                .until(ExpectedConditions.elementToBeClickable(locator))
                .click();
    }

    private void typeWhenReady(By locator, String value) {
        WebElement element = new WebDriverWait(driver, WAIT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void clickFirstAvailable(By... locators) {
        for (By locator : locators) {
            if (isVisible(locator)) {
                clickWhenReady(locator);
                return;
            }
        }
        throw new NoSuchElementException("None of the login menu locators were found");
    }

    private boolean isVisible(By locator) {
        return !driver.findElements(locator).isEmpty()
                && driver.findElement(locator).isDisplayed();
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void tapOutsideMenuBar() {
        Dimension size = driver.manage().window().getSize();
        int x = Math.max((int) (size.getWidth() * 0.9), 1);
        int y = Math.max(size.getHeight() / 2, 1);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(tap));
    }
}