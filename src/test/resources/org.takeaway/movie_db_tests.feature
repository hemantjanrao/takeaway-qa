@list
Feature: Movie API tests

  Background: A List
    Given list service is running

  Scenario Outline : Create list
    When I create a list with '<Name>' name
    Then List should get created '<status>' result

    Examples: List examples
      | Name    | status |
      | VName   | true   |
      | $@#%#$% | true   |
      | 123243  | true   |
      |         | false  |