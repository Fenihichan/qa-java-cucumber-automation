package mobile.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage extends BasePage {

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/titleTV")
    private WebElement txtItemName;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/itemsTV")
    private WebElement txtItemQuantity;

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/colorIV")
    private WebElement imgItemColor;

    public CartPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(driver, Duration.ofSeconds(10)),
                this
        );
    }

    @AndroidFindBy(id = "com.saucelabs.mydemoapp.android:id/cartBt")
    private WebElement btnCheckout;

    public boolean isCheckoutDisplayed() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(btnCheckout))
                .isDisplayed();
    }

    public void verifyProduct(String productName, int qty, String color) {
        WebElement itemName = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(txtItemName));
        if (!productName.equals(itemName.getText())) {
            throw new AssertionError("Expected item name " + productName + " but was " + itemName.getText());
        }

        WebElement itemQuantity = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(txtItemQuantity));
        if (!itemQuantity.getText().contains(String.valueOf(qty))) {
            throw new AssertionError("Expected quantity " + qty + " but was " + itemQuantity.getText());
        }

        WebElement itemColor = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(imgItemColor));
        String colorValue = itemColor.getAttribute("contentDescription");
        if (colorValue == null) {
            colorValue = itemColor.getAttribute("content-desc");
        }
        if (colorValue == null) {
            throw new AssertionError("Expected color indicator to be displayed");
        }
        if (!colorValue.equalsIgnoreCase("Displays color of selected product")) {
            throw new AssertionError("Expected color description 'Displays color of selected product' but was " + colorValue);
        }
    }

    public void checkout() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnCheckout))
                .click();
    }

}
