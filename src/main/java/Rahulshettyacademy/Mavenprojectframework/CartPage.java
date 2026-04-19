package Rahulshettyacademy.Mavenprojectframework;

import java.util.List;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Abstractcomponents.Abstractcomponents;

public class CartPage extends Abstractcomponents{
	WebDriver driver;
	WebDriverWait wait;

	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		PageFactory.initElements(driver, this);
	}
	
	// Page Elements
	@FindBy(xpath = "//button[contains(@routerlink, 'cart')]")
	WebElement cartButton;
	
	@FindBy(css = ".cartSection h3")
	List<WebElement> cartProducts;
	
	@FindBy(css = "button.btn.btn-primary")
	List<WebElement> allButtons;
	
	@FindBy(css = ".ng-animating")
	WebElement spinner;
	
	// Locators
	By cartProductsLocator = By.cssSelector(".cartSection h3");
	By animatingSpinner = By.cssSelector(".ng-animating");
	By cartButtonLocator = By.xpath("//button[contains(@routerlink, 'cart')]");
	
	/**
	 * Click on cart button to navigate to cart page
	 */
	public void clickCartButton() throws InterruptedException {
		System.out.println("Starting Cart button click");
		
		try {
			// Wait for cart button to be visible and clickable
			wait.until(ExpectedConditions.visibilityOfElementLocated(cartButtonLocator));
			System.out.println("Cart button is visible");
			
			wait.until(ExpectedConditions.elementToBeClickable(cartButton));
			System.out.println("Cart button is clickable");
			
			// Scroll to the cart button to ensure visibility
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cartButton);
			Thread.sleep(500);
			System.out.println("Scrolled to cart button");
			
			// Click the cart button using JavaScript for reliability
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);
			System.out.println("Successfully clicked Cart button using JavaScript!");
			
			// Wait for cart page to load with products
			Thread.sleep(2000);
			System.out.println("Waiting for cart products to load...");
			
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartProductsLocator));
			System.out.println("Cart page loaded successfully with products!");
			
		} catch(Exception e) {
			System.out.println("Error clicking cart button: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to click cart button: " + e.getMessage());
		}
	}
	
	/**
	 * Verify if product is displayed in cart
	 */
	public boolean VerifyProductDisplay(String productName) {
		try {
			// Refresh the cartProducts list to get latest elements
			List<WebElement> products = driver.findElements(cartProductsLocator);
			System.out.println("Total products in cart: " + products.size());
			
			for(WebElement product : products) {
				System.out.println("Product in cart: " + product.getText());
			}
			
			Boolean match = products.stream()
					.anyMatch(cartproduct -> cartproduct.getText().equalsIgnoreCase(productName));
			
			if(match) {
				System.out.println("Product '" + productName + "' found in cart!");
			} else {
				System.out.println("Product '" + productName + "' NOT found in cart!");
			}
			
			return match;
		} catch(Exception e) {
			System.out.println("Error verifying product display: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Click on checkout button
	 */
	public void clickCheckoutButton() throws InterruptedException {
		System.out.println("Starting Checkout button click");
		
		try {
			// Wait for spinner to disappear
			System.out.println("Waiting for spinner to disappear...");
			waitForElementToDisappear(spinner);
			System.out.println("Spinner disappeared");
			
			Thread.sleep(500);
			
			// Refresh the buttons list to get the latest elements
			List<WebElement> buttonsList = driver.findElements(By.cssSelector("button.btn.btn-primary"));
			System.out.println("Found " + buttonsList.size() + " primary buttons");
			
			// Find checkout button with text "Checkout"
			WebElement checkoutButton = null;
			for(WebElement btn : buttonsList) {
				String text = btn.getText().trim();
				System.out.println("Button text: '" + text + "'");
				if(text.equals("Checkout")) {
					checkoutButton = btn;
					System.out.println("Found Checkout button!");
					break;
				}
			}
			
			if(checkoutButton != null) {
				// Scroll into view
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkoutButton);
				Thread.sleep(500);
				System.out.println("Scrolled to Checkout button");
				
				// Wait for it to be clickable
				wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
				System.out.println("Checkout button is clickable");
				
				// Click using JavaScript for reliability
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutButton);
				System.out.println("Successfully clicked Checkout button using JavaScript!");
				
				// Wait for checkout page to load
				Thread.sleep(2000);
				System.out.println("Waiting for checkout page to fully load...");
				
				// Wait for country input field to appear on checkout page
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[placeholder='Select Country']")));
					System.out.println("✓ Checkout page loaded successfully! Country input field is visible");
				} catch(Exception e) {
					System.out.println("Warning: Country input not visible yet, continuing...");
				}
				
			} else {
				System.out.println("ERROR: Checkout button not found!");
				System.out.println("Available buttons count: " + buttonsList.size());
				throw new RuntimeException("Checkout button not found in cart page");
			}
		} catch(Exception e) {
			System.out.println("Error clicking checkout button: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to click checkout button: " + e.getMessage());
		}
	}
	
}