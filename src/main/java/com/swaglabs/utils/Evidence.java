package com.swaglabs.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class Evidence {

    private static final Logger LOG = LogManager.getLogger(Evidence.class);

    private Evidence() {
    }

    public static void attachScreenshot(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            byte[] png = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.attachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(png),
                    AttachmentOptions.withFileExtension(".png")
            );

        } catch (RuntimeException e) {
            LOG.warn("Could not take screenshot '{}': {}", name, e.getMessage());
        }
    }

    public static void attachVideo(File video, String name) {
        if (video == null || !video.isFile()) {
            return;
        }

        boolean mp4 = video.getName().toLowerCase().endsWith(".mp4");

        String mediaType = mp4 ? "video/mp4" : "video/avi";
        String extension = mp4 ? ".mp4" : ".avi";

        try (FileInputStream in = new FileInputStream(video)) {

            Allure.attachment(
                    name,
                    mediaType,
                    in,
                    AttachmentOptions.withFileExtension(extension)
            );

        } catch (IOException | RuntimeException e) {
            LOG.warn("Could not attach video '{}': {}", name, e.getMessage());
        }
    }
}