package Rahulshettyacademy.Mavenprojectframework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Abstractcomponents.Abstractcomponents;

public class Landingpage extends Abstractcomponents{
	WebDriver driver;
	public Landingpage(WebDriver driver)
	{
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	//WebElement userEmail=driver.findElement(By.id("userEmail"));
	//pagefactory
	@FindBy(id="userEmail")
	WebElement userEmail;
	//driver.findElement(By.id("userPassword"))
	@FindBy(id="userPassword")
	WebElement password;
	//driver.findElement(By.id("login")
	@FindBy(id="login")
	WebElement submit;
	public void loginApplicatiion(String email, String pass)
	{
		userEmail.sendKeys(email);
		password.sendKeys(pass);
		submit.click();
		
	}
	public void goTo()
	{
		  driver.get("https://rahulshettyacademy.com/client");
	}
	}
	
