package com.shop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the Cart Page.
 * Handles items displayed in the cart and navigation to Checkout.
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By cartContainer = By.id("cart_contents_container");   // page-level container
    private final By cartItem      = By.className("cart_item");          // each item row
    private final By itemPrice     = By.className("inventory_item_price");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ❌ OLD (bad): wait for at least one cart_item
        // wait.until(ExpectedConditions.visibilityOfElementLocated(cartItem));

        // ✅ NEW (better): wait for the cart page container
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartContainer));
        System.out.println("DEBUG: CartPage loaded, URL = " + driver.getCurrentUrl());
    }

    /**
     * Returns how many items are listed in the cart.
     */
    public int getNumberOfItems() {
        List<WebElement> items = driver.findElements(cartItem);
        int count = items.size();
        System.out.println("DEBUG: Number of cart items found = " + count);
        return count;
    }

    /**
     * Optional: calculate the total price of all items in the cart.
     */
    public double calculateCartTotal() {
        List<WebElement> prices = driver.findElements(itemPrice);
        double total = 0.0;
        for (WebElement price : prices) {
            String priceText = price.getText().replace("$", "").trim();
            try {
                total += Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                System.err.println(
                        "Error parsing price to double on Cart Page: " + priceText
                );
            }
        }
        return total;
    }

    /**
     * Clicks the Checkout button to proceed to shipping information.
     */
    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }
}
