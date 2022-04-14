package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.CompanyDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * Positive and negative tests for the company class.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public class CompanyTests {

    private Company company;

    private StringBuilder stringBuilder;
    private String expectedError;
    private int errors;

    @BeforeEach
    public void makeCompanyForTest() {
        try {
            this.company = new Company(1, "SausLeifs Tea", new CompanyDetails("Superior tea", new Address()));
        } catch (IllegalArgumentException ex) {
            fail("Expected the company used for testing to be of valid format.");
        }
        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Adds a new error to the StringBuilder.
     *
     * @param expectedError the message it should say before error.
     * @param error         the error to appear.
     */
    private void addError(String expectedError, String error) {
        stringBuilder.append("\n").append(expectedError).append(error);
        errors++;
    }

    /**
     * Checks if the tests failed and display the results.
     */
    private void checkIfTestsFailedAndDisplayResult() {
        if (stringBuilder.length() == 0) {
            assertTrue(true);
        } else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }

    /**
     * Tests that the constructor works with valid input.
     * This is a positive test and should only cast exception if the test fails.
     */
    @Test
    @DisplayName("Test if the constructor works with valid input.")
    public void testIfTheConstructorWorksWithValidInput() {
        try {
            Company company = new Company(1, "Troll Tea AS", new CompanyDetails("Tea from the nature of norway", new Address()));
        } catch (IllegalArgumentException ex) {
            addError(expectedError, "You have used invalid input/format on a positive test for company constructor.");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if the constructor works with invalid use of input.
     * This test checks all field parameters in one.
     */
    @Test
    @DisplayName("Tests if the constructor works with invalid use of input.")
    public void testIfTheConstructorWorksWithInvalidInput() {
        try {
            Company company = new Company(0, "Troll Tea AS", new CompanyDetails("Tea from the nature of norway", new Address()));
            addError(expectedError, " The CompanyID is zero");
        } catch (IllegalArgumentException ex) {}
        try {
            Company company = new Company(-5, "Troll Tea AS", new CompanyDetails("Tea from the nature of norway", new Address()));
            addError(expectedError, " The CompanyID is negative");
        } catch (IllegalArgumentException ex) {}
        try {
            Company company = new Company(3, "", new CompanyDetails("Tea from the nature of norway", new Address()));
            addError(expectedError, " The companyName is an empty String");
        } catch (IllegalArgumentException ex) {}
        try {
            Company company = new Company(3, null, new CompanyDetails("Tea from the nature of norway", new Address()));
            addError(expectedError, " The companyName is null");
        } catch (IllegalArgumentException ex) {}
        try {
            Company company = new Company(3, "Troll Tea AS", null);
            addError(expectedError, " The companyName is null");
        } catch (IllegalArgumentException ex) {}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if SetCompanyName work with valid input usages.
     */
    @Test
    @DisplayName("Test if setCompanyName work with valid input.")
    public void testIfSetCompanyNameWorkWithValidInput() {
        try {
            String companyName = "Troll Tea AS";
            company.setCompanyName(companyName);

            assertEquals(companyName, company.getCompanyName());
        } catch (IllegalArgumentException ex) {
            fail("Expected the companyName to be set since the input is valid.");
        }
    }

    /**
     * Test if setCompanyName work with invalid input.
     * We are testing null and empty string, since that shouldn't work when creating companyName.
     */
    @Test
    @DisplayName("Test if setCompanyName work with invalid input")
    public void testIfSetCompanyNameWorkWithUseOfInvalidInput() {
        String expectedError = "Expected to get an IllegalArgumentException since the input ";
        try {
            company.setCompanyName("");
            addError(expectedError,"is an empty String.");
        } catch (IllegalArgumentException ex) {}
        try {
            company.setCompanyName(null);
            addError(expectedError,"is null.");
        } catch (IllegalArgumentException e) {}

        checkIfTestsFailedAndDisplayResult();
    }
}

