package mobile.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mobile.pages.LoginPage;

import static mobile.support.TestData.PASSWORD;
import static mobile.support.TestData.USERNAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private LoginPage loginPage;

    private LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    @Given("user opens login page")
    public void userOpensLoginPage() {
        loginPage().openLoginPage();
    }

    @When("user login with valid credential")
    public void userLogin() {
        loginPage().login(USERNAME, PASSWORD);
    }

    @Then("user should see logout menu")
    public void verifyLoginSuccess() {
        assertTrue(loginPage().isLogoutMenuDisplayed());
    }

    @And("user closes menu bar")
    public void closeMenuBar() {
        loginPage().closeMenu();
    }

    @Given("user already login")
    public void userAlreadyLogin() {
        loginPage().openLoginPage();
        loginPage().login(USERNAME, PASSWORD);
        assertTrue(loginPage().isLogoutMenuDisplayed());
        loginPage().closeMenu();
    }
}
