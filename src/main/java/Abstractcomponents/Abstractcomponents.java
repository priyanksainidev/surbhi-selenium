package Abstractcomponents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Abstractcomponents {
	WebDriver driver;
	public Abstractcomponents(WebDriver driver)
	{
		this.driver=driver;
		
	}
	
	
	
	
	public void waitForElementToAppear(By findBy)
	{
		WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait1.until(ExpectedConditions.visibilityOfElementLocated(findBy));
	}
	
	public void waitForElementToDisappear(WebElement ele)
	{
		WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait1.until(ExpectedConditions.invisibilityOf(ele));
	}
	

}
