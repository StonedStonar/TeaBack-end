package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory.addError;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ProductTests2 {

    private Product product;

    private StringBuilder stringBuilder;
    private String expectedError;
    private int errors;

    @BeforeEach
    public void setUpForTests() {
        expectedError = "IllegalArgumentException was expected since ";
        this.stringBuilder = new StringBuilder();
        errors = 0;
        this.product = makeProduct();
    }

    /**
     * Makes an instance of product.
     *
     * @return product we made.
     */
    private Product makeProduct() {
        return new Product(3, "KremOst", 29.99f, 20, new TeaDetails(), new Company());
    }

    /**
     * Tests if the constructor works with invalid input.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid input.")
    public void testIfConstructorWorksWithInvalidInput() {
        try {
            Product product = new Product(-3, "Ost", 12.99f, 3, new TeaDetails(), new Company());
            addError(expectedError, "The id is negative");
        } catch (IllegalArgumentException ex) {}
        try {
            Product product = new Product(1, "", 12.99f, 3, new TeaDetails(), new Company());
            addError(expectedError, "The productName is an empty String");
        } catch (IllegalArgumentException ex) {}
        try {
            Product product = new Product(3, null, 12.99f, 3, new TeaDetails(), new Company());
            addError(expectedError, "The product name is null");
        } catch (IllegalArgumentException ex) {}
        try {
            Product product = new Product(3, "Kremost", -2.22f, 3, new TeaDetails(), new Company());
            addError(expectedError, "The price is null");
        } catch (IllegalArgumentException ex) {}
        try {
            Product product = new Product(3, "Kremost", 3.29f, -5, new TeaDetails(), new Company());
            addError(expectedError, "The size is negative");
        } catch (IllegalArgumentException ex) {}
        try {
            Product product = new Product(3, "Kremost", 3.29f, 4, null, new Company());
            addError(expectedError, "Details should be set to null in negative test");
        } catch (IllegalArgumentException ex) {}
        try {
            Product product = new Product(3, "Kremost", 3.29f, 3, new TeaDetails(), null);
            addError(expectedError, "Company should be set to null in negative test");
        } catch (IllegalArgumentException ex) {}
        checkIfTestsFailedAndDisplayResult();
        }

    private void addError(String errorPrefix, String expectedError) {
        stringBuilder.append("\n").append(errorPrefix).append(expectedError);
        errors++;
    }

    /**
     * Checks if the tests failed and displays the results.
     */
    private void checkIfTestsFailedAndDisplayResult() {
        if(stringBuilder.isEmpty()) {
            assertTrue(true);
        } else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }
}
