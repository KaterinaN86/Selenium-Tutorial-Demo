package com.herokuapp.theinternet;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestAuthentication {
    @Test (groups = { "Sniff", "Regression" })
    public void validAuthenticationTest(){
        System.out.println(" Sniff + Regression" + System.getProperty("environment"));
    }

    @Test (groups = { "Regression" })
    @Parameters("environment")
    public void failedAuthenticationTest(String environment){
        System.out.println("Regression-"+environment);
    }

    @Parameters("environment")
    @Test(groups = { "Sniff"})
    public void newUserAuthenticationTest(String environment){
        System.out.println("Sniff-"+environment);
    }
}
