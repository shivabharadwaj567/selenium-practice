package com.shop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inventoryContainer = By.id("inventory_container");
    private final By addToCartButtons   = By.cssSelector("button.btn_inventory");
    private final By cartIcon           = By.cssSelector("a.shopping_cart_link");
    private final By cartBadge          = By.cssSelector("span.shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        // basic sanity check
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryContainer));
    }

    public boolean isLoaded() {
        return !driver.findElements(inventoryContainer).isEmpty();
    }

    public void addFirstNProductsToCart(int count) {
        List<WebElement> buttons = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons)
        );

        int clicks = Math.min(count, buttons.size());
        for (int i = 0; i < clicks; i++) {
            buttons.get(i).click();

            // For learning/debugging only – NOT used in assertions anymore
            String badge = getCartBadgeCount();
            System.out.println("Clicked add-to-cart on product #" + (i + 1) +
                    ", badge now = " + badge);
        }
    }

    /**
     * Returns the current cart badge value if it exists, otherwise "0".
     * This is best-effort and used only for logging, not for test pass/fail.
     */
    public String getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        if (!badges.isEmpty()) {
            return badges.get(0).getText().trim();
        }
        return "0";
    }

    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        return new CartPage(driver);
    }
}
