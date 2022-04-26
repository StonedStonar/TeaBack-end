package no.ntnu.appdev.group15.teawebsitebackend.model;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Represents a product that has been ordered by a customer.
 * @author
 * @version 0.1
 */
//Todo: Litt usikker på om denne trenger en ID. Tenk litt på det og hittl settes den bare som embeddable.
@Entity
public class OrderedProduct {
    @Id
    @GeneratedValue
    private long orderedID;

    @OneToOne(targetEntity = Product.class)
    private Product product;

    private int amountOfProduct;

    private int returnedProductAmount;

    private float pricePerProduct;

    private OrderState orderState;

    /**
     * Empty constructor for JPA.
     */
    public OrderedProduct() {
    }

    /**
     * Makes an instance of the OrderedProduct class.
     * @param product the product that was ordered.
     * @param amountOfProduct the amount of the product that was ordered.
     * @throws IllegalArgumentException if any of the input arguments are invalid format.
     */
    public OrderedProduct(Product product, int amountOfProduct, OrderState orderState) {
        checkIfProductIsValid(product);
        checkIfAmountIsValid(amountOfProduct);
        checkOrderState(orderState);
        this.product = product;
        this.pricePerProduct = product.getPrice();
        this.amountOfProduct = amountOfProduct;
        this.returnedProductAmount = 0;
        this.orderState = orderState;
    }

    /**
     * Makes an instance of the OrderedProduct class.
     * @param product the product that was ordered.
     * @param amountOfProduct the amount of that product that was ordered.
     * @throws IllegalArgumentException gets thrown if one of the parameters are invalid values like null or 0.
     */
    public OrderedProduct(Product product, int amountOfProduct){
        checkIfProductIsValid(product);
        checkIfAmountIsValid(amountOfProduct);
        this.product = product;
        this.pricePerProduct = product.getPrice();
        this.amountOfProduct = amountOfProduct;
        this.orderState = OrderState.ORDERED;
        this.returnedProductAmount = 0;
    }

    /**
     * Changes the order state.
     * @param orderState the order state to change to.
     * @throws IllegalArgumentException gets thrown if the order state is null.
     */
    public void setOrderState(OrderState orderState){
        checkOrderState(orderState);
        this.orderState = orderState;
    }

    /**
     * Gets the state of the ordered product.
     * @return the state of the ordered product.
     */
    public OrderState getOrderState(){
        return orderState;
    }

    /**
     * Gets the product that was ordered.
     * @return the product.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Gets the amount of product that was ordered.
     * @return the amount of product.
     */
    public int getAmountOfProduct() {
        return amountOfProduct;
    }

    /**
     * Gets the amount of returned products.
     * @return the amount of returned products.
     */
    public int getReturnedProduct() {
        return returnedProductAmount;
    }

    /**
     * Gets the price that the product was bought for.
     * @return the price the product was bought for.
     */
    public float getPricePerProduct() {
        return pricePerProduct;
    }

    /**
     * Removes an amount of product from this object. If it hits zero the order is set to returned.
     * @param amountOfProduct the amount to remove.
     * @throws IllegalArgumentException gets thrown if the number is zero or above the amount in this object.
     */
    public void removeAmountOfProduct(int amountOfProduct){
        checkIfAmountIsValid(amountOfProduct);
        if (amountOfProduct > this.amountOfProduct || (returnedProductAmount + amountOfProduct) > this.amountOfProduct){
            throw new IllegalArgumentException("The amount of product to remove cannot be bigger than the ordered amount.");
        }
        this.returnedProductAmount += amountOfProduct;
        if (this.returnedProductAmount == this.amountOfProduct){
            this.orderState = OrderState.RETURNED;
        }else {
            this.orderState = OrderState.PARTIALRETURN;
        }
    }

    /**
     * Checks if the amount is below or equal to zero.
     * @param amountOfProduct the amount of product to check
     * @throws IllegalArgumentException gets thrown if the amount of product is equal or lass than zero.
     */
    private void checkIfAmountIsValid(int amountOfProduct){
        checkIfLongIsAboveZero(amountOfProduct, "amount of product");
    }

    /**
     * Checks if the order state is not null.
     * @param orderState the order state to check.
     * @throws IllegalArgumentException gets thrown if the order state is null.
     */
    private void checkOrderState(OrderState orderState){
        checkIfObjectIsNull(orderState, "order state");
    }

    /**
     * Checks if the product is not null.
     * @param product the product to check.
     * @throws IllegalArgumentException gets thrown if the product is null.
     */
    private void checkIfProductIsValid(Product product){
        checkIfObjectIsNull(product, "product");
    }

    /**
     * Checks if the input long is above zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     * @throws IllegalArgumentException gets thrown if the number is equal or below zero.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be above zero.");
        }
    }

    /**
     * Checks if an object is null.
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
