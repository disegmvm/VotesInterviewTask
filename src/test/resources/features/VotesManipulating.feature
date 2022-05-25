Feature: Manipulating with votes

  Scenario: Get all votes
    Given a request to get votes is prepared
    When a GET request is made
    Then status code is 200
    And length of the response is more than 0
    And votes are stored in proper object

  Scenario: Get specific vote
    Given a request to get votes is prepared
    When a GET request to retrieve specific vote is made
    Then status code is 200
    And length of the response is more than 0
    And retrieved vote is corresponding to the stored one

  Scenario: Create a new vote
    Given a request to post vote is prepared
    When a POST request to create a vote is made
    Then status code is 200
    And response contains "message": "SUCCESS"
    And "id" in response is not empty

  Scenario: Get created vote
    Given a request to get votes is prepared
    When a GET request to retrieve created vote is made
    Then status code is 200
    And "id" in response is equal to required id

  Scenario: Delete created vote
    Given a request to delete vote is prepared
    When a DELETE request is made
    Then response contains "message": "SUCCESS"

  Scenario: Get deleted vote
    Given a request to get votes is prepared
    When a GET request to retrieve deleted vote is made
    Then response contains "message": "NOT_FOUND"
    And status code is 404