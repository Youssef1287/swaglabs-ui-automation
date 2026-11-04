package com.swaglabs.tests.shopping;

import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import com.swaglabs.data.UserAccount;
import com.swaglabs.pages.CartPage;
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

import java.util.Collections;

@Epic("Shopping")
@Feature("Cart")
public class CartTests extends SignedInTest {

    private CartPage cart;

    @Factory(dataProvider = "shoppers", dataProviderClass = UserProviders.class)
    public CartTests(UserAccount account) {
        super(account);
    }

    @BeforeMethod(alwaysRun = true)
    public void openCartWithBackpack() {
        cart = inventory.addToCart(Product.BACKPACK).header().openCart();
    }

    @Test(description = "The cart lists the product that was added")
    @Severity(SeverityLevel.NORMAL)
    public void listsTheAddedProduct() {
        Assert.assertEquals(cart.itemNames(), Collections.singletonList(Product.BACKPACK.title()));
    }

    @Test(description = "Removing the only product empties the cart")
    @Severity(SeverityLevel.BLOCKER)
    public void removingTheProductEmptiesTheCart() {
        cart.remove(Product.BACKPACK);

        Assert.assertTrue(cart.header().isCartEmpty());
        Assert.assertTrue(cart.itemNames().isEmpty());
    }

    @Test(description = "Continue shopping returns to the product list")
    @Severity(SeverityLevel.NORMAL)
    public void continueShoppingReturnsToTheList() {
        cart.continueShopping();

        assertOn(Route.INVENTORY);
    }

    @Test(description = "Checkout opens the customer information step")
    @Severity(SeverityLevel.NORMAL)
    public void checkoutOpensCustomerInformation() {
        cart.checkout();

        assertOn(Route.CHECKOUT_INFO);
    }
}
