Feature: Search Product

  Scenario: Search BBCA
    Given User already logged in
    When User searches "BBCA"
    And User clicks searched product "BBCA"
    Then User should see detail page "BBCA"