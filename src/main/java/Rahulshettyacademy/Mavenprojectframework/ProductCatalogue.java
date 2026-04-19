package Rahulshettyacademy.Mavenprojectframework;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Abstractcomponents.Abstractcomponents;

public class ProductCatalogue extends Abstractcomponents{
	WebDriver driver;
	WebDriverWait wait;
	
	public ProductCatalogue(WebDriver driver)
	{
		super(driver);
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		PageFactory.initElements(driver, this);
	}
	
	//WebElement userEmail=driver.findElement(By.id("userEmail"));
	//pagefactory
	//driver.findElements(By.cssSelector(".mb-3"));
	@FindBy(css=".mb-3")
	List<WebElement> products;
	@FindBy(css=".ng-animating")
	WebElement spinner;
	
	By productsby = By.cssSelector(".mb-3");
	By addTocart = By.cssSelector("button.btn.w-10");
	By toastmessage = By.cssSelector("#toast-container");
	
	public List<WebElement> getProductsList()
	{
		waitForElementToAppear(productsby);
		return products;
	}
	

	public WebElement getProductByName(String productName)
	{
		List<WebElement> productsList = getProductsList();
		System.out.println("Searching for product: " + productName);
		System.out.println("Total products available: " + productsList.size());
		
		// First, list all available products for debugging
		for(WebElement product : productsList) {
			try {
				String productText = product.findElement(By.cssSelector("b")).getText();
				System.out.println("  Available product: " + productText);
			} catch(Exception e) {
				System.out.println("  Could not extract product name from element");
			}
		}
		
		WebElement prod = productsList.stream()
			.filter(product -> {
				try {
					String pName = product.findElement(By.cssSelector("b")).getText();
					return pName.equals(productName);
				} catch(Exception e) {
					return false;
				}
			})
			.findFirst()
			.orElse(null);
		
		if(prod == null) {
			System.out.println("✗ ERROR: Product '" + productName + "' not found!");
			throw new RuntimeException("Product '" + productName + "' not found in the product catalogue");
		}
		
		return prod;
	}
	
	public void addProductToCart(String productName) throws InterruptedException
	{
		WebElement prod = getProductByName(productName);
		
		if(prod == null) {
			throw new RuntimeException("Product element is null - cannot add to cart");
		}
		
		WebElement addBtn = prod.findElement(addTocart);
		if(addBtn == null) {
			throw new RuntimeException("Add to Cart button not found for product: " + productName);
		}
		
		// Scroll the Add to Cart button into view
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBtn);
		Thread.sleep(500);
		
		// Wait for button to be clickable
		wait.until(ExpectedConditions.elementToBeClickable(addBtn));
		
		// Click using JavaScript executor for reliable interaction
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
		System.out.println("Successfully clicked Add to Cart for: " + productName);
		
		// Wait for toast message to appear
		waitForElementToAppear(toastmessage);
		
		// Wait for spinner/animation to disappear
		waitForElementToDisappear(spinner);
	}
}
	
