import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverInitializator {
    public static WebDriver getDriver() {
        // way to Driver
        String driverPath = System.getProperty("user.dir") + "//resources/chromedriver.exe";
        if (driverPath == null) {
            System.out.println("Incorrect way to driver(null)");
        }
        // driver parameters
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new ChromeDriver();
    }
}
