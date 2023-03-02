package com.herokuapp.theinternet.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class BrowserDriverFactory {

    private WebDriver driver;
    private String browser;
    private Logger log;

    public BrowserDriverFactory(String browser, Logger log) {
        this.browser = browser.toLowerCase();
        this.log = log;
    }

    public WebDriver createDriver() {
        // Create driver
        log.info("Create driver: " + browser);

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "chromeheadless":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefoxheadless":
                WebDriverManager.firefoxdriver().setup();
                FirefoxBinary firefoxBinary = new FirefoxBinary();
                firefoxBinary.addCommandLineOptions("--headless");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBinary(firefoxBinary);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "phantomjs":
                Capabilities caps = new DesiredCapabilities();

                ((DesiredCapabilities) caps).setJavascriptEnabled(true);

                ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);

                ((DesiredCapabilities) caps).setCapability(
                        PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                        "src/main/resources/phantomjs.exe"
                );
                //********
                //System.setProperty("phantomjs.binary.path", "src/main/resources/phantomjs.exe");
                ThreadLocal<WebDriver> phantomDriver = new ThreadLocal<WebDriver>();
                //phantomDriver.set(new PhantomJSDriver());
                phantomDriver.set(new PhantomJSDriver(caps));
                return phantomDriver.get();
            case "htmlunit":
                ThreadLocal<WebDriver> htmlUnitDriver = new ThreadLocal<WebDriver>();
                htmlUnitDriver.set(new HtmlUnitDriver());
                return htmlUnitDriver.get();
            default:
                System.out.println("Do not know how to start: " + browser + ", starting chrome.");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }
        
        return driver;
    }

    public WebDriver createChromeWithProfile(String profile) {
        log.info("Starting chrome driver with profile: " + profile);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("user-data-dir=" + System.getProperty("user.dir") + "//src//main//resources//Profiles//" + profile);
        //Create driver object for Chrome
        driver = new ChromeDriver(chromeOptions);
        return driver;
    }

    public WebDriver createChromeWithMobileEmulation(String deviceName) {
        log.info("Starting driver with " + deviceName + " emulation]");
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        //Create driver object for Chrome
        driver = new ChromeDriver(chromeOptions);
        return driver;
    }
}
