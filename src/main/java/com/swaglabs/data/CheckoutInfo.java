package com.swaglabs.data;

import java.util.Objects;

/** Immutable customer details for checkout step one. Use the with* methods to build invalid variants. */
public final class CheckoutInfo {

    private final String firstName;
    private final String lastName;
    private final String postalCode;

    public CheckoutInfo(String firstName, String lastName, String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
    }

    public static CheckoutInfo sample() {
        return new CheckoutInfo("Jane", "Tester", "90210");
    }

    public CheckoutInfo withFirstName(String value) {
        return new CheckoutInfo(value, lastName, postalCode);
    }

    public CheckoutInfo withLastName(String value) {
        return new CheckoutInfo(firstName, value, postalCode);
    }

    public CheckoutInfo withPostalCode(String value) {
        return new CheckoutInfo(firstName, lastName, value);
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String postalCode() {
        return postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckoutInfo)) {
            return false;
        }
        CheckoutInfo other = (CheckoutInfo) o;
        return Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName)
                && Objects.equals(postalCode, other.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, postalCode);
    }

    @Override
    public String toString() {
        return "CheckoutInfo{first='" + firstName + "', last='" + lastName + "', postal='" + postalCode + "'}";
    }
}
