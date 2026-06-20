package mobile.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.AppiumBy;
import mobile.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage extends BasePage {

    private static final By REVIEW_PRODUCT_NAME = AppiumBy.id("com.saucelabs.mydemoapp.android:id/titleTV");
    private static final By REVIEW_ITEM_NUMBER = AppiumBy.id("com.saucelabs.mydemoapp.android:id/itemNumberTV");
    private static final By REVIEW_TOTAL_AMOUNT = AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalAmountTV");

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/fullNameET")
    private WebElement txtFullName;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/address1ET")
    private WebElement txtAddress1;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/address2ET")
    private WebElement txtAddress2;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cityET")
    private WebElement txtCity;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/stateET")
    private WebElement txtState;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/zipET")
    private WebElement txtZip;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/countryET")
    private WebElement txtCountry;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/nameET")
    private WebElement txtCardHolderName;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cardNumberET")
    private WebElement txtCardNumber;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/expirationDateET")
    private WebElement txtExpirationDate;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/securityCodeET")
    private WebElement txtSecurityCode;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/titleTV")
    private WebElement txtReviewProductName;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/fullNameTV")
    private WebElement txtReviewFullName;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/addressTV")
    private WebElement txtReviewAddress;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cardHolderTV")
    private WebElement txtReviewCardHolderName;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cardNumberTV")
    private WebElement txtReviewCardNumber;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/expirationDateTV")
    private WebElement txtReviewExpirationDate;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/itemNumberTV")
    private WebElement txtReviewItemNumber;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/totalAmountTV")
    private WebElement txtReviewTotalAmount;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/completeTV")
    private WebElement txtCheckoutComplete;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/paymentBtn")
    private WebElement btnPrimaryAction;

    public CheckoutPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver(), Duration.ofSeconds(10)),
                this
        );
    }

    public void fillShippingInformation(
            String fullName,
            String address1,
            String address2,
            String city,
            String state,
            String zip,
            String country
    ) {
        txtFullName.sendKeys(fullName);
        txtAddress1.sendKeys(address1);
        txtAddress2.sendKeys(address2);
        txtCity.sendKeys(city);
        txtState.sendKeys(state);
        txtZip.sendKeys(zip);
        txtCountry.sendKeys(country);
    }

    public void continueFromShipping() {
        btnPrimaryAction.click();
    }

    public void fillPaymentInformation(
            String cardHolderName,
            String cardNumber,
            String expirationDate,
            String securityCode
    ) {
        txtCardHolderName.sendKeys(cardHolderName);
        txtCardNumber.sendKeys(cardNumber);
        txtExpirationDate.sendKeys(expirationDate);
        txtSecurityCode.sendKeys(securityCode);
    }

    public void continueFromPayment() {
        btnPrimaryAction.click();
    }

    public void verifyReview(
            String productName,
            int qty,
            String totalAmount
    ) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(REVIEW_PRODUCT_NAME));

        String actualProductName = visibleText(wait, REVIEW_PRODUCT_NAME, "com.saucelabs.mydemoapp.android:id/titleTV");
        String actualItemNumber = visibleText(wait, REVIEW_ITEM_NUMBER, "com.saucelabs.mydemoapp.android:id/itemNumberTV");
        String actualTotalAmount = visibleText(wait, REVIEW_TOTAL_AMOUNT, "com.saucelabs.mydemoapp.android:id/totalAmountTV");

        if (!productName.equals(actualProductName)) {
            throw new AssertionError("Expected product name " + productName + " but was " + actualProductName);
        }
        if (!actualItemNumber.contains(String.valueOf(qty))) {
            throw new AssertionError("Expected quantity " + qty + " but was " + actualItemNumber);
        }
        if (!totalAmount.equals(actualTotalAmount)) {
            throw new AssertionError("Expected total amount " + totalAmount + " but was " + actualTotalAmount);
        }
    }

    public void placeOrder() {
        WebElement placeOrderButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnPrimaryAction));

        String buttonText = placeOrderButton.getText();
        if (buttonText == null || !buttonText.equalsIgnoreCase("Place Order")) {
            throw new AssertionError("Expected final checkout button text to be 'Place Order' but was '" + buttonText + "'");
        }

        placeOrderButton.click();
    }

    public boolean isCheckoutCompleteDisplayed() {
        return txtCheckoutComplete.isDisplayed();
    }

    private String visibleText(WebDriverWait wait, By locator, String resourceId) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        } catch (TimeoutException ex) {
            By scrolledLocator = AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(scrolledLocator));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        }
    }
}
