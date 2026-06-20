Feature: Sorting Products

  Background:
    Then user should see products title

  Scenario: Sort products by name descending
    When user sorts products by "Name - Descending"
    Then products should be sorted by name descending

  Scenario: Sort products by price ascending
    When user sorts products by "Price - Ascending"
    Then products should be sorted by price ascending
