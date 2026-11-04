package com.swaglabs.support;

import com.swaglabs.data.UserAccount;
import com.swaglabs.pages.InventoryPage;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;

/**
 * Base for tests that need a logged-in shopper. The account is passed in through the constructor,
 * so one test class can be run for several accounts with a TestNG @Factory
 * (see UserProviders), instead of copy-pasting the class once per account.
 */
public abstract class SignedInTest extends BaseTest implements ITest {

    private final UserAccount account;
    protected InventoryPage inventory;

    protected SignedInTest(UserAccount account) {
        this.account = account;
    }

    @BeforeMethod(alwaysRun = true)
    public void signIn() {
        inventory = loginPage.loginAs(account);
    }

    @Override
    public String getTestName() {
        return getClass().getSimpleName() + " [" + account + "]";
    }
}
