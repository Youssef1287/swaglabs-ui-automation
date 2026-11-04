package com.swaglabs.tests.checkout;

import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import com.swaglabs.data.UserAccount;
import com.swaglabs.flows.CheckoutFlow;
import com.swaglabs.pages.CheckoutOverviewPage;
import com.swaglabs.support.SignedInTest;
import com.swaglabs.support.UserProviders;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.math.BigDecimal;

@Epic("Checkout")
@Feature("Step two: order overview")
public class CheckoutOverviewTests extends SignedInTest {

    private static final Product[] ORDER = {Product.BACKPACK, Product.BIKE_LIGHT};

    private CheckoutOverviewPage overview;

    @Factory(dataProvider = "shoppers", dataProviderClass = UserProviders.class)
    public CheckoutOverviewTests(UserAccount account) {
        super(account);
    }

    @BeforeMethod(alwaysRun = true)
    public void openOverview() {
        overview = CheckoutFlow.toOverview(inventory, ORDER);
    }

    @Test(description = "The item total equals the sum of the product prices")
    @Severity(SeverityLevel.NORMAL)
    public void itemTotalMatchesTheOrder() {
        BigDecimal expected = BigDecimal.ZERO;
        for (Product product : ORDER) {
            expected = expected.add(product.price());
        }

        Assert.assertEquals(overview.itemTotal().compareTo(expected), 0,
                "Item total shown: " + overview.itemTotal() + ", expected: " + expected);
    }

    @Test(description = "Finish completes the order")
    @Severity(SeverityLevel.CRITICAL)
    public void finishCompletesTheOrder() {
        overview.finish();

        assertOn(Route.CHECKOUT_COMPLETE);
    }

    @Test(description = "Cancel goes back to the product list")
    @Severity(SeverityLevel.NORMAL)
    public void cancelReturnsToTheList() {
        overview.cancel();

        assertOn(Route.INVENTORY);
    }
}
