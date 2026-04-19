Feature: Submit order on ecommerce application

  Background:
    Given User navigates to the application

  @Regression
  Scenario: Positive Test - User submits order successfully
    When User logs in with email "surbhi19872105@gmail.com" and password "India@123456"
    And User searches for product "ZARA COAT 3" and adds to cart
    And User navigates to the shopping cart
    Then User verifies the product "ZARA COAT 3" is displayed in cart
    When User clicks on checkout button
    And User selects country "India" for checkout
    And User places the order
    Then User verifies the order confirmation message "THANKYOU FOR THE ORDER."

  @Regression
  Scenario Outline: Submit multiple orders with different products
    When User logs in with email "<email>" and password "<password>"
    And User searches for product "<product>" and adds to cart
    And User navigates to the shopping cart
    Then User verifies the product "<product>" is displayed in cart
    When User clicks on checkout button
    And User selects country "<country>" for checkout
    And User places the order
    Then User verifies the order confirmation message "THANKYOU FOR THE ORDER."

    Examples:
      | email            | password      | product      | country |
      | surbhi19872105@gmail.com | India@123456   | ZARA COAT 3  | India   |
      | anshika@gmail.com | Iamking@000   | ADIDAS ORIGINAL | India |
