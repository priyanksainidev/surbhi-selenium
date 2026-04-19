package Stepdefinitions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Rahulshettyacademy.Mavenprojectframework.CartPage;
import Rahulshettyacademy.Mavenprojectframework.CheckoutPage;
import Rahulshettyacademy.Mavenprojectframework.DataReader;
import Rahulshettyacademy.Mavenprojectframework.Landingpage;
import Rahulshettyacademy.Mavenprojectframework.ProductCatalogue;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class stepdefinitionimpl {
	
	public WebDriver driver;
	public Landingpage landingpage;
	public ProductCatalogue productcatalogue;
	public CartPage cartpage;
	public CheckoutPage checkoutpage;
	
	@Given("User navigates to the application")
	public void user_navigates_to_application() throws Throwable {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		System.out.println("\n========== LAUNCHING BROWSER ==========");
		System.out.println("Browser launched successfully!");
		
		landingpage = new Landingpage(driver);
		productcatalogue = new ProductCatalogue(driver);
		cartpage = new CartPage(driver);
		checkoutpage = new CheckoutPage(driver);
	}
	
	@When("User logs in with email {string} and password {string}")
	public void user_logs_in_with_credentials(String email, String password) throws Throwable {
		System.out.println("\n========== STEP 1: LOGIN ==========");
		landingpage.goTo();
		System.out.println("Navigated to login page");
		landingpage.loginApplicatiion(email, password);
		System.out.println("Login successful with email: " + email);
		Thread.sleep(2000);
	}
	
	@And("User searches for product {string} and adds to cart")
	public void user_searches_and_adds_product_to_cart(String productName) throws Throwable {
		System.out.println("\n========== STEP 2: ADD PRODUCT TO CART ==========");
		List<WebElement> products = productcatalogue.getProductsList();
		System.out.println("Products list retrieved: " + products.size() + " products found");
		productcatalogue.addProductToCart(productName);
		System.out.println("Product '" + productName + "' added to cart successfully!");
		Thread.sleep(2000);
	}
	
	@And("User navigates to the shopping cart")
	public void user_navigates_to_shopping_cart() throws Throwable {
		System.out.println("\n========== STEP 3: NAVIGATE TO CART ==========");
		cartpage.clickCartButton();
		System.out.println("Cart page opened successfully!");
		Thread.sleep(1000);
	}
	
	@Then("User verifies the product {string} is displayed in cart")
	public void user_verifies_product_in_cart(String productName) throws Throwable {
		System.out.println("\n========== STEP 4: VERIFY PRODUCT IN CART ==========");
		Boolean match = cartpage.VerifyProductDisplay(productName);
		if(match) {
			System.out.println("✓ Product '" + productName + "' found in cart!");
			Assert.assertTrue(match);
		} else {
			System.out.println("✗ Product '" + productName + "' NOT found in cart!");
			Assert.assertTrue(match, "Product not found in cart!");
		}
	}
	
	@When("User clicks on checkout button")
	public void user_clicks_checkout_button() throws Throwable {
		System.out.println("\n========== STEP 5: PROCEED TO CHECKOUT ==========");
		cartpage.clickCheckoutButton();
		System.out.println("Checkout button clicked successfully!");
		Thread.sleep(1000);
	}
	
	@And("User selects country {string} for checkout")
	public void user_selects_country_for_checkout(String countryName) throws Throwable {
		System.out.println("\n========== STEP 6: SELECT COUNTRY ==========");
		checkoutpage.selectCountry(countryName);
		System.out.println("✓ Country '" + countryName + "' selected successfully!");
		
		// Wait for country dropdown to close
		System.out.println("\n========== WAITING FOR COUNTRY SELECTION TO COMPLETE ==========");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ta-results")));
			System.out.println("✓ Country dropdown results have disappeared - selection confirmed!");
		} catch (Exception e) {
			System.out.println("⚠ Dropdown didn't close, but country was selected. Continuing...");
		}
		Thread.sleep(1000);
	}
	
	@And("User places the order")
	public void user_places_the_order() throws Throwable {
		System.out.println("\n========== STEP 7: CLICK PLACE ORDER BUTTON ==========");
		checkoutpage.clickPlaceOrderButton();
		System.out.println("✓ Place Order button clicked!");
		Thread.sleep(1000);
	}
	
	@Then("User verifies the order confirmation message {string}")
	public void user_verifies_order_confirmation(String expectedConfirmationMessage) throws Throwable {
		System.out.println("\n========== STEP 8: VERIFY ORDER CONFIRMATION ==========");
		boolean isOrderConfirmed = checkoutpage.verifyOrderConfirmation(expectedConfirmationMessage);
		if(isOrderConfirmed) {
			System.out.println("✓ Order confirmed successfully!");
			System.out.println("✓ Confirmation message received: " + expectedConfirmationMessage);
			System.out.println("\n========== TEST PASSED ==========");
			Assert.assertTrue(isOrderConfirmed);
		} else {
			System.out.println("✗ Order confirmation failed!");
			Assert.assertTrue(isOrderConfirmed, "Order confirmation message not found!");
		}
	}

}
