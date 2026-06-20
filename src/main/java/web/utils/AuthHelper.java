package web.utils;

import org.openqa.selenium.JavascriptExecutor;

import java.io.IOException;
import java.util.Properties;

public class AuthHelper {

    public static void injectToken() throws IOException {
        Properties props = new Properties();
        props.load(AuthHelper.class.getClassLoader()
                .getResourceAsStream("config.properties"));

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

        DriverFactory.getDriver().navigate().refresh();
    }
}