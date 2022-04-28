package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Represents a test class for an ordered product.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class OrderedProductTests {

    private OrderedProduct orderedProduct;

    private  StringBuilder stringBuilder;

    private String prefixIllegal;

    private int errors;

    @BeforeEach
    public void setupTest(){
        prefixIllegal = "Expected to get a IllegalArgumentException since ";
        this.stringBuilder = new StringBuilder();
        errors = 0;
        this.orderedProduct = makeOrderedProduct();
    }

    /**
     * Makes an ordered product.
     * @return the ordered product for testing.
     */
    private OrderedProduct makeOrderedProduct(){
        OrderedProduct orderedProduct = null;

        try {
            Product product = makeProduct();
            orderedProduct = new OrderedProduct(product, 2);
        }catch (IllegalArgumentException exception){
            fail("Expected the ordered product to be made since the input is valid.");
        }
        return orderedProduct;
    }
    /**
     * Makes a instance of the product class.
     * @return the product that was made.
     */
    private Product makeProduct(){
        return new Product(1, "Fjell laks Te", 399.99f, 100, new TeaDetails(), new Company());
    }

    /**
     * Tests if the constructor with two parameters works with invalid input.
     */
    @Test
    @DisplayName("Tests if the constructor with two parameters works with invalid input.")
    public void testIfConstructorWithTwoParametersWorksWithInvalidInput(){
        try {
            OrderedProduct orderedProduct = new OrderedProduct(null, 2);
            addError(prefixIllegal, "the product is null.");
        }catch (IllegalArgumentException exception){}
        try {
            OrderedProduct orderedProduct = new OrderedProduct(makeProduct(), -2);
            addError(prefixIllegal, "the amount is negative");
        }catch (IllegalArgumentException exception){}
        try {
            OrderedProduct orderedProduct = new OrderedProduct(makeProduct(), 0);
            addError(prefixIllegal, "the amount of product is zero.");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if the constructor with four parameters works with invalid input.
     */
    @Test
    @DisplayName("Tests if the constructor with four parameters works with invalid input.")
    public void testIfConstructorWithFourParametersWorksWithInvalidInput(){
        try {
            OrderedProduct orderedProduct = new OrderedProduct(1, null, 2, OrderState.ORDERED);
            addError(prefixIllegal, "the product is null");
        }catch (IllegalArgumentException exception){}
        try {
            OrderedProduct orderedProduct = new OrderedProduct(1, makeProduct(), -2, OrderState.ORDERED);
            addError(prefixIllegal, "amount is negative");
        }catch (IllegalArgumentException exception){}
        try {
            OrderedProduct orderedProduct = new OrderedProduct(1, makeProduct(), 0, OrderState.ORDERED);
            addError(prefixIllegal, "the amount is zero");
        }catch (IllegalArgumentException exception){}
        try {
            OrderedProduct orderedProduct = new OrderedProduct(1, makeProduct(), 3, null);
            addError(prefixIllegal, "the order state is null");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Checks if the constructors works with valid input.
     */
    @Test
    @DisplayName("Checks if the constructors works with valid input.")
    public void testIfConstructorWorksWithValidParameters(){
        String worksPrefix = "Expected the constructor to work since the input of ";
        try {
            OrderedProduct orderedProduct = new OrderedProduct(1, makeProduct(), 2, OrderState.ORDERED);
        }catch (IllegalArgumentException exception){
            addError(worksPrefix, "3 parameters are valid.");
        }
        try {
            OrderedProduct orderedProduct = new OrderedProduct(makeProduct(), 3);
        }catch (IllegalArgumentException exception) {
            addError(worksPrefix, "2 parameters are valid.");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setOrderStatus works with invalid input.
     */
    @Test
    @DisplayName("Tests if setOrderStatus works with invalid input.")
    public void testIfSetOrderStatusWorksWithInvalidInput(){
        try {
            this.orderedProduct.setOrderState(null);
            addError(prefixIllegal, "the input is null");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setOrderStatus works with valid input.
     */
    @Test
    @DisplayName("Tests if setOrderStatus works with valid input.")
    public void testIfSetOrderStatusWorksWithValidInput(){
        try {
            this.orderedProduct.setOrderState(OrderState.PACKED);
        }catch (IllegalArgumentException exception){
            addError("Expected the order status to be changed since the input is valid", "");
        }
        checkIfTestsFailedAndDisplayResult();
        assertEquals(orderedProduct.getOrderState(), OrderState.PACKED);
    }

    /**
     * Tests if removeAmountOfProduct works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeAmountOfProduct works with invalid input.")
    public void testIfRemoveAmountOfProductWorksWithInvalidInput(){
        try {
            orderedProduct.removeAmountOfProduct(-1);
            addError(prefixIllegal, "the input is negative.");
        }catch (IllegalArgumentException exception){}
        try {
            orderedProduct.removeAmountOfProduct(0);
            addError(prefixIllegal, "the input is zero");
        }catch (IllegalArgumentException exception){}
        try {
            orderedProduct.removeAmountOfProduct(orderedProduct.getAmountOfProduct() + 1);
            addError(prefixIllegal, "the input is one larger than the actual amount");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeAmountOfProduct works with valid input.
     */
    @Test
    @DisplayName("Tests if removeAmountOfProduct works with valid input.")
    public void testIfRemoveAmountOfProductWorksWithValidInput(){
        try {
            orderedProduct.removeAmountOfProduct(orderedProduct.getAmountOfProduct());
        }catch (IllegalArgumentException exception){
            addError("Expected the amount to removed since its the amount 2 is valid.", "");
        }
        OrderState orderState = orderedProduct.getOrderState();
        orderedProduct = makeOrderedProduct();
        try {
            orderedProduct.removeAmountOfProduct(orderedProduct.getAmountOfProduct() - 1);
        }catch (IllegalArgumentException exception){
            addError("Expected the amount to be removed since the input totalSize - 1 is valid", "");
        }
        OrderState orderState1 = orderedProduct.getOrderState();
        try {
            orderedProduct.removeAmountOfProduct(1);
        }catch (IllegalArgumentException exception){
            addError("Expected the amount to be removed since the input 1 is valid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
        assertEquals(orderState, OrderState.RETURNED);
        assertEquals(orderState1, OrderState.PARTIALRETURN);
        assertEquals(orderedProduct.getOrderState(), OrderState.RETURNED);
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
     * Checks if the tests failed and displays the results.
     */
    private void checkIfTestsFailedAndDisplayResult(){
        if(stringBuilder.length() == 0){
            assertTrue(true);
        }else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }
}
