Feature: JSONPlaceholder API Testing

  Scenario: Create a new post
    Given User sends POST request to "/posts" with title "Learn API Testing" body "Practicing API testing with JSONPlaceholder" and userId 101
    Then Response status code should be 201
    And Response title should be "Learn API Testing"
    And Response body should be "Practicing API testing with JSONPlaceholder"
    And Response userId should be 101
    And Response matches post schema

  Scenario: Retrieve all posts
    Given User sends GET request to "/posts"
    Then Response status code should be 200
    And All posts should have non-null id
    And Response matches posts array schema

  Scenario: Delete a post
    Given User sends DELETE request to "/posts/1"
    Then Response status code should be 200
    And Response body is empty object
    And Response matches delete schema

  Scenario: Update a post
    Given User sends PUT request to "/posts/1" with title "Updated Post Title" body "This is the updated body content." and userId 99
    Then Response status code should be 200
    And Response title should be "Updated Post Title"
    And Response body should be "This is the updated body content."
    And Response userId should be 99
    And Response matches post schema