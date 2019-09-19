import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import java.beans.EventHandler;
import java.util.concurrent.TimeUnit;

public class DriverInitialization {

    public static WebDriver driver;

    public static WebDriver getDriver(String enterBrowser) {
        // options for driver
        if (enterBrowser.equals("chrome")) {
        // way to Driver
        String driverPath = System.getProperty("user.dir") + "//resources/chromedriver.exe";
        // driver parameters
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
            }
        if (enterBrowser.equals("firefox")) {
        // way to Driver
        String driverPath = System.getProperty("user.dir") + "//resources/geckodriver.exe";
        // driver parameters
        System.setProperty("webdriver.gecko.driver", driverPath);
        driver = new FirefoxDriver();
        }
        // Adding parameters to driver
        driver.manage().window().maximize();
        // providing time to load the page
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        // setting implicitly wait
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
        }
}
//EventFiringWebDriver driver = new EventFiringWebDriver(new ChromeDriver());
