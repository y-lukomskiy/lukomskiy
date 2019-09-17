import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrestashopMain {

    public static void main(String[] args) throws InterruptedException {
        // initializing driver
        WebDriver driver = getDriver();
        // opening browser fullscreen
        driver.manage().window().maximize();
        // setting implicitly wait
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // TEST 1 navigate to site
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        WebElement contactUs = driver.findElement(By.xpath("//*[@id=\"contact-link\"]/a"));
        if (contactUs.getText().equals("Свяжитесь с нами"))
            System.out.println("TEST 1 PASSED - the site is displayed");
        else {
            System.out.println("TEST 1 FAILED - site isn't opened correctly");

        }

        // TEST 2 Checking that good price is the same is set for page
        //Get currency symbol in header
        WebElement currencyForHeader = driver.findElement(By.xpath("//*[@id=\"_desktop_currency_selector\"]/div/span[2]"));
        String currencyFullInHeader = currencyForHeader.getText();
        String currencySymbolInHeader = receiveAnyWord(currencyFullInHeader," ",2);
        // Get currency for goods
        WebElement currencyForGoods = driver.findElement(By.xpath("//*[@id=\"content\"]/section/div/article[1]/div/div[1]/div/span"));
        String currencyFullForGoods = currencyForGoods.getText();
        String currencySymbolOnGoods = receiveAnyWord(currencyFullForGoods," ",2);
       //check of result - is the currency is the same
        if(!currencySymbolInHeader.equals(currencySymbolOnGoods)){
            System.out.println("TEST 2 FAILED - the currency for goods differs from set in header");
        }
        else System.out.println("TEST 2 PASSED - the currency for goods is same to currency in header");

        // TEST 3 Checking possibility to select other currency
        // Opening currency drop-down
        driver.findElement(By.xpath("//*[@id=\"_desktop_currency_selector\"]/div/a/i")).click();
        // Selecting USD as a currency
        driver.findElement(By.xpath("//*[@id=\"_desktop_currency_selector\"]/div/ul/li[3]/a")).click();
        // Finding set currency
        String currencySet = driver.findElement(By.xpath("//*[@id=\"_desktop_currency_selector\"]/div/span[2]")).getText().toString();
        if (currencySet.equals("USD $")){
            System.out.println("TEST 3 PASSED - the currency set correctly in drop-down");
        }
        else System.out.println("TEST 3 FAILED - incorrect currency is set");

        // TEST 4 Checking search works correctly
        // Making search
        WebElement searchField = driver.findElement(By.xpath("//*[@id=\"search_widget\"]/form/input[2]"));
        searchField.click();
        searchField.sendKeys("Dress");
        searchField.submit();
        // Checking that only items which contain word "Dress" are displayed
        List <WebElement> foundDressesOnly = driver.findElements(By.className("h3 product-title"));
        System.out.println(foundDressesOnly.get(0).toString());


        int numberOfWrongItems = 0;
        for (int a = 0; a < foundDressesOnly.size(); a++){

            if (!foundDressesOnly.get(a).getText().contains("Dress")){
                numberOfWrongItems++;
            }
        }
        System.out.println(numberOfWrongItems);

        //Finding the number of displayed records
        List <WebElement> foundDresses = driver.findElements(By.className("thumbnail-container"));
        //Temporal check of result - remove in final version
        System.out.println(foundDresses.size());
        //Saving the counter state on page
        WebElement numberOfFoundItems = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/nav/div[1]"));
        String resultQuantity = receiveAnyWord(numberOfFoundItems.getText()," ", 4);
        //Temporal check of result - remove in final version
        System.out.println(resultQuantity);
        //checking if number is the same
        if (Integer.parseInt(resultQuantity) == foundDresses.size()){
            System.out.println("Pass - the number is same to quantity of search results");
        }
        else System.out.println("Fail - number differs");

        // Checking that only USD is used for goods
        List <WebElement> goodsCurrency = driver.findElements(By.className("price"));
        ArrayList <String> priceValues = new ArrayList<String>();
        for (int a = 0; a < goodsCurrency.size(); a++){
            priceValues.add(goodsCurrency.get(a).getText());
        }
        String usdSymbol = "$";
        for (int a = 0; a < priceValues.size(); a++){
            //Temporal check of result - remove in final version
            System.out.println(priceValues.get(a));
            if (!receiveAnyWord(priceValues.get(a)," ", 2).equals(usdSymbol)){
                System.out.println("Fail = not USD is used for " + a + " item in list");
            }
        }
        System.out.println("not failed - means passed)");

        // Setting other items order
        driver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[2]/div/div/a/i")).click();
        driver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[2]/div/div/div/a[5]")).click();

        // Creating

        //Temporal check of result - remove in final version
        Thread.sleep(500);
        // Close driver after check
        driver.quit();
    }

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

    // method to get any word from string - divides string "stringToDivide" with symbol/s set in divider on wordNumber
    public static String receiveAnyWord (String stringToDivide, String divider, Integer wordNumber){
        String currencySymbol = null;
        for (String data : stringToDivide.split(divider, wordNumber)) {
            currencySymbol = data;
        }
        return currencySymbol;
    }
}
