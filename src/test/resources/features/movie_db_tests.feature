@list
Feature: Movie API tests

#  Background: A List
#    Given list service is running

  Scenario Outline: Create list with given name
    Given list service is running
    When I create a list with '<Name>' name
    Then Should be with '<status>' result

    Examples:
      | Name    | status |
      | VName   | pass   |
      | $@#%#$% | pass   |
      | 123243  | pass   |

  Scenario Outline: Update the list name
    Given list service is running
    When I create a list with '<Name>' name
    Then Should be with '<status>' result
    And I update list name to '<updated_name>'
    And Should be with '<update_status>' result

    Examples:
      | Name  | status | updated_name | update_status |
      | VName | pass   | upVname      | pass          |
      | sfdfs | pass   | upsfdf       | pass          |