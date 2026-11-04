package com.swaglabs.tests.shopping;

import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import com.swaglabs.data.UserAccount;
import com.swaglabs.pages.ProductDetailsPage;
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

@Epic("Shopping")
@Feature("Product details")
public class ProductDetailsTests extends SignedInTest {

    private ProductDetailsPage details;

    @Factory(dataProvider = "shoppers", dataProviderClass = UserProviders.class)
    public ProductDetailsTests(UserAccount account) {
        super(account);
    }

    @BeforeMethod(alwaysRun = true)
    public void openBackpack() {
        details = inventory.open(Product.BACKPACK);
    }

    @Test(description = "The details page shows the product name and description")
    @Severity(SeverityLevel.NORMAL)
    public void showsTheProduct() {
        Assert.assertEquals(details.title(), Product.BACKPACK.title());
        Assert.assertTrue(details.showsDescription());
    }

    @Test(description = "A product can be added from its details page")
    @Severity(SeverityLevel.MINOR)
    public void canAddFromDetails() {
        details.addToCart();

        Assert.assertEquals(details.header().cartCount(), 1);
    }

    @Test(description = "A product can be removed again from its details page")
    @Severity(SeverityLevel.MINOR)
    public void canRemoveFromDetails() {
        details.addToCart().removeFromCart();

        Assert.assertTrue(details.header().isCartEmpty());
    }

    @Test(description = "Back to products returns to the list")
    @Severity(SeverityLevel.CRITICAL)
    public void backReturnsToTheList() {
        Assert.assertTrue(details.backToProducts().listsEveryProduct());
        assertOn(Route.INVENTORY);
    }
}
