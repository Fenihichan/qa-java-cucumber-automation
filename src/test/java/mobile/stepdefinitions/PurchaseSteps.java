package mobile.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mobile.pages.CartPage;
import mobile.pages.CheckoutPage;
import mobile.pages.LoginPage;
import mobile.pages.ProductPage;
import mobile.support.PurchaseData;
import mobile.support.PurchaseDataLoader;

import static mobile.support.TestData.PASSWORD;
import static mobile.support.TestData.USERNAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseSteps {

    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private LoginPage loginPage;
    private final PurchaseData purchaseData = PurchaseDataLoader.loadDefault();

    private ProductPage productPage() {
        if (productPage == null) {
            productPage = new ProductPage();
        }
        return productPage;
    }

    private CartPage cartPage() {
        if (cartPage == null) {
            cartPage = new CartPage();
        }
        return cartPage;
    }

    private CheckoutPage checkoutPage() {
        if (checkoutPage == null) {
            checkoutPage = new CheckoutPage();
        }
        return checkoutPage;
    }

    private LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    @Given("user is logged in")
    public void userIsLoggedIn() {
        loginPage().openLoginPage();
        loginPage().login(USERNAME, PASSWORD);
        assertTrue(loginPage().isLogoutMenuDisplayed());
        loginPage().closeMenu();
    }

    @When("user selects product {string}")
    public void selectProduct(String product) {
        productPage().selectProduct(product);
    }

    @When("user selects color {string}")
    public void selectColor(String color) {
        productPage().selectColor(color);
    }

    @When("user changes quantity to {int}")
    public void changeQty(int qty) {
        productPage().changeQuantity(qty);
    }

    @When("user adds product to cart")
    public void addToCart() {
        productPage().addToCart();
    }

    @When("user opens cart")
    public void openCart() {
        productPage().openCart();
    }

    @Then("cart should contain item {string} with quantity {int} and color {string}")
    public void verifyCart(String productName, int qty, String color) {
        cartPage().verifyProduct(productName, qty, color);
    }

    @When("user proceeds to checkout")
    public void proceedToCheckout() {
        cartPage().checkout();
    }

    @When("user fills shipping information")
    public void fillShippingInformation() {
        checkoutPage().fillShippingInformation(
                purchaseData.fullName(),
                purchaseData.address1(),
                purchaseData.address2(),
                purchaseData.city(),
                purchaseData.state(),
                purchaseData.zip(),
                purchaseData.country()
        );
    }

    @When("user continues to payment")
    public void continueToPayment() {
        checkoutPage().continueFromShipping();
    }

    @When("user fills payment information")
    public void fillPaymentInformation() {
        checkoutPage().fillPaymentInformation(
                purchaseData.cardHolderInput(),
                purchaseData.cardNumberInput(),
                purchaseData.expirationDate(),
                purchaseData.securityCode()
        );
    }

    @When("user reviews the order")
    public void reviewsTheOrder() {
        checkoutPage().continueFromPayment();
        checkoutPage().verifyReview(
                purchaseData.productName(),
                purchaseData.quantity(),
                purchaseData.totalAmount()
        );
    }

    @When("user places the order")
    public void placesTheOrder() {
        checkoutPage().placeOrder();
    }

    @Then("order should be completed")
    public void orderShouldBeCompleted() {
        assertTrue(checkoutPage().isCheckoutCompleteDisplayed());
    }
}
