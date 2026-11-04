package com.swaglabs.components;

import com.swaglabs.pages.BasePage;
import com.swaglabs.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

/** The burger menu. */
public class SideMenu extends BasePage {

    private static final By OPEN_BUTTON = By.id("react-burger-menu-btn");
    private static final By ALL_ITEMS = By.id("inventory_sidebar_link");
    private static final By ABOUT = By.id("about_sidebar_link");
    private static final By LOGOUT = By.id("logout_sidebar_link");
    private static final By RESET_STATE = By.id("reset_sidebar_link");

    private static final List<By> EVERY_LINK = Arrays.asList(ALL_ITEMS, ABOUT, LOGOUT, RESET_STATE);

    public SideMenu(WebDriver driver) {
        super(driver);
    }

    public SideMenu open() {
        click(OPEN_BUTTON);
        visible(LOGOUT);
        return this;
    }

    public boolean showsEveryLink() {
        open();
        return EVERY_LINK.stream().allMatch(link -> visible(link).isDisplayed());
    }

    public LoginPage logout() {
        open();
        click(LOGOUT);
        return new LoginPage(driver);
    }
}
