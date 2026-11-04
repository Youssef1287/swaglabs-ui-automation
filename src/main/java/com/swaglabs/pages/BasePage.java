package com.swaglabs.pages;

import com.swaglabs.config.Route;
import com.swaglabs.config.Settings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/** Shared, wait-aware helpers for pages and page components. No implicit waits are used anywhere. */
public abstract class BasePage {

    protected final WebDriver driver;
    private final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Settings.explicitWait());
    }

    protected void waitForRoute(Route route) {
        wait.until(d -> route.matches(d.getCurrentUrl()));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String value) {
        WebElement field = visible(locator);
        field.clear();
        if (value != null && !value.isEmpty()) {
            field.sendKeys(value);
        }
    }

    protected String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    protected String valueOf(By locator) {
        String value = visible(locator).getAttribute("value");
        return value == null ? "" : value;
    }

    protected List<String> textsOf(By locator) {
        return driver.findElements(locator).stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    protected boolean becomesInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
