package no.ntnu.appdev.group15.teawebsitebackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Represents a product that is in the
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Embeddable
public class CartProduct {

    private int amount;

    @OneToOne(targetEntity = Product.class)
    @JoinColumn(name = "productID")
    private Product product;

    /**
     * Makes an instance of the CartProduct class.
     */
    public CartProduct() {
        this.amount = 0;
        this.product = null;
    }

    /**
     * Makes an instance of the CartProduct class.
     * @param product the product that is in the cart.
     * @param amount the amount of that product.
     */
    @JsonCreator
    public CartProduct(@JsonProperty("product") Product product, @JsonProperty("amount") int amount) {
        checkIfObjectIsNull(product, "product");
        checkIfNumberNotNegative(amount, "amount");
        this.product = product;
        this.amount = amount;
    }

    /**
     * Sets the amount of cart product.
     * @param amountToAdd the amount to add.
     */
    public void setAmount(int amountToAdd){
        checkIfNumberNotNegative(amountToAdd, "amount to add");
        this.amount = amountToAdd;
    }

    /**
     * Gets the product that is ordered.
     * @return the product.
     */
    public Product getProduct(){
        return product;
    }

    /**
     * Gets the amount of that product.
     * @return the amount.
     */
    public int getAmount(){
        return amount;
    }

    /**
     * Check to make sure that integer values cannot go bellow 0. (Negative value)
     * @param number the object to be checked.
     * @param errorMessage exception to be displayed.
     */
    public void checkIfNumberNotNegative(long number, String errorMessage) {
        if(number <= 0) {
            throw new IllegalArgumentException("The " + errorMessage + " cannot be of negative value.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
