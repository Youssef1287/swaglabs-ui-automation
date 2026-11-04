package com.swaglabs.listeners;

import com.swaglabs.config.Settings;
import com.swaglabs.core.DriverManager;
import com.swaglabs.utils.AllureReport;
import com.swaglabs.utils.Evidence;
import com.swaglabs.utils.VideoConverter;
import com.swaglabs.utils.VideoRecorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.util.Arrays;

/**
 * Logs every test, attaches a screenshot (and the video, if enabled) when a test fails,
 * and optionally builds the Allure report once the suite is done.
 * Registered in testng.xml.
 */
public class ResultListener implements ITestListener, IInvokedMethodListener, ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(ResultListener.class);

    // ---- per-method hooks: evidence must be attached before the Allure test case is closed ----

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        // Start with the first @BeforeMethod (browser start-up, sign-in), not only with the test
        // method itself, so the video shows the whole scenario. start() ignores repeated calls.
        if (Settings.recordVideo() && isRecordingStart(method)) {
            VideoRecorder.start();
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        boolean failedSetup = isSetup(method) && result.getStatus() == ITestResult.FAILURE;
        if (!method.isTestMethod() && !failedSetup) {
            return;
        }
        // A failed @BeforeMethod skips the test method, so its recording must be closed here.
        boolean failed = result.getStatus() == ITestResult.FAILURE;
        String name = evidenceName(result);

        if (failed) {
            Evidence.attachScreenshot(DriverManager.current(), "Failure - " + name);
        }
        if (Settings.recordVideo()) {
            File video = VideoRecorder.stop(name);
            if (failed) {
                Evidence.attachVideo(VideoConverter.toMp4(video), "Video - " + name);
            } else {
                VideoRecorder.discard(video);
            }
        }
    }

    private static boolean isSetup(IInvokedMethod method) {
        return method.isConfigurationMethod() && method.getTestMethod().isBeforeMethodConfiguration();
    }

    private static boolean isRecordingStart(IInvokedMethod method) {
        return method.isTestMethod() || isSetup(method);
    }

    /** Class, account (for @Factory tests), method and data-provider row, so every run gets its own name. */
    private static String evidenceName(ITestResult result) {
        String owner = result.getTestName() != null
                ? result.getTestName()
                : result.getTestClass().getRealClass().getSimpleName();
        String name = owner + "." + result.getMethod().getMethodName();
        Object[] parameters = result.getParameters();
        return parameters == null || parameters.length == 0 ? name : name + Arrays.toString(parameters);
    }

    // ---- logging ----

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("START   {}", label(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("PASSED  {}", label(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable cause = result.getThrowable();
        LOG.error("FAILED  {} -> {}", label(result), cause == null ? "unknown cause" : cause.getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIPPED {}", label(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("'{}' finished: {} passed, {} failed, {} skipped", context.getName(),
                context.getPassedTests().size(), context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    // ---- suite level ----

    @Override
    public void onFinish(ISuite suite) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        for (ISuiteResult suiteResult : suite.getResults().values()) {
            ITestContext context = suiteResult.getTestContext();
            passed += context.getPassedTests().size();
            failed += context.getFailedTests().size();
            skipped += context.getSkippedTests().size();
        }
        int total = passed + failed + skipped;
        LOG.info("Suite '{}': {} tests, {} passed, {} failed, {} skipped ({}% passed)",
                suite.getName(), total, passed, failed, skipped,
                total == 0 ? 0 : Math.round(passed * 100.0 / total));

        if (Settings.autoReport() && AllureReport.generate()) {
            AllureReport.open();
        }
    }

    private static String label(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }
}
