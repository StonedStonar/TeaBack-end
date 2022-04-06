package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddReviewException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveReviewException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Positive and negative tests for the product class.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public class ProductTests {

    private Product product;
    private Details details;
    private Company company;
    private Review review;

    private StringBuilder stringBuilder;
    private String expectedError;
    private int errors;

    /**
     * Sets up the basic test environment for a product.
     */
    @BeforeEach
    public void setupTestProduct(){
        try{
            details = new TeaDetails();
            company = new Company();
            this.product = makeProduct();
        }catch (IllegalArgumentException | CouldNotAddReviewException exception){
            fail("Expected the product to be made with valid input. ");
        }
        expectedError = "IllegalArgumentException was expected since ";
        this.stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Makes an instance of product.
     * @return a product.
     */
    private Product makeProduct() throws CouldNotAddReviewException {
        Product product = new Product(1, "Fjell laks Te", 399.99f, 100, details, company);
        this.review = makeReview();
        product.addReview(review);
        return product;
    }

    /**
     * Makes an instance of review, so we can use it for testing.
     * @return a review.
     */
    private Review makeReview() {
        return new Review("Cola Tea", "Per Asle", new User(),
                "Cola tea might be the best tea i ever tasted.", LocalDate.now().minusDays(70), 4);
    }

    /**
     * Tests if the constructor works with invalid input.
     * Have set up some expectedError that shows the error that will come when we change a value from
     * invalid to valid since this is negative test.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid input.")
    public void testIfConstructorWorksWithInvalidInput() {
        try {
            Product product = new Product(-4, "Ost", 12.99f, 3, new TeaDetails(), new Company());
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

    /**
     * Tests that the constructor works with valid input.
     * All integer less than zero is invalid and String that's empty or holds null.
     */
    @Test
    @DisplayName("Test if constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput() {
        try {
            Product product = new Product(2, "Saus", 14.99f, 0, new TeaDetails(), new Company());
        } catch (IllegalArgumentException ex) {
            addError(expectedError, " You have used Invalid input on a positive test.");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeAmountOfProduct works with invalid input. (negative test)
     */
    @Test
    @DisplayName("Test if remove amount of product works with invalid input")
    public void testIfRemoveAmountOfProductWorksWithInvalidInput() {
        try {
            product.removeAmountOfProduct(-3);
            addError(expectedError, " the input is negative.");
        } catch (IllegalArgumentException ex) {}
        try {
            product.removeAmountOfProduct(0);
            addError(expectedError, " the input is zero");
        } catch (IllegalArgumentException ex) {}
        try {
            product.removeAmountOfProduct(product.getAmountOfProduct() + 1);
            addError(expectedError, " Sthe input is one larger than the actual amount");
        } catch (IllegalArgumentException ex) {}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if remove amount of product works with valid input.
     */
    @Test
    @DisplayName("Test if remove amountOfProduct works with valid input")
    public void testIfRemoveAmountOfProductWorksWithValidInput() {
        try {
            product.removeAmountOfProduct(1);
        } catch (IllegalArgumentException ex) {
            addError("Expected the amount to be removed since the input 1 is valid.", "");
        }
        try {
            product.removeAmountOfProduct(product.getAmountOfProduct() - 1);
        } catch (IllegalArgumentException ex) {
            addError("Expected the amount to be removed since the input of collection size -1 should be valid", "");
        }
        try {
            product.removeAmountOfProduct(product.getAmountOfProduct());
        } catch (IllegalArgumentException ex) {
            addError("Expected the amount to be removed since the amount in collection is valid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeReview work with valid input.
     */
    @Test
    @DisplayName("Test if removeReview work with valid input")
    public void testIfRemoveReviewWorkWithValidInput() {
        try {
            product.removeReview(review);
        } catch (IllegalArgumentException ex) {
            addError("Expected the review to be right format.", "");
        }catch (CouldNotRemoveReviewException exception) {
            addError("Expected the review to be removed since input is valid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeReview work with invalid input.
     */
    @Test
    @DisplayName("Test if removeReview work with invalid input")
    public void testIfRemoveReviewWorkWithInvalidInput() {
        String removeEx = "the input is null";

        try {
            product.removeReview(null);
            addError(expectedError, removeEx);
        } catch (CouldNotRemoveReviewException ex) {
            addError(expectedError, removeEx + " and should not get a CouldNotRemoveReviewException");
        } catch (IllegalArgumentException ex) {
        }

        String removeException = "CouldNotRemoveReviewException was expected since ";

        try {
            //Skal fjerne et reivew som ikke er der.
            product.removeReview(makeReview());
            //Feilmelding hvis vi ikke får noen exceptions. Vi skal ha CouldNotRemoveReviewException
            addError(removeException, "the review is not in product, trying to remove a review that is not there.");
        } catch (IllegalArgumentException ex) {
            //Vi ønsker IKKE denne. Og dermed gir en error. Den er riktig format og skal dermed ikke være IllegalArgumentException
            addError(removeException, "the review is not in product," +
                    " trying to remove a review that is not there and should not get a IllegalArgumentException.");
        }catch (CouldNotRemoveReviewException exception) {
            //Denne er den vi vil ha. Siden det ikke er noe review av samme boks i produkt.
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if addReview work with valid input.
     */
    @Test
    @DisplayName("Test if addReview work with valid input")
    public void testIfAddReviewWorksWithValidInput() {
        try {
            product.addReview(makeReview());
        } catch (IllegalArgumentException ex) {
            addError("Expected the review to be added since it uses valid format.", "");
        } catch (CouldNotAddReviewException ex) {
            addError("Expected the review to be added", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if addReview work with invalid input.
     */
    @Test
    @DisplayName("Test if addReview work with invalid input")
    public void testIfAddReviewWorksWithInvalidInput() {
        String addException = "the input is null";

        try {
            product.addReview(null);
            addError(expectedError, addException);
        } catch (CouldNotAddReviewException ex) {
            addError(expectedError,  addException + " and should not get a CouldNotAddReviewException");
        } catch (IllegalArgumentException ex) {

        }
        checkIfTestsFailedAndDisplayResult();
    }

    // TODO: User may post the same review twice. Should that be possible?

    /**
     * Tests if setProductName works with valid input.
     * This is a positive test and should result in correct result with valid input.
     */
    @Test
    @DisplayName("Test if setProduct works with valid input")
    public void testIfSetProductNameWorksWithValidInput() {
        try {
            // Name to be set.
            String productName = "KremOst";
            // sets the new name.
            product.setProductName(productName);
            //If we do not get an exception we check that the name was set.
            assertEquals(productName, product.getProductName());
        } catch (IllegalArgumentException e) {
            //Exception to come if the steps over included invalid input.
            fail("Expected the product name to be set since the input was valid. ");
        }
    }

    /**
     * Tests if setProductName works with invalid input.
     * Negative test. Test result should be wrong input.
     */
    @Test
    @DisplayName("Test if setProductName works with invalid input")
    public void testIfSetProductNameWorksWithInvalidInput() {
        try {
            product.setProductName("");
            fail("Expected to get an IllegalArgumentException since the input should be empty string.");
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        try {
            product.setProductName(null);
            fail("Expected to get an IllegalArgumentException Since the input should be null.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        }
    }

    /**
     * Tests if setPrice works with valid input.
     * Positive test. Test result where input is correct.
     */
    @Test
    @DisplayName("Test if setPrice works with valid input")
    public void testIfSetPriceWorksWithValidInput() {
        try {
            float price = 2300.99f;
            product.setPrice(price);
            assertEquals(price, product.getPrice());
        } catch (IllegalArgumentException e) {
            fail("Expected the product price to be of valid input. ");
        }
    }

    /**
     * Tests if setPrice works with invalid input. every number less than 0.
     * Negative test. Test result should be valid with wrong input.
     */
    @Test
    @DisplayName("Test if setPrice works with invalid input")
    public void testIfSetPriceWorksWithInvalidInput() {
        try {
            product.setPrice(-0.99f);
            fail("Expected to get an IllegalArgumentException since the input should be negative integer. ");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Adds a new error to the StringBuilder.
     * @param errorPrefix what it should say before the error.
     * @param expectedError the error to appear.
     */
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
