package com.swaglabs.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Supported browsers and how to start each one. */
public enum Browser {

    CHROME {
        @Override
        WebDriver launch(boolean headless) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(headless ? "--headless=new" : "--start-maximized");
            options.addArguments("--window-size=1920,1080", "--disable-notifications");
            options.setExperimentalOption("prefs", quietProfilePrefs());
            return new ChromeDriver(options);
        }
    },

    EDGE {
        @Override
        WebDriver launch(boolean headless) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments(headless ? "--headless=new" : "--start-maximized");
            options.addArguments("--window-size=1920,1080", "--disable-notifications");
            options.setExperimentalOption("prefs", quietProfilePrefs());
            return new EdgeDriver(options);
        }
    },

    FIREFOX {
        @Override
        WebDriver launch(boolean headless) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            options.addArguments("--width=1920", "--height=1080");
            return new FirefoxDriver(options);
        }
    },

    SAFARI {
        @Override
        WebDriver launch(boolean headless) {
            return new SafariDriver();
        }
    };

    abstract WebDriver launch(boolean headless);

    public static Browser from(String name) {
        try {
            return valueOf(name.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported browser '" + name + "'. Use chrome, edge, firefox or safari.", e);
        }
    }

    /** Stops the "change your password" / leak-detection popups that cover the page in Chromium browsers. */
    private static Map<String, Object> quietProfilePrefs() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        return prefs;
    }
}
