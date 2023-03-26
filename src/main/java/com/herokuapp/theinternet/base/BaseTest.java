package com.herokuapp.theinternet.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners({com.herokuapp.theinternet.base.TestListener.class})
public class BaseTest {

    protected static String testSuiteName;

    protected WebDriver driver;

    protected Logger log;


    protected String testName;
    protected String testMethodName;

    @Parameters({"browser", "chromeProfile", "deviceName", "remoteFirefox", "environment"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, @Optional("chrome") String browser, @Optional String profile, @Optional String deviceName, @Optional String remoteFirefox, @Optional String environment, ITestContext ctx) {
        String testName = ctx.getCurrentXmlTest().getName();
        log = LogManager.getLogger(testName);
        BrowserDriverFactory factory = new BrowserDriverFactory(browser, log);
        if (profile != null) {
            driver = factory.createChromeWithProfile(profile);
        } else if (deviceName != null) {
            driver = factory.createChromeWithMobileEmulation(deviceName);
        } else if (remoteFirefox != null) {
            driver = factory.createRemoteFirefox();
        } else if (environment != null && environment.equals("grid")) {
            driver = factory.createDriverGrid();
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