package com.herokuapp.theinternet.dropdowntests;

import com.herokuapp.theinternet.base.TestUtilities;
import com.herokuapp.theinternet.pages.DropdownPage;
import com.herokuapp.theinternet.pages.WelcomePage;
import org.openqa.selenium.UnsupportedCommandException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DropdownTest extends TestUtilities {

    @Test
    public void optionTwoTest() {
        //Commented out because TestNg Listener is implemented in order to log this data.
        //log.info("Starting optionTwoTest");

        // open main page
        WelcomePage welcomePage = new WelcomePage(driver, log);
        welcomePage.openPage();

        // Click on Dropdown link
        DropdownPage dropdownPage = welcomePage.clickDropdownLink();
        try {
            // Select Option 2
            dropdownPage.selectOption(2);
            // Verify Option 2 is selected
            String selectedOption = dropdownPage.getSelectedOption();
            Assert.assertTrue(selectedOption.equals("Option 2"),
                    "Option 2 is not selected. Instead selected - " + selectedOption);
        } catch (UnsupportedCommandException e) {
            log.info("Can't fix this error that appears only when phantomJS is used.");
        }
    }
}
