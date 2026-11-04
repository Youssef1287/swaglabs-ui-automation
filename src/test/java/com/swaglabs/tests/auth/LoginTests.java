package com.swaglabs.tests.auth;

import com.swaglabs.config.Route;
import com.swaglabs.config.Settings;
import com.swaglabs.data.UserAccount;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.support.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("User Authentication")
@Feature("Login")
public class LoginTests extends BaseTest {

    @DataProvider(name = "acceptedAccounts")
    public Object[][] acceptedAccounts() {
        return new Object[][]{
                {UserAccount.STANDARD},
                {UserAccount.PERFORMANCE_GLITCH}
        };
    }

    @DataProvider(name = "rejectedLogins")
    public Object[][] rejectedLogins() {
        String password = Settings.sharedPassword();
        String standard = UserAccount.STANDARD.username();
        return new Object[][]{
                // username, password, text expected in the error banner
                {"unknown_user", password, "do not match any user"},
                {standard, "wrong_password", "do not match any user"},
                {"", password, "Username is required"},
                {standard, "", "Password is required"},
                {UserAccount.LOCKED_OUT.username(), password, "locked out"}
        };
    }

    @Test(dataProvider = "acceptedAccounts", description = "Accepted accounts land on the product list")
    @Severity(SeverityLevel.BLOCKER)
    public void acceptedAccountsReachTheInventory(UserAccount account) {
        InventoryPage inventory = loginPage.loginAs(account);

        assertOn(Route.INVENTORY);
        Assert.assertFalse(inventory.productNames().isEmpty(), "The product list should not be empty");
    }

    @Test(dataProvider = "rejectedLogins", description = "Bad credentials are rejected with a clear message")
    @Severity(SeverityLevel.CRITICAL)
    public void rejectedLoginsShowAnError(String username, String password, String expectedText) {
        String message = loginPage.submit(username, password).errorMessage();

        Assert.assertTrue(message.contains(expectedText),
                "Expected the error to contain '" + expectedText + "' but it was: " + message);
        assertOn(Route.LOGIN);
    }
}
