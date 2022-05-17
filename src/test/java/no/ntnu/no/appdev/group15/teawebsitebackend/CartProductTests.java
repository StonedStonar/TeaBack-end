package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.CartProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.ProductDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the cart product class.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CartProductTests {

    private CartProduct cartProduct;

    private int errors;

    private StringBuilder stringBuilder;

    private String illegalPrefix;

    /**
     * Makes an instance of the CartProductTests class.
     */
    public CartProductTests() {
        this.illegalPrefix = makeExceptionString("IllegalArgumentException");
    }

    /**
     * Sets up the test fields.
     */
    @BeforeEach
    private void setUpTests(){
        try {
            Product product = makeProductWithId(1);
            cartProduct = new CartProduct(product, 3, null);
        }catch (IllegalArgumentException exception){
            fail("Expected the test data to be made since the input is valid.");
        }
        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Checks if the tests failed and displays the results.
     */
    @AfterEach
    private void checkIfTestsFailedAndDisplayResult(){
        if(stringBuilder.length() == 0){
            assertTrue(true);
        }else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }


    /**
     * Adds an error with an exception in the title.
     * @param errorPrefix what it should say before the main error.
     * @param error what it should say after the error.
     * @param exception the exception that was not expected.
     */
    private void addErrorWithException(String errorPrefix, String error, Exception exception){
        addError(errorPrefix, error);
        stringBuilder.append(" and not a ").append(exception.getClass().getSimpleName());
    }

    /**
     * Makes an exception into the wanted string.
     * @param exceptionName the name of the exception.
     * @return the full exception string.
     */
    private String makeExceptionString(String exceptionName){
        return "Expected to get a " +  exceptionName + " since";
    }

    /**
     * Adds a new error to the stringbuilder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(error);
        errors++;
    }

    /**
     * Makes a product with a certain id.
     * @param id the id the product should have.
     * @return the product.
     */
    private Product makeProductWithId(long id){
        return new Product(id, "Fjell laks Te", 399.99f, 100, new ProductDetails(), new Company());
    }

    /**
     * Tests if constructor works with invalid input.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid input.")
    public void testIfConstructorWorksWithInvalidInput(){
        Product product = makeProductWithId(2);
        int amount = 2;

        try {
            CartProduct cartProduct = new CartProduct(null, amount, null);
            addError(illegalPrefix, "the product is null");
        }catch (IllegalArgumentException exception){}
        try {
            CartProduct cartProduct = new CartProduct(product, 0, null);
            addError(illegalPrefix, "the amount is zero");
        }catch (IllegalArgumentException exception){}
        try {
            CartProduct cartProduct = new CartProduct(product, -1, null);
            addError(illegalPrefix, "the amount is -1");
        }catch (IllegalArgumentException exception){}
    }

    /**
     * Tests if constructor works with valid input.
     */
    @Test
    @DisplayName("Tests if constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        try{
            CartProduct cartProduct = new CartProduct(makeProductWithId(1), 1, null);
        }catch (IllegalArgumentException exception){
            addErrorWithException("Expected the cart product to be made since the input is valid", "", exception);
        }
    }

    /**
     * Tests if addAmount works with invalid input.
     */
    @Test
    @DisplayName("Tests if addAmount works with invalid input.")
    public void testIfAddAmountWorksWithInvalidInput(){
        try {
            cartProduct.addAmount(0);
            addError(illegalPrefix, "the input is zero");
        }catch (IllegalArgumentException exception){}
        try {
            cartProduct.addAmount(-1);
            addError(illegalPrefix, "the input is negative");
        }catch (IllegalArgumentException exception){}
    }

    /**
     * Tests if addAmount works with valid input.
     */
    @Test
    @DisplayName("Tests if addAmount works with valid input.")
    public void testIfAddAmountWorksWithValidInput(){
        try {
            cartProduct.addAmount(2);
        }catch (IllegalArgumentException exception){
            addErrorWithException("Expected the ", "amount to be added since the input is valid", exception);
        }
    }

    /**
     * Tests if removeAmount works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeAmount works with invalid input.")
    public void testIfRemoveAmountWorksWithInvalidInput(){
        try {
            cartProduct.removeAmount(0);
            addError(illegalPrefix, "the input amount is zero");
        }catch (IllegalArgumentException exception){}
        try {
            cartProduct.removeAmount(-1);
            addError(illegalPrefix, "the input is negative");
        }catch (IllegalArgumentException exception){}
    }

    /**
     * Tests if removeAmount works with valid input.
     */
    @Test
    @DisplayName("Tests if removeAmount works with valid input.")
    public void testIfRemoveAmountWorksWithValidInput(){
        try {
            cartProduct.removeAmount(2);
        }catch (IllegalArgumentException exception){
            addErrorWithException("Expected the", "amount to be removed since the input is valid", exception);
        }
    }
}
