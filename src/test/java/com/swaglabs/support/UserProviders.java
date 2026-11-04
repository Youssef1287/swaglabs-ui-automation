package com.swaglabs.support;

import com.swaglabs.data.UserAccount;
import org.testng.annotations.DataProvider;

/** Supplies the standard shopper account to signed-in test classes. */
public final class UserProviders {

    private UserProviders() {
    }

    @DataProvider(name = "shoppers")
    public static Object[][] shoppers() {
        return new Object[][]{
                {UserAccount.STANDARD}
        };
    }
}
