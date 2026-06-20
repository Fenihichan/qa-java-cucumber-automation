package mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import mobile.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.Duration;

public class ProductPage extends BasePage {

    private static final String PRODUCT_TITLE_ID = "com.saucelabs.mydemoapp.android:id/titleTV";
    private static final String PRODUCT_PRICE_ID = "com.saucelabs.mydemoapp.android:id/priceTV";
    private static final String PRODUCT_SCREEN_TITLE_ID = "com.saucelabs.mydemoapp.android:id/productTV";

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/plusIV")
    private WebElement btnIncreaseQuantity;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cartBt")
    private WebElement btnAddToCart;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cartIV")
    private WebElement btnCart;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/sortIV")
    private WebElement btnSort;

    public ProductPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver(), Duration.ofSeconds(10)),
                this
        );
    }

    public boolean isProductPageDisplayed() {
        return btnAddToCart.isDisplayed();
    }

    public void verifyProductsTitle() {
        WebElement title = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(PRODUCT_SCREEN_TITLE_ID)));
        if (!"Products".equals(title.getText())) {
            throw new AssertionError("Expected catalog title to be 'Products' but was '" + title.getText() + "'");
        }
    }

    public void selectProduct(String productName) {
        if ("Sauce Lab Back Packs".equalsIgnoreCase(productName)) {
            productName = "Sauce Labs Backpack";
        }
        By productTextLocator = AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + productName + "\"))"
        );
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(productTextLocator));

        By productImageLocator = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(productImageLocator))
                .click();
    }

    public void selectColor(String color) {
        String description = color.toLowerCase().endsWith("color") ? color : color + " color";
        WebElement colorElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/colorIV\")" +
                        ".description(\"" + description + "\")"
        )));
        colorElement.click();
    }

    public void changeQuantity(int qty) {
        for (int i = 1; i < qty; i++) {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(btnIncreaseQuantity))
                    .click();
        }
    }

    public void addToCart() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnAddToCart))
                .click();
    }

    public void openCart() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnCart))
                .click();
    }

    public void openSortMenu() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnSort))
                .click();
    }

    public void selectSortOption(String option) {
        WebElement sortOption = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.androidUIAutomator("new UiSelector().text(\"" + option + "\")")
                ));
        sortOption.click();
    }

    public void sortProduct(String option) {
        openSortMenu();
        selectSortOption(option);
    }

    public void verifyNameDescending() {
        List<String> actualNames = getVisibleTexts(PRODUCT_TITLE_ID);
        if (actualNames.size() < 2) {
            throw new AssertionError("Expected at least two product names to verify descending order");
        }

        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        if (!expectedNames.equals(actualNames)) {
            throw new AssertionError("Product names are not in descending order. Actual: " + actualNames);
        }
    }

    public void verifyPriceAscending() {
        List<String> actualPrices = getVisibleTexts(PRODUCT_PRICE_ID);
        if (actualPrices.size() < 2) {
            throw new AssertionError("Expected at least two product prices to verify ascending order");
        }

        List<Double> actualValues = new ArrayList<>();
        for (String price : actualPrices) {
            actualValues.add(parsePrice(price));
        }

        List<Double> expectedValues = new ArrayList<>(actualValues);
        expectedValues.sort(Double::compareTo);

        if (!expectedValues.equals(actualValues)) {
            throw new AssertionError("Product prices are not in ascending order. Actual: " + actualPrices);
        }
    }

    private List<String> getVisibleTexts(String elementId) {
        List<WebElement> elements = driver.findElements(AppiumBy.id(elementId));
        List<String> values = new ArrayList<>();
        for (WebElement element : elements) {
            String text = element.getText();
            if (text != null && !text.isBlank()) {
                values.add(text.trim());
            }
        }
        return values;
    }

    private double parsePrice(String price) {
        String normalized = price.replace("$", "").trim();
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException ex) {
            throw new AssertionError("Unable to parse price: " + price, ex);
        }
    }
}
