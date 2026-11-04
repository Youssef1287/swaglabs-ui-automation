package com.swaglabs.pages;

import com.swaglabs.config.Route;
import com.swaglabs.data.CheckoutInfo;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: customer information. */
public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By CANCEL = By.id("cancel");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
        waitForRoute(Route.CHECKOUT_INFO);
    }

    @Step("Fill customer information: {info}")
    public CheckoutInfoPage fill(CheckoutInfo info) {
        type(FIRST_NAME, info.firstName());
        type(LAST_NAME, info.lastName());
        type(POSTAL_CODE, info.postalCode());
        return this;
    }

    /** What the three fields currently contain. */
    public CheckoutInfo enteredInfo() {
        return new CheckoutInfo(valueOf(FIRST_NAME), valueOf(LAST_NAME), valueOf(POSTAL_CODE));
    }

    /** Continue with valid data: moves on to the order overview. */
    @Step("Continue to the order overview")
    public CheckoutOverviewPage submit() {
        click(CONTINUE);
        return new CheckoutOverviewPage(driver);
    }

    /** Continue with incomplete data: stays on this page and shows a validation error. */
    @Step("Continue with incomplete data")
    public CheckoutInfoPage submitExpectingError() {
        click(CONTINUE);
        return this;
    }

    public String errorMessage() {
        return textOf(ERROR);
    }

    @Step("Cancel checkout")
    public CartPage cancel() {
        click(CANCEL);
        return new CartPage(driver);
    }
}
