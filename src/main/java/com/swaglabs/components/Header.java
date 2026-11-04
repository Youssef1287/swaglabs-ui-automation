package com.swaglabs.components;

import com.swaglabs.pages.BasePage;
import com.swaglabs.pages.CartPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/** The top bar shown on every page after login: cart link, cart badge and the side menu button. */
public class Header extends BasePage {

    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By CART_BADGE = By.className("shopping_cart_badge");

    public Header(WebDriver driver) {
        super(driver);
    }

    /** Number shown on the cart badge; 0 when the badge is not displayed. */
    public int cartCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
    }

    public boolean isCartEmpty() {
        return becomesInvisible(CART_BADGE);
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public SideMenu sideMenu() {
        return new SideMenu(driver);
    }
}
