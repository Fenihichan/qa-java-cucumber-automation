package web.stepdefinitions;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import web.pages.ExplorePage;
import web.utils.DriverFactory;

public class ExploreSteps {

    // Don't initialize here — driver is null at this point
    private ExplorePage explore;

    private ExplorePage getExplorePage() {
        if (explore == null) {
            explore = new ExplorePage(DriverFactory.getDriver());
        }
        return explore;
    }

    @Given("User already logged in")
    public void userLoggedIn() {
        // Driver and token are already set up by Hooks
        // Just navigate to explore page
        DriverFactory.getDriver().get("https://app.bibit.id/explore");
    }

    @When("User searches {string}")
    public void search(String product) {
        getExplorePage().searchProduct(product);
    }

    @When("User clicks searched product {string}")
    public void click(String product) {
        getExplorePage().clickProduct(product);
    }

    @Then("User should see detail page {string}")
    public void validate(String product) {
        assertTrue(getExplorePage().validateProduct(product));
    }
}