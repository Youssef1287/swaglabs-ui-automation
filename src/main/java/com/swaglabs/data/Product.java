package com.swaglabs.data;

import java.math.BigDecimal;

/** The six products in the catalogue. The element ids on the page are derived from the title. */
public enum Product {
    BACKPACK("Sauce Labs Backpack", "29.99"),
    BIKE_LIGHT("Sauce Labs Bike Light", "9.99"),
    BOLT_TSHIRT("Sauce Labs Bolt T-Shirt", "15.99"),
    FLEECE_JACKET("Sauce Labs Fleece Jacket", "49.99"),
    ONESIE("Sauce Labs Onesie", "7.99"),
    RED_TSHIRT("Test.allTheThings() T-Shirt (Red)", "15.99");

    private final String title;
    private final BigDecimal price;

    Product(String title, String price) {
        this.title = title;
        this.price = new BigDecimal(price);
    }

    public String title() {
        return title;
    }

    public BigDecimal price() {
        return price;
    }

    /** "Sauce Labs Backpack" -> "sauce-labs-backpack", used in ids like add-to-cart-sauce-labs-backpack. */
    public String slug() {
        return title.toLowerCase(java.util.Locale.ROOT).replace(' ', '-');
    }
}
