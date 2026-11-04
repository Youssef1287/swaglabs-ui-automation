package com.swaglabs.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Single entry point for run configuration.
 * Resolution order: JVM -D property, then settings/&lt;env&gt;.properties, then settings/default.properties.
 */
public final class Settings {

    private static final Properties FILE_VALUES = new Properties();

    static {
        load("settings/default.properties");
        String env = System.getProperty("env");
        if (env != null && !env.isBlank()) {
            load("settings/" + env.trim().toLowerCase() + ".properties");
        }
    }

    private Settings() {
    }

    private static void load(String resource) {
        try (InputStream in = Settings.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Settings file not found on classpath: " + resource);
            }
            FILE_VALUES.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read settings file: " + resource, e);
        }
    }

    private static String value(String key) {
        String found = System.getProperty(key, FILE_VALUES.getProperty(key));
        if (found == null) {
            throw new IllegalStateException("Missing setting: " + key);
        }
        return found.trim();
    }

    public static String baseUrl() {
        String url = value("base.url");
        return url.endsWith("/") ? url : url + "/";
    }

    public static String browser() {
        return value("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(value("headless"));
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(value("wait.seconds")));
    }

    public static boolean recordVideo() {
        return Boolean.parseBoolean(value("record.video"));
    }

    public static boolean autoReport() {
        return Boolean.parseBoolean(value("allure.auto.report"));
    }


    public static String sharedPassword() {
        return value("users.password");
    }
}
