package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveCartProductException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the cart
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CartTests {


    private int errors;

    private StringBuilder stringBuilder;

    private Cart cart;

    private CartProduct cartProduct;

    private String illegalPrefix;

    private String addPrefix;

    private String removePrefix;

    private String getPrefix;

    /**
     * Makes an instance of the CartTests class.
     */
    public CartTests() {
        illegalPrefix = makeExceptionString("IllegalArgumentException");
        addPrefix = makeExceptionString("CouldNotAddOrderedProductException");
        removePrefix = makeExceptionString("CouldNotRemoveOrderedProductException");
        getPrefix = makeExceptionString("CouldNotGetCartProductException");
    }

    /**
     * Sets up a test cart.
     */
    @BeforeEach
    private void setUpTestData(){
        try {
            List<CartProduct> cartProductList = makeCartProducts();
            cart = new Cart();
            for (CartProduct product : cartProductList) {
                cart.addCartProduct(product);
            }
            cartProduct = cartProductList.get(0);
        } catch (CouldNotAddCartProductException | IllegalArgumentException e) {
            fail("Expected the test data to be made since the input is valid.");
        }
        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Makes a list with the products for the cart.
     * @return the list.
     */
    private List<CartProduct> makeCartProducts(){
        List<CartProduct> cartProductList = new ArrayList<>();
        cartProductList.add(new CartProduct(makeProductWithId(1), 2, null));
        cartProductList.add(new CartProduct(makeProductWithId(2), 5, null));
        cartProductList.add(new CartProduct(makeProductWithId(3), 4, null));
        return cartProductList;
    }

    /**
     * Makes a product with a certain id.
     * @param id the id the product should have.
     * @return the product.
     */
    private Product makeProductWithId(long id){
        return new Product(id, "Fjell laks Te", 399, 100, new ProductDetails(), new Company());
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
     * Adds a new error to the string builder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(error);
        errors++;
    }

    /**
     * Tests if addCartProduct works with invalid input.
     */
    @Test
    @DisplayName("Tests if addCartProduct works with invalid input.")
    public void testIfAddCartProductWorksWithInvalidInput(){
        try {
            cart.addCartProduct(null);
            addError(illegalPrefix, "the input is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotAddCartProductException exception){
            addErrorWithException(illegalPrefix, "the input is null", exception);
        }
        try {
            cart.addCartProduct(cartProduct);
            addError(addPrefix, "the input is already in the cart");
        }catch (IllegalArgumentException exception){
            addErrorWithException(addPrefix, "the input is already in the cart", exception);
        }catch (CouldNotAddCartProductException exception) {}
    }

    /**
     * Tests if addCartProduct works with valid input.
     */
    @Test
    @DisplayName("Tests if addCartProduct works with valid input.")
    public void testIfAddCartProductWorksWithValidInput(){
        try {
            cart.addCartProduct(new CartProduct(makeProductWithId(20), 2, null));
        }catch (IllegalArgumentException | CouldNotAddCartProductException exception){
            addErrorWithException("Expected the", "the product to be added since the input is valid", exception);
        }
    }

    /**
     * Tests if removeCartProduct works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeCartProduct works with invalid input.")
    public void testIfRemoveCartProductWorksWithInvalidInput(){
        try {
            cart.removeCartProduct(null);
            addError(illegalPrefix, "the input is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotRemoveCartProductException exception){
            addErrorWithException(illegalPrefix, "the input is null", exception);
        }
        try {
            cart.removeCartProduct(makeProductWithId(100));
            addError(removePrefix, "the input product is not in the cart");
        }catch (IllegalArgumentException exception){
            addErrorWithException(removePrefix, "the input product is not in the cart", exception);
        }catch (CouldNotRemoveCartProductException exception){

        }
    }

    /**
     * Tests if removeCartProduct works with valid input.
     */
    @Test
    @DisplayName("Tests if removeCartProduct works with valid input.")
    public void testIfRemoveCartProductWorksWithValidInput(){
        try {
            cart.removeCartProduct(cartProduct.getProduct());
        }catch (IllegalArgumentException | CouldNotRemoveCartProductException exception){
            addErrorWithException("Expected the", "product to be removed since the input is valid", exception);
        }
    }

    /**
     * Tests if getCartProduct works with invalid input.
     */
    @Test
    @DisplayName("Tests if getCartProduct works with invalid input.")
    public void testIfGetCartProductWorksWithInvalidInput(){
        try {
            cart.getCartProduct(0);
            addError(illegalPrefix, "the input is 0");
        }catch (IllegalArgumentException exception){}
        catch (CouldNotGetCartProductException exception){
            addError(illegalPrefix, "the input is 0");
        }
        try {
            cart.getCartProduct(-1);
            addError(illegalPrefix, "the input is negative");
        }catch (IllegalArgumentException exception){}
        catch (CouldNotGetCartProductException exception){
            addError(illegalPrefix, "the input is negative");
        }
        try {
            cart.getCartProduct(500);
            addError(getPrefix, "the input is not in this cart");
        }catch (IllegalArgumentException exception){
            addError(getPrefix, "the input is not in this cart");
        } catch (CouldNotGetCartProductException exception){}

    }


    /**
     * Tests if getCartProduct works with valid input.
     */
    @Test
    @DisplayName("Tests if getCartProduct works with valid input.")
    public void testIfGetCArtProductWorksWithValidInput(){
        try {
            cart.getCartProduct(cartProduct.getProduct().getProductID());
        }catch (IllegalArgumentException | CouldNotGetCartProductException exception){
            addErrorWithException("Expected the", "product to be found since its in the list", exception);
        }
    }
}
