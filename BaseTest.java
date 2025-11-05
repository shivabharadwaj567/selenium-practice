package com.shop;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite(alwaysRun = true)
    public void setupReport() {
        ExtentSparkReporter reporter =
                new ExtentSparkReporter("target/extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method method) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Make sure this line is commented so it is NOT headless:
        // options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        String testName = method.getDeclaringClass().getSimpleName()
                + "." + method.getName();
        test = extent.createTest(testName);
        test.info("Starting test: " + testName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.SUCCESS -> test.pass("Test passed");
                case ITestResult.FAILURE -> test.fail(result.getThrowable());
                case ITestResult.SKIP -> {
                    if (result.getThrowable() != null) {
                        test.log(Status.SKIP, result.getThrowable());
                    } else {
                        test.log(Status.SKIP, "Test skipped.");
                    }
                }
                default -> test.info("Test finished with unknown status");
            }
        }

        if (driver != null) {
            driver.quit();
        }

        if (test != null) {
            test.info("Browser closed");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    // 👇 Added this for visual demo pauses
    protected void demoPause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
