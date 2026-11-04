package com.swaglabs.pages;

import com.swaglabs.components.Header;
import com.swaglabs.config.Route;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderCompletePage extends BasePage {

    private static final By CONFIRMATION = By.className("complete-header");
    private static final By BACK_HOME = By.id("back-to-products");

    public OrderCompletePage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.CHECKOUT_COMPLETE);
    }

    public Header header() {
        return new Header(driver);
    }

    public String confirmation() {
        return textOf(CONFIRMATION);
    }

    @Step("Go back home")
    public InventoryPage backHome() {
        click(BACK_HOME);
        return new InventoryPage(driver);
    }
}
