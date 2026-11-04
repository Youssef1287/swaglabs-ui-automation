package com.swaglabs.pages;

import com.swaglabs.config.Route;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

/** Checkout step two: order summary. */
public class CheckoutOverviewPage extends BasePage {

    private static final By ITEM_TOTAL = By.className("summary_subtotal_label");
    private static final By FINISH = By.id("finish");
    private static final By CANCEL = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.CHECKOUT_OVERVIEW);
    }

    /** The "Item total: $xx.xx" line as a number. */
    public BigDecimal itemTotal() {
        String digitsOnly = textOf(ITEM_TOTAL).replaceAll("[^0-9.]", "");
        return new BigDecimal(digitsOnly);
    }

    @Step("Finish the order")
    public OrderCompletePage finish() {
        click(FINISH);
        return new OrderCompletePage(driver);
    }

    @Step("Cancel from the order overview")
    public InventoryPage cancel() {
        click(CANCEL);
        return new InventoryPage(driver);
    }
}
