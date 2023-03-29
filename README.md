# Selenium-Tutorial-Demo
Following course https://www.udemy.com/course/advanced-selenium-webdriver/

# Running tests on Selenium Grid
For this purpose a method is added to the base class **BrowserDriverFactory** used for creating corresponding RemoteWebDriver instance. Capabilities and optins for this instance vary depending on the browser parameter passed in the test suite .xml file.

- **com.herokuapp.theinternet.base.BrowserDriverFactory.java** contains method **createDriverGrid** that provides corresponding WebDriver instance.
- Class **com.herokuapp.theinternet.PositiveTests.java** in Test Sources Root directory which extends **com.herokuapp.theinternet.base.BaseTest.java** class in Sources Root directory
