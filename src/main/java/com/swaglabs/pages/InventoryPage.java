package com.swaglabs.pages;

import com.swaglabs.components.Header;
import com.swaglabs.config.Route;
import com.swaglabs.data.Product;
import com.swaglabs.data.SortOption;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** The product list page. */
public class InventoryPage extends BasePage {

    private static final By ITEM_NAMES = By.className("inventory_item_name");
    private static final By ITEM_PRICES = By.className("inventory_item_price");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.INVENTORY);
        visible(ITEM_NAMES);
    }

    private static By addButton(Product product) {
        return By.id("add-to-cart-" + product.slug());
    }

    private static By removeButton(Product product) {
        return By.id("remove-" + product.slug());
    }

    private static By image(Product product) {
        return By.cssSelector("img[alt=\"" + product.title() + "\"]");
    }

    public Header header() {
        return new Header(driver);
    }

    @Step("Add to cart: {products}")
    public InventoryPage addToCart(Product... products) {
        for (Product product : products) {
            click(addButton(product));
        }
        return this;
    }

    @Step("Remove from cart: {product}")
    public InventoryPage removeFromCart(Product product) {
        click(removeButton(product));
        return this;
    }

    @Step("Open details of {product}")
    public ProductDetailsPage open(Product product) {
        click(By.linkText(product.title()));
        return new ProductDetailsPage(driver);
    }

    @Step("Sort by {option}")
    public InventoryPage sortBy(SortOption option) {
        new Select(visible(SORT_DROPDOWN)).selectByValue(option.value());
        return this;
    }

    public List<String> productNames() {
        return textsOf(ITEM_NAMES);
    }

    public List<BigDecimal> productPrices() {
        return textsOf(ITEM_PRICES).stream()
                .map(text -> new BigDecimal(text.replace("$", "")))
                .collect(Collectors.toList());
    }

    /** All catalogue products are listed, each with a visible picture. */
    @Step("Check that every catalogue product is listed")
    public boolean listsEveryProduct() {
        List<String> names = productNames();
        boolean allListed = names.size() == Product.values().length
                && Arrays.stream(Product.values()).allMatch(p -> names.contains(p.title()));
        return allListed && Arrays.stream(Product.values()).allMatch(p -> visible(image(p)).isDisplayed());
    }
}
