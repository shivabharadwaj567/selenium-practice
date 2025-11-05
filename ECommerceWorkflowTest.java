package com.shop;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ECommerceWorkflowTest extends BaseTest {

    @Test(description = "Verify a standard user can complete a full purchase flow on SauceDemo")
    public void testFullECommerceFlow() {

        // 1. LOGIN
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        test.info("Opened login page");
        demoPause(1000); // just for you to see it

        InventoryPage inventoryPage = loginPage.loginAs("standard_user", "secret_sauce");
        test.info("Logged in as standard_user");
        demoPause(1000);

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load correctly");
        test.info("Inventory page loaded");

        // 2. ADD ITEMS TO CART (just 1, keep it simple and reliable)
        inventoryPage.addFirstNProductsToCart(1);
        demoPause(1000);

        String badgeCount = inventoryPage.getCartBadgeCount();
        test.info("Cart badge (if any) after adding items = '" + badgeCount + "'");

        // 3. GO TO CART
        CartPage cartPage = inventoryPage.goToCart();
        demoPause(1000);

        int itemsInCart = cartPage.getNumberOfItems();
        // Expect exactly 1, because we asked to add 1
        Assert.assertEquals(itemsInCart, 1,
                "Cart page does not show 1 item as expected");
        test.info("Cart shows " + itemsInCart + " items");

        // 4. CHECKOUT INFO
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        demoPause(1000);

        checkoutPage.fillShippingInformation("John", "Tester", "12345");
        test.info("Filled in checkout details");
        demoPause(1000);

        // 5. SUMMARY + FINISH
        CheckoutSummaryPage summaryPage = checkoutPage.clickContinue();
        Assert.assertTrue(summaryPage.isLoaded(), "Checkout summary page not loaded");
        test.info("On checkout summary page");
        demoPause(1000);

        CheckoutCompletePage completePage = summaryPage.clickFinish();
        demoPause(1000);

        Assert.assertTrue(
                completePage.isOrderConfirmed(),
                "Order confirmation message not found"
        );
        test.pass("Purchase completed successfully. Confirmation message verified: "
                + CheckoutCompletePage.EXPECTED_HEADER_TEXT);

        // final pause to see the confirmation page
        demoPause(2000);
    }
}
