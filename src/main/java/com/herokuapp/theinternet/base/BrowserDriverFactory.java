package com.herokuapp.theinternet.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserDriverFactory {

    ChromeOptions chromeOptions;
    private WebDriver driver;
    private String browser;
    private Logger log;

    public BrowserDriverFactory(String browser, Logger log) {
        this.browser = browser.toLowerCase();
        this.log = log;
        this.chromeOptions = new ChromeOptions();
    }

    public WebDriver createDriver() {
        // Create driver
        log.info("Create driver: " + browser);

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                //After Chrome browser 1.1.0 and ChromeDriver update there is forbidden access issue.
                //Adding this argument to the options object is necessary for chromeDriver to work.
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                //WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "chromeheadless":
                WebDriverManager.chromedriver().setup();
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--remote-allow-origins=*");
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
                        "src/main/resources/phantomjs.exe");
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
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;
        }
        return driver;
    }

    public WebDriver createChromeWithProfile(String profile) {
        log.info("Starting chrome driver with profile: " + profile);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("user-data-dir=" + System.getProperty("user.dir") + "//src//main//resources//Profiles//" + profile);
        chromeOptions.addArguments("--remote-allow-origins=*");
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
        chromeOptions.addArguments("--remote-allow-origins=*");
        //Create driver object for Chrome
        driver = new ChromeDriver(chromeOptions);
        return driver;
    }

    public WebDriver createRemoteFirefox() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        WebDriver driver = new RemoteWebDriver(firefoxOptions);
        return driver;
    }

    /**
     * Method used for creating RemoteWebDriver instance used for performing tests on SeleniumGrid. Depending on browser variable value certain instance is created with corresponding capabilities and options.
     *
     * @return WebDriver instance
     */
    public WebDriver createDriverGrid() {
        //Defining URL of SeleniumGrid hub used for Firefox and Chrome nodes.
        String hubUrl = "http://40.114.204.255:4444/wd/hub";
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", browser);
        log.info("Starting \" + browser + \" on grid");
        // Creating driver
        if (browser.equals("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.merge(capabilities);
            try {
                driver = new RemoteWebDriver(new URL(hubUrl), chromeOptions);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (browser.equals("microsoftedge")) {
            //Edge browser could not be started as part of Selenium Grid for other browsers so separate grid form standalone docker image is created. Value of hubURL variable corresponds to edge standalone IP.
            //hubUrl = "http://192.168.1.109:4444/wd/hub";
            hubUrl = "http://192.168.1.109:5555/wd/hub";
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            //Following capabilities will vary depending on configuration of machine where SeleniumGrid is running.
            options.setCapability("platform", "Windows 10");
            options.setCapability("browserVersion", "111.0");
            capabilities.setCapability("maxInstances", "1");
            options.merge(capabilities);
            try {
                driver = new RemoteWebDriver(new URL(hubUrl), options);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        return driver;
    }
}