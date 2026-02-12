package automation.automate;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.Assert.assertEquals;

public class assertions {

	public static void main(String[] args) throws InterruptedException {
		
		String name="rahul";
		
		WebDriverManager.chromedriver().setup();
        
        ChromeDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get("https://rahulshettyacademy.com/locatorspractice/");
        driver.findElement(By.id("inputUsername")).sendKeys(name);
        driver.findElement(By.name("inputPassword")).sendKeys("rahulshettyacademy");
        driver.findElement(By.className("signInBtn")).click();
        Thread.sleep(2000);
        System.out.println(driver.findElement(By.tagName("p")).getText());
       // System.out.println(driver.findElement(By.tagName("p")).getText());
        assertEquals("You are successfully logged in.",driver.findElement(By.tagName("p")).getText());
        assertEquals("Hello " + name + ",",driver.findElement(By.cssSelector("div[class='Login-container'] h2")).getText());
        
        driver.findElement(By.xpath("//*[text()='Log Out']")).click();
        //System.out.println(driver.findElement(By.cssSelector("div[class='Login-container'] h2")).getText());
		// TODO Auto-generated method stub

	}

}
