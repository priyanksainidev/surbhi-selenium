package Rahulshettyacademy.Mavenprojectframework;

import java.util.List;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Abstractcomponents.Abstractcomponents;
import java.time.Duration;

public class CheckoutPage extends Abstractcomponents {
	WebDriver driver;
	WebDriverWait wait;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		PageFactory.initElements(driver, this);
	}

	// Page Elements
	@FindBy(css = "[placeholder='Select Country']")
	WebElement countryInput;

	@FindBy(css = "button.ta-item")
	List<WebElement> countryButtons;

	@FindBy(css = "[class*='select']")
	List<WebElement> dropdownSelects;

	@FindBy(css = "a.action__submit")
	WebElement placeOrderButton;

	@FindBy(css = ".hero-primary")
	WebElement confirmationMessage;

	@FindBy(css = ".ta-results")
	WebElement dropdownResults;

	// Locators
	By countryDropdown = By.cssSelector("[placeholder='Select Country']");
	By taResults = By.cssSelector(".ta-results");
	By taItemButtons = By.cssSelector("button.ta-item");
	By animatingSpinner = By.cssSelector(".ng-animating");
	By companyDropdown = By.xpath("//select[@class='input ddl']");

	/**
	 * Select company from dropdown
	 */
	public void selectCompany(String companyName) throws InterruptedException {
		System.out.println("\n========== COMPANY SELECTION START ==========");
		System.out.println("Starting company selection for: " + companyName);
		
		try {
			Thread.sleep(1000); // Wait for page to stabilize after country selection
			
			// Find all select elements on the page
			List<WebElement> allSelects = driver.findElements(By.tagName("select"));
			System.out.println("Total select elements found on page: " + allSelects.size());
			
			// Log all select elements to help identify the company dropdown
			for (int i = 0; i < allSelects.size(); i++) {
				String classes = allSelects.get(i).getAttribute("class");
				String id = allSelects.get(i).getAttribute("id");
				String name = allSelects.get(i).getAttribute("name");
				System.out.println("  Select[" + i + "] - Class: '" + classes + "', ID: '" + id + "', Name: '" + name + "'");
				
				// List options in this select
				Select select = new Select(allSelects.get(i));
				List<WebElement> options = select.getOptions();
				for (int j = 0; j < options.size(); j++) {
					System.out.println("      Option[" + j + "]: " + options.get(j).getText());
				}
			}
			
			// Try to find the dropdown with the company name (likely the second select or one with "ddl" in class)
			WebElement companySelect = null;
			
			// Strategy 1: Look for select with "ddl" in class name
			for (WebElement select : allSelects) {
				String classes = select.getAttribute("class");
				if (classes != null && classes.contains("ddl")) {
					companySelect = select;
					System.out.println("✓ Found company dropdown using 'ddl' class matcher");
					break;
				}
			}
			
			// Strategy 2: If not found, try the second select element (skip first country one)
			if (companySelect == null && allSelects.size() > 1) {
				companySelect = allSelects.get(1);
				System.out.println("✓ Using second select element as company dropdown");
			}
			
			// Strategy 3: If still not found, use first select that's visible
			if (companySelect == null) {
				for (WebElement select : allSelects) {
					String display = select.getCssValue("display");
					if (!display.equals("none")) {
						companySelect = select;
						System.out.println("✓ Found visible select element");
						break;
					}
				}
			}
			
			if (companySelect == null) {
				System.out.println("✗ ERROR: No suitable dropdown found!");
				throw new RuntimeException("Company dropdown element not found on page");
			}
			
			// Use Selenium's Select class to interact with the dropdown
			Select dropdown = new Select(companySelect);
			
			// Scroll into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", companySelect);
			Thread.sleep(500);
			System.out.println("✓ Scrolled to company dropdown");
			
			// Get all options and find the matching one
			List<WebElement> options = dropdown.getOptions();
			System.out.println("Total options in selected dropdown: " + options.size());
			
			WebElement targetOption = null;
			for (WebElement option : options) {
				String optionText = option.getText().trim();
				System.out.println("  Checking option: '" + optionText + "'");
				
				if (optionText.equalsIgnoreCase(companyName)) {
					targetOption = option;
					System.out.println("✓ Found exact match for: " + companyName);
					break;
				}
			}
			
			// If exact match not found, try contains
			if (targetOption == null) {
				for (WebElement option : options) {
					String optionText = option.getText().trim();
					if (optionText.contains(companyName)) {
						targetOption = option;
						System.out.println("✓ Found matching option containing: " + companyName);
						break;
					}
				}
			}
			
			if (targetOption != null) {
				// Use selectByVisibleText or selectByValue
				try {
					dropdown.selectByVisibleText(targetOption.getText());
					System.out.println("✓ Successfully selected using visibleText: " + targetOption.getText());
				} catch (Exception e) {
					// Fallback to value
					String value = targetOption.getAttribute("value");
					dropdown.selectByValue(value);
					System.out.println("✓ Successfully selected using value: " + value);
				}
				
				Thread.sleep(500);
				System.out.println("========== COMPANY SELECTION COMPLETE ==========\n");
			} else {
				System.out.println("✗ ERROR: " + companyName + " option not found in dropdown!");
				System.out.println("✗ Available options:");
				for (WebElement opt : options) {
					System.out.println("  - " + opt.getText().trim());
				}
				throw new RuntimeException(companyName + " option not found in company dropdown");
			}
		} catch(Exception e) {
			System.out.println("✗ Error selecting company: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to select company: " + e.getMessage());
		}
	}

	/**
	 * Select country from typeahead dropdown
	 */
	public void selectCountry(String countryName) throws InterruptedException {
	    System.out.println("\n========== COUNTRY SELECTION START ==========");
	    System.out.println("Starting country selection for: " + countryName);
	    
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    try {
	        // Step 1: Find and click the country input
	        System.out.println("Step 1: Finding country input field...");
	        WebElement countryInput = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                By.cssSelector("[placeholder='Select Country']")
	            )
	        );
	        System.out.println("✓ Country input field found and clickable");
	        
	        // Scroll into view
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", countryInput);
	        Thread.sleep(500);
	        System.out.println("✓ Scrolled country input into view");
	        
	        // Clear existing value
	        countryInput.clear();
	        Thread.sleep(300);
	        System.out.println("✓ Cleared input field");

	        // Step 2: Type the country name character by character
	        System.out.println("Step 2: Typing country name...");
	        for (char c : countryName.toCharArray()) {
	            countryInput.sendKeys(String.valueOf(c));
	            Thread.sleep(50);
	        }
	        System.out.println("✓ Typed: " + countryName);
	        
	        // Trigger input event for Angular
	        ((JavascriptExecutor) driver).executeScript(
	            "var evt = new Event('input', { bubbles: true }); " +
	            "arguments[0].dispatchEvent(evt);",
	            countryInput
	        );
	        System.out.println("✓ Triggered input event for Angular");

	        // Step 3: Wait for buttons to appear (the ta-results div might not appear)
	        System.out.println("Step 3: Waiting for dropdown buttons to appear...");
	        Thread.sleep(1000);  // Give Angular time to render
	        
	        // Try to find buttons with retry logic
	        List<WebElement> countryButtons = driver.findElements(
	            By.xpath("//button[contains(@class, 'ta-item')]")
	        );
	        
	        int maxRetries = 5;
	        int retryCount = 0;
	        while (countryButtons.isEmpty() && retryCount < maxRetries) {
	            retryCount++;
	            System.out.println("  Attempt " + retryCount + "/" + maxRetries + ": Waiting for buttons...");
	            Thread.sleep(500);
	            countryButtons = driver.findElements(
	                By.xpath("//button[contains(@class, 'ta-item')]")
	            );
	        }
	        
	        System.out.println("✓ Found " + countryButtons.size() + " button options");
	        
	        if (countryButtons.isEmpty()) {
	            System.out.println("✗ ERROR: No dropdown buttons appeared!");
	            System.out.println("\nDebugging information:");
	            
	            // Check for ta-results container
	            List<WebElement> taResults = driver.findElements(By.cssSelector(".ta-results"));
	            System.out.println("  .ta-results container found: " + !taResults.isEmpty());
	            
	            // Check for any ta- class elements
	            List<WebElement> allElements = driver.findElements(By.xpath("//*[contains(@class, 'ta')]"));
	            System.out.println("  Elements with 'ta' class: " + allElements.size());
	            for (WebElement el : allElements) {
	                System.out.println("    - " + el.getTagName() + " (class: '" + el.getAttribute("class") + "')");
	            }
	            
	            // Check if input still has the value
	            String inputValue = countryInput.getAttribute("value");
	            System.out.println("  Country input current value: '" + inputValue + "'");
	            
	            throw new RuntimeException("Country dropdown buttons not appearing after typing: " + countryName);
	        }

	        // Step 4: Print all available options
	        System.out.println("Available country options:");
	        for (WebElement btn : countryButtons) {
	            String btnText = btn.getText().trim();
	            System.out.println("  - '" + btnText + "'");
	        }

	        // Step 5: Find the matching country option
	        System.out.println("Step 4: Finding matching country option...");
	        WebElement countryOption = null;
	        
	        // Try exact match first
	        for (WebElement btn : countryButtons) {
	            String btnText = btn.getText().trim();
	            if (btnText.equalsIgnoreCase(countryName)) {
	                countryOption = btn;
	                System.out.println("✓ Found exact match: '" + btnText + "'");
	                break;
	            }
	        }
	        
	        // If exact match not found, try contains
	        if (countryOption == null) {
	            for (WebElement btn : countryButtons) {
	                String btnText = btn.getText().trim();
	                if (btnText.toLowerCase().contains(countryName.toLowerCase())) {
	                    countryOption = btn;
	                    System.out.println("✓ Found partial match: '" + btnText + "'");
	                    break;
	                }
	            }
	        }

	        if (countryOption == null) {
	            System.out.println("✗ ERROR: Country '" + countryName + "' not found in dropdown options!");
	            throw new RuntimeException("Country '" + countryName + "' not found in any dropdown option");
	        }

	        // Step 6: Click the country option
	        System.out.println("Step 5: Clicking country option...");
	        
	        // Scroll to the option if needed
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", countryOption);
	        Thread.sleep(300);
	        
	        // Click using JavaScript for reliability
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", countryOption);
	        System.out.println("✓ Successfully clicked country option");

	        // Step 7: Wait for dropdown to close and input to be populated
	        System.out.println("Step 6: Waiting for dropdown to close...");
	        Thread.sleep(800);
	        
	        try {
	            // Wait for ta-results to disappear (if it exists)
	            wait.until(ExpectedConditions.invisibilityOfElementLocated(
	                By.cssSelector(".ta-results")
	            ));
	            System.out.println("✓ Dropdown closed");
	        } catch (Exception e) {
	            System.out.println("⚠ Dropdown didn't close (container may not exist), but continuing...");
	        }
	        
	        System.out.println("✓ Country selection completed successfully!");
	        System.out.println("========== COUNTRY SELECTION COMPLETE ==========\n");
	        
	    } catch (Exception e) {
	        System.out.println("✗ ERROR in country selection: " + e.getMessage());
	        e.printStackTrace();
	        System.out.println("========== COUNTRY SELECTION FAILED ==========\n");
	        throw new RuntimeException("Failed to select country: " + e.getMessage());
	    }
	}

	/**
	 * Click on Place Order button
	 */
	public void clickPlaceOrderButton() throws InterruptedException {
		System.out.println("\n========== PLACE ORDER START ==========");
		System.out.println("Starting Place Order button click");
		
		try {
			// Wait for Place Order button to be visible and clickable
			System.out.println("Waiting for Place Order button to be visible...");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.action__submit")));
			System.out.println("✓ Place Order button is visible");
			
			System.out.println("Waiting for Place Order button to be clickable...");
			wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
			System.out.println("✓ Place Order button is clickable");
			
			// Scroll the button into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", placeOrderButton);
			Thread.sleep(500);
			System.out.println("✓ Scrolled to Place Order button");
			
			// Click the button using JavaScript for reliability
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderButton);
			System.out.println("✓ Successfully clicked Place Order button using JavaScript!");
			
			// Wait for confirmation page to load
			Thread.sleep(2000);
			System.out.println("✓ Waiting for order confirmation page to load...");
			System.out.println("========== PLACE ORDER COMPLETE ==========\n");
			
		} catch(Exception e) {
			System.out.println("✗ Error clicking Place Order button: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to click Place Order button: " + e.getMessage());
		}
	}

	/**
	 * Get confirmation message
	 */
	public String getConfirmationMessage() {
		try {
			System.out.println("\n========== ORDER CONFIRMATION START ==========");
			System.out.println("Waiting for confirmation message element...");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".hero-primary")));
			System.out.println("✓ Confirmation message element is visible");
			
			String message = confirmationMessage.getText();
			System.out.println("✓ Confirmation message retrieved: " + message);
			System.out.println("========== ORDER CONFIRMATION COMPLETE ==========\n");
			return message;
		} catch(Exception e) {
			System.out.println("✗ Error getting confirmation message: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Confirmation message not found on order confirmation page");
		}
	}

	/**
	 * Verify order confirmation
	 */
	public boolean verifyOrderConfirmation(String expectedMessage) {
		try {
			System.out.println("\n========== VERIFY ORDER CONFIRMATION START ==========");
			System.out.println("Expected confirmation message: " + expectedMessage);
			
			String actualMessage = getConfirmationMessage();
			boolean isMatched = actualMessage.equalsIgnoreCase(expectedMessage);
			
			System.out.println("Actual confirmation message: " + actualMessage);
			System.out.println("Match result: " + (isMatched ? "✓ PASSED" : "✗ FAILED"));
			
			if(!isMatched) {
				throw new RuntimeException("Confirmation message mismatch! Expected: '" + expectedMessage + "' but got: '" + actualMessage + "'");
			}
			
			Assert.assertTrue(isMatched);
			System.out.println("========== VERIFY ORDER CONFIRMATION COMPLETE ==========\n");
			return isMatched;
		} catch(Exception e) {
			System.out.println("✗ Error verifying order confirmation: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Order confirmation verification failed: " + e.getMessage());
		}
	}
}
