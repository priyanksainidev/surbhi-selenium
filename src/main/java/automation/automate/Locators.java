package automation.automate;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Locators {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 WebDriverManager.chromedriver().setup();
	        
	        ChromeDriver driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

	        driver.get("https://rahulshettyacademy.com/locatorspractice/");
	        driver.findElement(By.id("inputUsername")).sendKeys("rahul");
	        driver.findElement(By.name("inputPassword")).sendKeys("hello123");
	        driver.findElement(By.className("signInBtn")).click();
	        
	        // Wait for the error message to appear
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.error")));
	        System.out.println(driver.findElement(By.cssSelector("p.error")).getText());
	        
	        // Wait for the "Forgot your password?" link to be clickable
	        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot your password?")));
	        driver.findElement(By.linkText("Forgot your password?")).click();
	        
	        // Wait for the name input field to appear on the password reset page
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Name']")));
	        driver.findElement(By.xpath("//input[@placeholder='Name']")).sendKeys("John");
	       driver.findElement(By.cssSelector("input[placeholder='Email']")).sendKeys("john@gmail.com");
	       driver.findElement(By.xpath("//input[@type='text'][2]")).clear();
	       //driver.findElement(By.cssSelector("input[type='text']nth-child(3)")).sendKeys("john1@gmail.com");
	       driver.findElement(By.cssSelector("input[data-gtm-form-interact-field-id='3']")).sendKeys("john12@gmail.com");
	        driver.findElement(By.xpath("//form/input[3]")).sendKeys("8377917200");
	        driver.findElement(By.cssSelector(".reset-pwd-btn")).click();
	        System.out.println(driver.findElement(By.cssSelector("form p")).getText());
	        //driver.close();
	}

}
