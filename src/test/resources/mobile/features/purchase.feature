Feature: Purchase Product

  Background:
    Given user is logged in

  Scenario: Buy Sauce Labs Backpack with Blue color and quantity 2
    When user selects product "Sauce Labs Backpack"
    And user selects color "Blue"
    And user changes quantity to 2
    And user adds product to cart
    And user opens cart
    Then cart should contain item "Sauce Labs Backpack" with quantity 2 and color "Blue"
    When user proceeds to checkout
    And user fills shipping information
    And user continues to payment
    And user fills payment information
    And user reviews the order
    And user places the order
    Then order should be completed
