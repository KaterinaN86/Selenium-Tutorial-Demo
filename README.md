# Selenium-Tutorial-Demo
Following course https://www.udemy.com/course/advanced-selenium-webdriver/

# Project structure
**P**age **O**bject **M**odel design pattern is used to reduce test duplication and enable easier maintenance.  enhancing test maintenance and reducing code duplication. An object-oriented class is created for every page of the application under test (AUT) that serves as an interface to each page. The tests then use the methods of this page object class whenever they need to interact with the UI of that page. The benefit is that if the UI changes for the page, the tests themselves don’t need to change, only the code within the page object needs to change. Subsequently, all changes to support that new UI are located in one place.

Package **base** in the Sources Root folder (/src/main/java folder marked as sources root by Maven) contains classes with methods that are called several times for testing multiple different pages. It contains following classes:
   1. **BaseTest.java** with variables and methods used throughout all tests. For example, here the **driver** variable is created, the log variable for log4j logging, and also methods that are executed before and after every test (setup method and tearDown to stop driver).
   2. **BrowserDriverFactory.java** initializes the driver variable depending on the parameters for each test. It contains several methods that used different approaches to initialize the driver, like creating driver with browser profile, creating driver used for selenium grid tests, initializing driver for mobile emulation and so on.
   3. Helper classes **CsvDataProviders.java** and **ExtentReporterNG**, used as selenium TestNG data providers required for Selenium Data Driven Framework and generating TestNG reports correspondingly. Class **TestListener.java** implements ITestListener interface which is part of the so called TestNG listeners interfaces. They are used to modify TestNG behavior. In this project it is used to add logging data like test name from class and test name from test suite or started test.
   4. **TestUtilities.java** is the parent class of all test classes in Test Sources Root directory (/src/test/java directory). It extends **BaseTest.java** and has some additional methods for getting browser logs, creating data provider and so on.

- Package **pages** contains all classes that represent a page from the application under test. Class **BasePageObject.java** is the parent class for pages classes. Contains variables and methods used on multiple pages. 

# Running tests on Selenium Grid
For this purpose a method is added to the base class **BrowserDriverFactory** used for creating corresponding RemoteWebDriver instance. Capabilities and optins for this instance vary depending on the browser parameter passed in the test suite .xml file.

- **com.herokuapp.theinternet.base.BrowserDriverFactory.java** contains method **createDriverGrid** that provides corresponding WebDriver instance.
- Class **com.herokuapp.theinternet.PositiveTests.java** in Test Sources Root directory which extends **com.herokuapp.theinternet.base.BaseTest.java** class in Sources Root directory

# Generating reports using **ExtentReporterNG** 
 - Detailed explanation can be found here [https://www.ontestautomation.com/using-the-extentreports-testng-listener-in-selenium-page-object-tests/](https://www.ontestautomation.com/using-the-extentreports-testng-listener-in-selenium-page-object-tests/).
 - Suitable dependencies are added:
```  <!-- https://mvnrepository.com/artifact/com.relevantcodes/extentreports -->
        <dependency>
            <groupId>com.relevantcodes</groupId>
            <artifactId>extentreports</artifactId>
            <version>2.41.2</version>
        </dependency>
  ```      
 - Class **ExtentReporterNG.java** implements IReporter interface. In this class, method **generateReport** is overridden in order to generate report. Created listener for generating extent report is used in **SmokeTestSuite**:
```
 <listeners>
        <listener class-name="com.herokuapp.theinternet.base.ExtentReporterNG"></listener>
    </listeners>
```
   When we run **SmokeTestSuite.xml** file, an ExtentReports HTML report is created in the default test-output folder.
   ![Generated report using ExtentReport TestNG listener, after running SmoteTestSuite.xml](src/main/resources/readmeImg/ExtentReports.png)