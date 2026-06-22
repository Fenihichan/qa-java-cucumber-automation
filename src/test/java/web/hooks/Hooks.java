package web.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import web.utils.AuthHelper;
import web.utils.DriverFactory;

import java.io.IOException;
import java.util.Properties;

public class Hooks {

    @Before
    public void setUp() throws IOException {
        DriverFactory.initDriver();
        Properties props = AuthHelper.loadConfig();
        DriverFactory.getDriver().get(props.getProperty("base.url", "https://app.bibit.id"));
        AuthHelper.injectToken(props);
        DriverFactory.getDriver().navigate().refresh();
        AuthHelper.assertLoggedInLandingPage(props);
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
