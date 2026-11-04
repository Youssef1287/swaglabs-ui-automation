package com.swaglabs.pages;

import com.swaglabs.config.Route;
import com.swaglabs.config.Settings;
import com.swaglabs.data.UserAccount;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By SUBMIT = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.LOGIN);
    }

    public static LoginPage open(WebDriver driver) {
        driver.get(Route.LOGIN.url());
        return new LoginPage(driver);
    }

    /** Fills and submits the form, staying on this page object. Use it for negative scenarios. */
    @Step("Submit login form with username '{username}'")
    public LoginPage submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(SUBMIT);
        return this;
    }

    /** Logs in with a demo account and returns the inventory page. */
    @Step("Log in as {account}")
    public InventoryPage loginAs(UserAccount account) {
        submit(account.username(), Settings.sharedPassword());
        return new InventoryPage(driver);
    }

    public String errorMessage() {
        return textOf(ERROR);
    }
}
