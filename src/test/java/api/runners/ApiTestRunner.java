package api.runners;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("api/features")
@ConfigurationParameter(key = "cucumber.glue", value = "api.stepdefinitions")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber-reports/api-report.html")
public class ApiTestRunner {
}