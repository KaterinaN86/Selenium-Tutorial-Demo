package com.herokuapp.theinternet.base;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

//Used for logging data for every test, contains takeScreenshot method.
public class TestListener implements ITestListener {
    Logger log;
    String testName;
    String testMethodName;

    @Override
    public void onTestStart(ITestResult result) {
        //this.testMethodName will be set to method name in tests class.
        this.testMethodName = result.getMethod().getMethodName();
        //this.testMethodName value matches test name in test suite .xml file.
        //this.testMethodName = result.getTestContext().getName();
        log.info("[Starting " + testMethodName + "]");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("[Test " + testMethodName + " passed]");
    }

    /**
     * Method used to automatically take screenshot on test failure.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestFailure(ITestResult result) {
        log.info("[Test " + testMethodName + " failed]");
        String methodName = result.getName().toString().trim();
        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver) context.getAttribute("driver");
        takeScreenshot(methodName + "-failure", driver);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStart(ITestContext context) {
        this.testName = context.getCurrentXmlTest().getName();
        this.log = LogManager.getLogger(testName);
        log.info("[TEST " + testName + " STARTED]");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("[ALL " + testName + " FINISHED]");
    }

    /**
     * Used to take screenshot on test fail.
     *
     * @param fileName String name of image file.
     * @param driver   WebDriver instance, as defined by set parameters.
     */
    public void takeScreenshot(String fileName, WebDriver driver) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //Path to directory where screenshots are saved. Starting from the root of the project (System.getProperty("user.dir") followed by the test-output and screenshots directory a directory named according to current date, test suite name, test name in suite and method nave in class is created. The final directory is named according to current system time.
        String path = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "screenshots" + File.separator + TestUtilities.getTodaysDate() + File.separator + TestUtilities.testSuiteName + File.separator + testName + File.separator + testMethodName + File.separator + TestUtilities.getSystemTime() + " " + fileName + ".png";
        try {
            FileUtils.copyFile(scrFile, new File(path));
        } catch (IOException e) {
            System.err.println("Screenshot file copy failed!");
        }
    }
}
