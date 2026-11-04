package com.swaglabs.config;

/** Every page of the app we test, with its path relative to the base URL. */
public enum Route {
    LOGIN(""),
    INVENTORY("inventory.html"),
    PRODUCT_DETAILS("inventory-item.html"),
    CART("cart.html"),
    CHECKOUT_INFO("checkout-step-one.html"),
    CHECKOUT_OVERVIEW("checkout-step-two.html"),
    CHECKOUT_COMPLETE("checkout-complete.html");

    private final String path;

    Route(String path) {
        this.path = path;
    }

    public String url() {
        return Settings.baseUrl() + path;
    }

    /** True when the given browser URL is this page (query string and fragment are ignored). */
    public boolean matches(String currentUrl) {
        String withoutQuery = currentUrl.split("[?#]", 2)[0];
        return withoutQuery.equals(url());
    }
}
