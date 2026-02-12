package automation.automate;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;


public class Forgotpassword {

	public static void main(String[] args) {
		 WebDriverManager.chromedriver().setup();
	        
	        ChromeDriver driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	        String password = getPassword(driver, wait);
	        driver.get("https://rahulshettyacademy.com/locatorspractice/");
	        driver.findElement(By.id("inputUsername")).sendKeys("rahul");
	        driver.findElement(By.name("inputPassword")).sendKeys(password);
	        driver.findElement(By.className("signInBtn")).click();
	}
	public static String getPassword(ChromeDriver driver, WebDriverWait wait)
	{

	        driver.get("https://rahulshettyacademy.com/locatorspractice/");
	        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot your password?")));                   
	        driver.findElement(By.linkText("Forgot your password?")).click();
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("reset-pwd-btn")));
	        driver.findElement(By.className("reset-pwd-btn")).click();
	        String PasswordText = driver.findElement(By.className("infoMsg")).getText();
	        String[] Passwordarray= PasswordText.split("'");
	        String[] Passwordarray1= Passwordarray[1].split("'");
	        String password = Passwordarray[1].split("'")[0];
	        return password;
	        
	        
		// TODO Auto-generated method stub

	}

}
