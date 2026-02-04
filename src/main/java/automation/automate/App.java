package automation.automate;

/**
 * Hello world!
 *
 */
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class App 
{
    public static void main( String[] args )
    {
    	
    	getdata();
    	app1 a1=new app1();
    	a1.display();
    	
    }
    public static void getdata()
    
    {
    	System.out.println("getdata");
    }
}

