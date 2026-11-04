package com.swaglabs.pages;

import com.swaglabs.components.Header;
import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private static final By ITEM_NAMES = By.className("inventory_item_name");
    private static final By CHECKOUT = By.id("checkout");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.CART);
    }

    public Header header() {
        return new Header(driver);
    }

    public List<String> itemNames() {
        return textsOf(ITEM_NAMES);
    }

    @Step("Remove {product} from the cart")
    public CartPage remove(Product product) {
        click(By.id("remove-" + product.slug()));
        return this;
    }

    @Step("Continue shopping")
    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return new InventoryPage(driver);
    }

    @Step("Proceed to checkout")
    public CheckoutInfoPage checkout() {
        click(CHECKOUT);
        return new CheckoutInfoPage(driver);
    }
}
