package com.shop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutSummaryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By summaryContainer = By.id("checkout_summary_container");
    private final By finishButton     = By.id("finish");

    public CheckoutSummaryPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(summaryContainer));
    }

    public boolean isLoaded() {
        return !driver.findElements(summaryContainer).isEmpty();
    }

    public CheckoutCompletePage clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton))
            .click();
        return new CheckoutCompletePage(driver);
    }
}
