package com.swaglabs.tests.shopping;

import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import com.swaglabs.data.SortOption;
import com.swaglabs.data.UserAccount;
import com.swaglabs.support.SignedInTest;
import com.swaglabs.support.UserProviders;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Shopping")
@Feature("Product list")
public class InventoryTests extends SignedInTest {

    @Factory(dataProvider = "shoppers", dataProviderClass = UserProviders.class)
    public InventoryTests(UserAccount account) {
        super(account);
    }

    @DataProvider(name = "sortOptions")
    public Object[][] sortOptions() {
        Object[][] rows = new Object[SortOption.values().length][1];
        for (int i = 0; i < rows.length; i++) {
            rows[i][0] = SortOption.values()[i];
        }
        return rows;
    }

    @Test(description = "Every catalogue product is listed with a picture")
    @Severity(SeverityLevel.MINOR)
    public void listsEveryProduct() {
        Assert.assertTrue(inventory.listsEveryProduct());
    }

    @Test(description = "Adding one product shows 1 on the cart badge")
    @Severity(SeverityLevel.MINOR)
    public void addingOneProductUpdatesTheBadge() {
        inventory.addToCart(Product.BACKPACK);

        Assert.assertEquals(inventory.header().cartCount(), 1);
    }

    @Test(description = "Adding three products shows 3 on the cart badge")
    @Severity(SeverityLevel.CRITICAL)
    public void addingSeveralProductsUpdatesTheBadge() {
        inventory.addToCart(Product.BACKPACK, Product.BIKE_LIGHT, Product.BOLT_TSHIRT);

        Assert.assertEquals(inventory.header().cartCount(), 3);
    }

    @Test(description = "Removing a product lowers the cart badge")
    @Severity(SeverityLevel.CRITICAL)
    public void removingAProductUpdatesTheBadge() {
        inventory.addToCart(Product.BACKPACK, Product.BIKE_LIGHT, Product.BOLT_TSHIRT)
                .removeFromCart(Product.BACKPACK);

        Assert.assertEquals(inventory.header().cartCount(), 2);
    }

    @Test(dataProvider = "sortOptions", description = "The sort dropdown orders the products")
    @Severity(SeverityLevel.MINOR)
    public void sortingOrdersTheProducts(SortOption option) {
        inventory.sortBy(option);

        if (option.byPrice()) {
            assertSorted(inventory.productPrices(), option.ascending());
        } else {
            assertSorted(inventory.productNames(), option.ascending());
        }
    }

    @Test(description = "The cart icon opens the cart")
    @Severity(SeverityLevel.MINOR)
    public void cartIconOpensTheCart() {
        inventory.header().openCart();

        assertOn(Route.CART);
    }

    private static <T extends Comparable<T>> void assertSorted(List<T> values, boolean ascending) {
        for (int i = 1; i < values.size(); i++) {
            int order = values.get(i - 1).compareTo(values.get(i));
            Assert.assertTrue(ascending ? order <= 0 : order >= 0,
                    "List is not sorted " + (ascending ? "ascending" : "descending") + ": " + values);
        }
    }
}
