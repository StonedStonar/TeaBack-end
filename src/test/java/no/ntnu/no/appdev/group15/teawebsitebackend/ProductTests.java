package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Positive and negative tests for the product class.
 * @author Kenneth Misund
 * @version 0.1
 */
public class ProductTests {

    private Product product;
    private Details details;
    private Company company;

    /**
     * Sets up the basic test environment for a product.
     */
    @BeforeEach
    public void setupTestProduct(){
        details = new TeaDetails();
        company = new Company();
        try{
            product = new Product(4, "Lord title", 60000, 3, details, company);
        }catch (IllegalArgumentException exception){
            fail("Expected the product to be made with valid input. ");
        }
    }

    /**
     * Makes an arrayList with product to be used in other test methods.
     * @return products.
     */
    private List<Product> makeAFewProduct() {
        List<Product> productList = new ArrayList();
        productList.add(new Product(0, "Troll Te", 49.99f, 23, details, company));
        productList.add(new Product(1, "Fæske Te", 29.99f, 2, details, company));
        return productList;
    }

    /**
     * Test that the constructor works with valid input. A product should be created correctly.
     * Negative test. test wrong input.
     */
    @Test
    public void testIfConstructorWorkWithValidInput() {
    }

    /**
     * Test if the constructor fails to make a product with invalid input.
     */
    @Test
    public void testIfConstructorWorksWithInvalidInput() {
        try {
            product = new Product(-4, "", -2,0,details,company);
            fail("Expected to get an IllegalArgumentException since the input was invalid.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if setProductName works with valid input.
     * This is a positive test and should result in correct result with valid input.
     */
    @Test
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
            fail("Expected the product name to be since the input was valid. ");
        }
    }

    /**
     * Tests if setProductName works with invalid input.
     * Negative test. Test result should be wrong input.
     */
    @Test
    public void testIfSetProductNameWorksWithInvalidInput() {
        try {
            product.setProductName("");
            fail("Expected to get an IllegalArgumentException since the input was invalid.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if setPrice works with valid input.
     * Positive test. Test result where input is correct.
     */
    @Test
    public void testIfSetPriceWorksWithValidInput() {
        try {
            float price = 2300;
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
    public void testIfSetPriceWorksWithInvalidInput() {
        try {
            product.setPrice(-500);
            fail("Expected to get an IllegalArgumentException since the input is invalid. ");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests if setAmountOfProducts works with valid input.
     * Positive test. all values above and zero should be valid. Everything else invalid.
     */
    @Test
    public void testIfSetAmountOfProductWorksWithValidInput() {
        try {
            product.setAmountOfProduct(90);
            assertEquals(90 ,product.getAmountOfProduct());
        } catch (IllegalArgumentException e) {
            fail("Expected the product to be of valid input. ");
        }
    }

    /**
     * Tests if the setAmountOfProducts works with invalid input.
     * Negative test. Test result should be valid with wrong input.
     */
    @Test
    public void testIfSetAmountOfProductsWorksWithInvalidInput() {
        try {
            product.setAmountOfProduct(-1);
            fail("Expected to get an IllegalArgumentException since the input should be invalid. ");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

}
