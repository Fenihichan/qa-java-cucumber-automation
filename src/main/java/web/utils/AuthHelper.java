package web.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class AuthHelper {

    public static Properties loadConfig() throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = AuthHelper.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IOException("config.properties was not found on the classpath");
            }
            props.load(inputStream);
        }
        return props;
    }

    public static void injectToken(Properties props) {
        JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();

        // Inject all required auth keys
        js.executeScript("window.localStorage.setItem('ac', arguments[0]);",        props.getProperty("ac"));
        js.executeScript("window.localStorage.setItem('actoken', arguments[0]);",   props.getProperty("actoken"));
        js.executeScript("window.localStorage.setItem('acexpire', arguments[0]);",  props.getProperty("acexpire"));
        js.executeScript("window.localStorage.setItem('bibit_user_id', arguments[0]);", props.getProperty("bibit_user_id"));
        js.executeScript("window.localStorage.setItem('hasLoggedIn', 'true');");

        System.out.println("=== DEBUG ===");
        System.out.println("ac       : " + js.executeScript("return window.localStorage.getItem('ac');"));
        System.out.println("actoken  : " + js.executeScript("return window.localStorage.getItem('actoken');"));
        System.out.println("acexpire : " + js.executeScript("return window.localStorage.getItem('acexpire');"));
        System.out.println("=============");

    }

    public static void assertLoggedInLandingPage(Properties props) {
        WebDriver driver = DriverFactory.getDriver();
        String expectedText = props.getProperty("home.logged_in_text", "Produk Investasi");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[normalize-space()='" + expectedText + "']")));
    }
}
