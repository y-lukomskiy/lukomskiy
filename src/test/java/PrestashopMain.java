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
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

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
        List <WebElement> foundDressesOnly = driver.findElements(By.className("product-description"));
        int numberOfWrongItems = 0;
        for (int a = 0; a < foundDressesOnly.size(); a++){
        if (!foundDressesOnly.get(a).getText().contains("Dress")){
        numberOfWrongItems++;
        }
        }
        if (numberOfWrongItems == 0){
            System.out.println("TEST 4 PASSED - there is no incorrect search results in the list");
        }
        else System.out.println("TEST 4 FAILED - some of results are incorrect. The number of incorrect results - " + numberOfWrongItems);

        // TEST 5 Checking that the number of found items is the same to counter on page
        //Finding the number of displayed items after search
        List <WebElement> foundDresses = driver.findElements(By.className("thumbnail-container"));
        //Saving the counter state on page
        WebElement numberOfFoundItems = driver.findElement(By.xpath("//*[@id=\"js-product-list\"]/nav/div[1]"));
        String resultQuantity = receiveAnyWord(numberOfFoundItems.getText()," ", 4);
        //checking that number of found items is the same to counter value
        if (Integer.parseInt(resultQuantity) == foundDresses.size()){
            System.out.println("TEST 5 PASSED - the number items is the same to counter on page");
        }
        else System.out.println("TEST 5 FAILED - the number items isn't the same to counter on page. Number of items found - " + resultQuantity + ". The counter value is - " + numberOfFoundItems);

        // TEST 5 Checking that only USD is used for goods after selection USD as a currency
        // Finding all prices on page and adding to goodsCurrency variable
        List <WebElement> goodsPriceFull = driver.findElements(By.className("price"));

        ArrayList <String> priceValues = new ArrayList<String>();
        for (int a = 0; a < goodsPriceFull.size(); a++){
            priceValues.add(goodsPriceFull.get(a).getText());
        }
        String usdSymbol = "$";
        for (int a = 0; a < priceValues.size(); a++){
            if (!receiveAnyWord(priceValues.get(a)," ", 2).equals(usdSymbol)){
            System.out.println("TEST 6 FAILED - not USD is used for " + a + " item in list");
            }
        }
        System.out.println("TEST 6 PASSED - only USD is used for goods on screen");

        // TEST 7 - Setting price sorting from High to low
        // Setting other items order
        driver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[2]/div/div/a/i")).click();
        driver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[2]/div/div/div/a[5]")).click();
        // Checking that correct parameter is set
        System.out.println(driver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[2]/div/div")).getText());
        if (!driver.findElement(By.xpath("//*[@id=\"js-product-list-top\"]/div[2]/div/div/a")).getText().contains("Цене: от высокой к низкой"))
            System.out.println("Test 7 failed");
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
