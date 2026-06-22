package mobile.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Locale;

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
        verifyColorIndicator(itemColor, color);
    }

    public void checkout() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnCheckout))
                .click();
    }

    private void verifyColorIndicator(WebElement itemColor, String expectedColor) {
        String colorValue = readColorLabel(itemColor);
        if (containsColorName(colorValue, expectedColor)) {
            return;
        }

        Color sampledColor = readSwatchColor(itemColor);
        if (!matchesExpectedColor(sampledColor, expectedColor)) {
            String observed = colorValue == null ? sampledColor.toString() : colorValue + " / " + sampledColor;
            throw new AssertionError("Expected color indicator to match '" + expectedColor + "' but was " + observed);
        }
    }

    private String readColorLabel(WebElement itemColor) {
        String[] attributes = {"contentDescription", "content-desc", "text", "name"};
        for (String attribute : attributes) {
            String value = itemColor.getAttribute(attribute);
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private boolean containsColorName(String actualValue, String expectedColor) {
        if (actualValue == null) {
            return false;
        }

        String normalizedActual = actualValue.toLowerCase(Locale.ROOT);
        String normalizedExpected = expectedColor.toLowerCase(Locale.ROOT);

        return normalizedActual.contains(normalizedExpected) && !normalizedActual.contains("selected product");
    }

    private Color readSwatchColor(WebElement itemColor) {
        byte[] screenshot = itemColor.getScreenshotAs(OutputType.BYTES);
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(screenshot));
            if (image == null) {
                throw new AssertionError("Unable to read color indicator screenshot");
            }

            int centerX = image.getWidth() / 2;
            int centerY = image.getHeight() / 2;
            int radius = Math.max(1, Math.min(image.getWidth(), image.getHeight()) / 6);

            long red = 0;
            long green = 0;
            long blue = 0;
            long samples = 0;

            for (int x = Math.max(0, centerX - radius); x <= Math.min(image.getWidth() - 1, centerX + radius); x++) {
                for (int y = Math.max(0, centerY - radius); y <= Math.min(image.getHeight() - 1, centerY + radius); y++) {
                    int argb = image.getRGB(x, y);
                    int alpha = (argb >>> 24) & 0xFF;
                    if (alpha < 20) {
                        continue;
                    }

                    red += (argb >>> 16) & 0xFF;
                    green += (argb >>> 8) & 0xFF;
                    blue += argb & 0xFF;
                    samples++;
                }
            }

            if (samples == 0) {
                throw new AssertionError("Unable to sample the color indicator");
            }

            return new Color((int) (red / samples), (int) (green / samples), (int) (blue / samples));
        } catch (IOException ex) {
            throw new AssertionError("Unable to inspect the color indicator", ex);
        }
    }

    private boolean matchesExpectedColor(Color actualColor, String expectedColor) {
        String normalizedExpected = expectedColor.toLowerCase(Locale.ROOT).trim();

        return switch (normalizedExpected) {
            case "blue" -> actualColor.getBlue() > actualColor.getRed() + 20
                    && actualColor.getBlue() > actualColor.getGreen() + 20;
            case "red" -> actualColor.getRed() > actualColor.getGreen() + 20
                    && actualColor.getRed() > actualColor.getBlue() + 20;
            case "green" -> actualColor.getGreen() > actualColor.getRed() + 20
                    && actualColor.getGreen() > actualColor.getBlue() + 20;
            case "yellow" -> actualColor.getRed() > 150
                    && actualColor.getGreen() > 150
                    && actualColor.getBlue() < 180;
            case "orange" -> actualColor.getRed() > actualColor.getGreen()
                    && actualColor.getGreen() > actualColor.getBlue();
            case "black" -> actualColor.getRed() < 90
                    && actualColor.getGreen() < 90
                    && actualColor.getBlue() < 90;
            case "white" -> actualColor.getRed() > 180
                    && actualColor.getGreen() > 180
                    && actualColor.getBlue() > 180;
            case "purple" -> actualColor.getRed() > actualColor.getBlue() - 20
                    && actualColor.getBlue() > actualColor.getGreen();
            case "gray", "grey" -> Math.abs(actualColor.getRed() - actualColor.getGreen()) < 25
                    && Math.abs(actualColor.getGreen() - actualColor.getBlue()) < 25;
            default -> throw new AssertionError("Unsupported color verification for '" + expectedColor + "'");
        };
    }

}
