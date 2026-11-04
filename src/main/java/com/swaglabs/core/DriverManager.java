package com.swaglabs.core;

import com.swaglabs.config.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/** Owns the WebDriver of the current thread, so tests can later run in parallel. */
public final class DriverManager {

    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> CURRENT = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver start() {
        if (CURRENT.get() != null) {
            stop();
        }
        Browser browser = Browser.from(Settings.browser());
        boolean headless = Settings.headless();
        LOG.info("Starting {} (headless={})", browser, headless);
        WebDriver driver = browser.launch(headless);
        CURRENT.set(driver);
        return driver;
    }

    /** The driver of the current thread, or null when none is running. */
    public static WebDriver current() {
        return CURRENT.get();
    }

    public static void stop() {
        WebDriver driver = CURRENT.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } catch (RuntimeException e) {
            LOG.warn("Browser did not close cleanly: {}", e.getMessage());
        } finally {
            CURRENT.remove();
        }
    }
}
