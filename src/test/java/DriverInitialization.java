import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.beans.EventHandler;
import java.util.concurrent.TimeUnit;

public class DriverInitialization {
    public static WebDriver getDriver() {
        // way to Driver
        String driverPath = System.getProperty("user.dir") + "//resources/chromedriver.exe";
        if (driverPath == null) {
            System.out.println("Incorrect way to driver(null)");
        }
        // driver parameters
        System.setProperty("webdriver.chrome.driver", driverPath);
        // logging isn't fully implemented here!!!!
        EventFiringWebDriver driver = new EventFiringWebDriver(new ChromeDriver());
        // opening browser fullscreen
        driver.manage().window().maximize();
        // providing time to load the page
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        // setting implicitly wait
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }
}
