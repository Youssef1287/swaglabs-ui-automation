package com.swaglabs.tests.checkout;

import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import com.swaglabs.data.UserAccount;
import com.swaglabs.flows.CheckoutFlow;
import com.swaglabs.pages.OrderCompletePage;
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

@Epic("Checkout")
@Feature("Order confirmation")
public class OrderCompleteTests extends SignedInTest {

    private OrderCompletePage complete;

    @Factory(dataProvider = "shoppers", dataProviderClass = UserProviders.class)
    public OrderCompleteTests(UserAccount account) {
        super(account);
    }

    @BeforeMethod(alwaysRun = true)
    public void placeAnOrder() {
        complete = CheckoutFlow.placeOrder(inventory, Product.BACKPACK);
    }

    @Test(description = "A thank-you message confirms the order")
    @Severity(SeverityLevel.CRITICAL)
    public void confirmsTheOrder() {
        Assert.assertEquals(complete.confirmation(), "Thank you for your order!");
    }

    @Test(description = "Back home returns to the product list")
    @Severity(SeverityLevel.NORMAL)
    public void backHomeReturnsToTheList() {
        complete.backHome();

        assertOn(Route.INVENTORY);
    }

    @Test(description = "The side menu offers all of its links")
    @Severity(SeverityLevel.MINOR)
    public void sideMenuShowsEveryLink() {
        Assert.assertTrue(complete.header().sideMenu().showsEveryLink());
    }

    @Test(description = "Logging out from the menu returns to the login page")
    @Severity(SeverityLevel.MINOR)
    public void logoutReturnsToLogin() {
        complete.header().sideMenu().logout();

        assertOn(Route.LOGIN);
    }
}
