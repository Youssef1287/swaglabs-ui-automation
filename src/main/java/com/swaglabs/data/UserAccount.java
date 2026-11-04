package com.swaglabs.data;

/** The demo accounts published by saucedemo.com (they all share one password, see Settings). */
public enum UserAccount {
    STANDARD("standard_user"),
    LOCKED_OUT("locked_out_user"),
    PERFORMANCE_GLITCH("performance_glitch_user");

    private final String username;

    UserAccount(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }
}
