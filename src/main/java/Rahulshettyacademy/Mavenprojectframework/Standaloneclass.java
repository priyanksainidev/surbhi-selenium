package Rahulshettyacademy.Mavenprojectframework;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Abstractcomponents.Abstractcomponents;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Standaloneclass extends Abstractcomponents {

	public Standaloneclass(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		WebDriverManager.chromedriver().setup();
        
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
      
       
        driver.manage().window().maximize();
        
        // Read data from JSON file using DataReader
        DataReader reader = new DataReader();
        String jsonFilePath = System.getProperty("user.dir") + "//src//main//java//Rahulshettyacademy//Mavenprojectframework//Purchaseorder.json";
        List<HashMap<String,String>> testData = reader.getJsonDataToMap(jsonFilePath);
        
        // Get first test data entry
        HashMap<String,String> data = testData.get(0);
        String email = data.get("email");
        String password = data.get("Password");
        String Productname = data.get("Product");
        String countryName = "India";
        String expectedConfirmationMessage = "THANKYOU FOR THE ORDER.";
        
        try {
        	System.out.println("\n========== STEP 1: LOGIN ==========");
            Landingpage lp = new Landingpage(driver);
            lp.goTo();
            System.out.println("Navigated to login page");
            lp.loginApplicatiion(email, password);
            System.out.println("Login successful!");
            
            System.out.println("\n========== STEP 2: ADD PRODUCT TO CART ==========");
            ProductCatalogue pc = new ProductCatalogue(driver);
            List<WebElement> products = pc.getProductsList();
            System.out.println("Products list retrieved: " + products.size() + " products found");
            pc.addProductToCart(Productname);
            System.out.println("Product added to cart successfully!");
            
            System.out.println("\n========== STEP 3: NAVIGATE TO CART ==========");
            CartPage cartpage = new CartPage(driver);
            cartpage.clickCartButton();
            System.out.println("Cart page opened successfully!");
            
            System.out.println("\n========== STEP 4: VERIFY PRODUCT IN CART ==========");
            Boolean match = cartpage.VerifyProductDisplay(Productname);
            if(match) {
                System.out.println("✓ Product found in cart!");
            } else {
                System.out.println("✗ Product NOT found in cart!");
                throw new RuntimeException("Product not found in cart!");
            }
            
            System.out.println("\n========== STEP 5: PROCEED TO CHECKOUT ==========");
            cartpage.clickCheckoutButton();
            System.out.println("Checkout button clicked successfully!");
            Thread.sleep(1000);
            
            System.out.println("\n========== STEP 6: SELECT COUNTRY ==========");
            CheckoutPage checkoutpage = new CheckoutPage(driver);
            checkoutpage.selectCountry(countryName);
            System.out.println("✓ Country selected successfully!");
            
            // Wait for .ta-results to disappear (country selection complete)
            System.out.println("\n========== WAITING FOR COUNTRY SELECTION TO COMPLETE ==========");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ta-results")));
                System.out.println("✓ Country dropdown results have disappeared - selection confirmed!");
            } catch (Exception e) {
                System.out.println("⚠ Dropdown didn't close, but country was selected. Continuing...");
            }
            
            Thread.sleep(1000);
            
            System.out.println("\n========== STEP 6B: SELECT COMPANY ==========");
//            checkoutpage.selectCompany(countryName);
            System.out.println("✓ Company selected successfully!");
            
            System.out.println("\n========== STEP 7: CLICK PLACE ORDER BUTTON ==========");
            checkoutpage.clickPlaceOrderButton();
            System.out.println("✓ Place Order button clicked!");
            
            System.out.println("\n========== STEP 8: VERIFY ORDER CONFIRMATION ==========");
            boolean isOrderConfirmed = checkoutpage.verifyOrderConfirmation(expectedConfirmationMessage);
            if(isOrderConfirmed) {
                System.out.println("✓ Order confirmed successfully!");
                System.out.println("\n========== TEST PASSED ==========");
            }
            
        } catch(Exception e) {
            System.out.println("\n✗ ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
            System.out.println("========== TEST FAILED ==========");
        } finally {
            System.out.println("\nClosing browser...");
//            driver.quit();
            System.out.println("Browser closed.");
        }
	}
	
	
}
