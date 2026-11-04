package com.swaglabs.tests.checkout;

import com.swaglabs.config.Route;
import com.swaglabs.data.CheckoutInfo;
import com.swaglabs.data.Product;
import com.swaglabs.data.UserAccount;
import com.swaglabs.flows.CheckoutFlow;
import com.swaglabs.pages.CheckoutInfoPage;
import com.swaglabs.support.SignedInTest;
import com.swaglabs.support.UserProviders;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@Epic("Checkout")
@Feature("Step one: customer information")
public class CheckoutInfoTests extends SignedInTest {

    private CheckoutInfoPage infoPage;

    @Factory(dataProvider = "shoppers", dataProviderClass = UserProviders.class)
    public CheckoutInfoTests(UserAccount account) {
        super(account);
    }

    @DataProvider(name = "incompleteInfo")
    public Object[][] incompleteInfo() {
        CheckoutInfo complete = CheckoutInfo.sample();
        return new Object[][]{
                {complete.withFirstName(""), "First Name is required"},
                {complete.withLastName(""), "Last Name is required"},
                {complete.withPostalCode(""), "Postal Code is required"}
        };
    }

    @BeforeMethod(alwaysRun = true)
    public void openCustomerInfo() {
        infoPage = CheckoutFlow.toCustomerInfo(inventory, Product.BACKPACK);
    }

    @Test(description = "Valid details are accepted and lead to the order overview")
    @Severity(SeverityLevel.NORMAL)
    public void validDetailsLeadToTheOverview() {
        CheckoutInfo info = CheckoutInfo.sample();
        infoPage.fill(info);

        Assert.assertEquals(infoPage.enteredInfo(), info, "The fields should show what was typed");
        infoPage.submit();
        assertOn(Route.CHECKOUT_OVERVIEW);
    }

    @Test(dataProvider = "incompleteInfo", description = "A missing field is reported")
    @Severity(SeverityLevel.NORMAL)
    public void missingFieldsAreReported(CheckoutInfo info, String expectedText) {
        String message = infoPage.fill(info).submitExpectingError().errorMessage();

        Assert.assertTrue(message.contains(expectedText),
                "Expected the error to contain '" + expectedText + "' but it was: " + message);
        assertOn(Route.CHECKOUT_INFO);
    }

    @Test(description = "Cancel goes back to the cart")
    @Severity(SeverityLevel.TRIVIAL)
    public void cancelReturnsToTheCart() {
        infoPage.cancel();

        assertOn(Route.CART);
    }
}
