package com.herokuapp.theinternet.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

//Added annotation for testNG listener to be used for all test suites.
@Listeners({com.herokuapp.theinternet.base.TestListener.class})
public class BaseTest {
    protected static String testSuiteName;
    protected WebDriver driver;
    protected Logger log;
    protected String testName;
    protected String testMethodName;

    /**
     * Method executed before each test method. It uses @Parameters annotation to get parameters from XML test suite or from maven command line.
     *
     * @param method           Method object that provides information about, and access to, a single method on a class or interface. The reflected method may be a class method or an instance method (including an abstract method).
     * @param browser          String parameter used to define browser used to perform tests. Default value is set to "chrome".
     * @param profile          String parameter used to determine if user profile needs to be created when using the browser.
     * @param deviceName       String parameter, used for mobile device emulation.
     * @param environment      String parameter that can be set to "grid" or "local". Used to determine whether tests will be performed on local machine or on grid.
     * @param ip               String parameter used for testing with Selenium Grid.
     * @param enableFileUpload String parameter used for enabling local file detection when performing file upload tests on selenium grid.
     * @param ctx              ITestContext object used for information about current testName and test suite name.
     */
    @Parameters({"browser", "chromeProfile", "deviceName", "environment", "ip", "enableFileUpload"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, @Optional("chrome") String browser, @Optional String profile, @Optional String deviceName, @Optional String environment, @Optional String ip, @Optional String enableFileUpload, ITestContext ctx) {
        String testName = ctx.getCurrentXmlTest().getName();
        log = LogManager.getLogger(testName);
        BrowserDriverFactory factory = new BrowserDriverFactory(browser, log);
        if (profile != null) {
            driver = factory.createChromeWithProfile(profile);
        } else if (deviceName != null) {
            driver = factory.createChromeWithMobileEmulation(deviceName);
        } else if ((environment != null) && (environment.equals("grid"))) {
            if (enableFileUpload == null) {
                enableFileUpload = "false";
            }
            driver = factory.createDriverGrid(enableFileUpload, ip);
        } else {
            driver = factory.createDriver();
        }
        ctx.setAttribute("driver", driver);
        driver.manage().window().maximize();
        setCurrentThreadName();
        this.testSuiteName = ctx.getSuite().getName();
        this.testName = testName;
        this.testMethodName = method.getName();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Close driver");
        // Close browser
        driver.quit();
    }

    /**
     * Sets thread name which includes thread id
     */
    private void setCurrentThreadName() {
        Thread thread = Thread.currentThread();
        String threadName = thread.getName();
        String threadId = String.valueOf(thread.getId());
        if (!threadName.contains(threadId)) {
            thread.setName(threadName + " " + threadId);
        }
    }
}