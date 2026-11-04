package com.swaglabs.data;

/** Options of the sort dropdown on the inventory page. */
public enum SortOption {
    NAME_A_TO_Z("az", false, true),
    NAME_Z_TO_A("za", false, false),
    PRICE_LOW_TO_HIGH("lohi", true, true),
    PRICE_HIGH_TO_LOW("hilo", true, false);

    private final String value;
    private final boolean byPrice;
    private final boolean ascending;

    SortOption(String value, boolean byPrice, boolean ascending) {
        this.value = value;
        this.byPrice = byPrice;
        this.ascending = ascending;
    }

    public String value() {
        return value;
    }

    public boolean byPrice() {
        return byPrice;
    }

    public boolean ascending() {
        return ascending;
    }
}
