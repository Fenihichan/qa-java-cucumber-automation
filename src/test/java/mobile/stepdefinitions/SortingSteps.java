package mobile.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mobile.pages.ProductPage;

public class SortingSteps {

    private ProductPage productPage;

    private ProductPage productPage() {
        if (productPage == null) {
            productPage = new ProductPage();
        }
        return productPage;
    }

    @When("user sorts products by {string}")
    public void sortProduct(String option) {
        productPage().openSortMenu();
        productPage().selectSortOption(option);
    }

    @Then("user should see products title")
    public void userShouldSeeProductsTitle() {
        productPage().verifyProductsTitle();
    }

    @Then("products should be sorted by name descending")
    public void verifyNameSort() {
        productPage().verifyNameDescending();
    }

    @Then("products should be sorted by price ascending")
    public void verifyPriceSort() {
        productPage().verifyPriceAscending();
    }
}
