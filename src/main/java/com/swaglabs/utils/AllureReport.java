package com.swaglabs.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Thin wrapper around the Allure command line tool (must be installed separately). */
public final class AllureReport {

    private static final Logger LOG = LogManager.getLogger(AllureReport.class);
    private static final Path RESULTS = Paths.get("target", "allure-results");
    private static final Path REPORT = Paths.get("target", "allure-report");

    private AllureReport() {
    }

    public static boolean generate() {
        if (!Files.isDirectory(RESULTS)) {
            LOG.warn("No Allure results found in {}", RESULTS);
            return false;
        }
        try {
            Process process = command("generate", RESULTS.toString(), "-o", REPORT.toString(), "--clean")
                    .inheritIO().start();
            boolean finished = process.waitFor(2, TimeUnit.MINUTES);
            boolean ok = finished && process.exitValue() == 0;
            LOG.info(ok ? "Allure report generated at {}" : "Allure report generation failed ({})", REPORT);
            return ok;
        } catch (IOException e) {
            LOG.warn("Allure CLI not available ({}). Install it or run: mvn allure:report", e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /** Starts a local web server showing the generated report; returns immediately. */
    public static void open() {
        try {
            command("open", REPORT.toString()).inheritIO().start();
        } catch (IOException e) {
            LOG.warn("Could not open the report: {}", e.getMessage());
        }
    }

    private static ProcessBuilder command(String... allureArgs) {
        List<String> cmd = new ArrayList<>();
        if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
            cmd.addAll(Arrays.asList("cmd", "/c", "allure"));
        } else {
            cmd.add("allure");
        }
        cmd.addAll(Arrays.asList(allureArgs));
        return new ProcessBuilder(cmd);
    }
}
