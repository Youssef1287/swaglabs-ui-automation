package com.swaglabs.support;

import com.swaglabs.config.Route;
import com.swaglabs.core.DriverManager;
import com.swaglabs.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/** Gives every test a fresh browser that starts on the login page. */
public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void openBrowser() {
        driver = DriverManager.start();
        loginPage = LoginPage.open(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        DriverManager.stop();
    }

    protected void assertOn(Route expected) {
        Assert.assertTrue(expected.matches(driver.getCurrentUrl()),
                "Expected to be on " + expected.url() + " but the browser is on " + driver.getCurrentUrl());
    }
}
