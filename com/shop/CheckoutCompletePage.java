package com.shop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutCompletePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By header = By.cssSelector("h2.complete-header");

    public static final String EXPECTED_HEADER_TEXT = "Thank you for your order!";

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOrderConfirmed() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(header))
                          .getText().trim();
        return EXPECTED_HEADER_TEXT.equals(text);
    }
}
