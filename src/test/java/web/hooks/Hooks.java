package web.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import web.utils.AuthHelper;
import web.utils.DriverFactory;

import java.io.IOException;

public class Hooks {

    @Before
    public void setUp() throws IOException {
        DriverFactory.initDriver();
        DriverFactory.getDriver().get("https://app.bibit.id");
        AuthHelper.injectToken();
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}