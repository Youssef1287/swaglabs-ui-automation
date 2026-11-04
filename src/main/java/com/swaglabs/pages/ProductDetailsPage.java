package com.swaglabs.pages;

import com.swaglabs.components.Header;
import com.swaglabs.config.Route;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private static final By NAME = By.className("inventory_details_name");
    private static final By DESCRIPTION = By.className("inventory_details_desc");
    private static final By ADD_TO_CART = By.id("add-to-cart");
    private static final By REMOVE = By.id("remove");
    private static final By BACK = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.PRODUCT_DETAILS);
    }

    public Header header() {
        return new Header(driver);
    }

    public String title() {
        return textOf(NAME);
    }

    public boolean showsDescription() {
        return visible(DESCRIPTION).isDisplayed();
    }

    @Step("Add the product to the cart from its details page")
    public ProductDetailsPage addToCart() {
        click(ADD_TO_CART);
        return this;
    }

    @Step("Remove the product from the cart from its details page")
    public ProductDetailsPage removeFromCart() {
        click(REMOVE);
        return this;
    }

    @Step("Go back to the product list")
    public InventoryPage backToProducts() {
        click(BACK);
        return new InventoryPage(driver);
    }
}
