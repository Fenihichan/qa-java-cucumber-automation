Feature: Login

  Scenario: Login successfully
    Given user opens login page
    When user login with valid credential
    Then user should see logout menu
    And user closes menu bar
