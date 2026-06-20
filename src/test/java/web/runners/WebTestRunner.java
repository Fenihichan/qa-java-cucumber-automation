package web.runners;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("web/features")

@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "web.stepdefinitions,web.hooks"
)

@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty,html:target/web-report.html"
)

public class WebTestRunner {
}