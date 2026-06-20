package web.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ExplorePage {

    WebDriver driver;

    public ExplorePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Step 1 — search button on explore main page
    @FindBy(xpath = "//p[contains(text(),'Cari produk investasi')]")
    WebElement searchButton;

    // Step 2 — actual input on /explore/search page
    @FindBy(css = "input.custom-input-search")
    WebElement searchInput;

    public void searchProduct(String keyword) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Click the search button to go to search page
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();

        // Wait for search input to appear on /explore/search
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(keyword);
    }

    public void clickProduct(String keyword) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement product = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[contains(text(),'" + keyword + "')]")
                )
        );

        product.click();
    }

    public boolean validateProduct(String keyword) {
        return driver.getPageSource().contains(keyword);
    }
}