package com.swaglabs.flows;

import com.swaglabs.data.CheckoutInfo;
import com.swaglabs.data.Product;
import com.swaglabs.pages.CheckoutInfoPage;
import com.swaglabs.pages.CheckoutOverviewPage;
import com.swaglabs.pages.InventoryPage;
import com.swaglabs.pages.OrderCompletePage;

/** Reusable journeys through the real UI, so tests can start from any point of checkout. */
public final class CheckoutFlow {

    private CheckoutFlow() {
    }

    public static CheckoutInfoPage toCustomerInfo(InventoryPage inventory, Product... items) {
        return inventory.addToCart(items).header().openCart().checkout();
    }

    public static CheckoutOverviewPage toOverview(InventoryPage inventory, Product... items) {
        return toCustomerInfo(inventory, items).fill(CheckoutInfo.sample()).submit();
    }

    public static OrderCompletePage placeOrder(InventoryPage inventory, Product... items) {
        return toOverview(inventory, items).finish();
    }
}
