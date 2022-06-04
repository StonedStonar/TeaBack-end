package no.ntnu.appdev.group15.teawebsitebackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

/**
 * Represents a product that has been ordered by a customer.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
//Todo: Litt usikker på om denne trenger en ID. Tenk litt på det og hittl settes den bare som embeddable.
@Entity
@Table(name = "orderedProduct")
public class OrderedProduct {

    @Id
    @GeneratedValue
    private long orderedProductID;

    @OneToOne(targetEntity = Product.class)
    @JoinColumn(name = "productID")
    private Product product;

    private int amountOfProduct;

    private int returnedProductAmount;

    private int pricePerProduct;

    @Enumerated
    private OrderState orderState;

    /**
     * Empty constructor for JPA.
     */
    public OrderedProduct() {
    }

    /**
     * Makes an instance of the OrderedProduct class.
     * @param orderedID the id of the order.
     * @param product the product that was ordered.
     * @param amountOfProduct the amount of the product that was ordered.
     * @throws IllegalArgumentException if any of the input arguments are invalid format.
     */
    @JsonCreator
    public OrderedProduct(@JsonProperty("orderedProductID") long orderedID, @JsonProperty("product") Product product, @JsonProperty("amountOfProduct") int amountOfProduct, @JsonProperty("orderState")OrderState orderState) {
        checkIfProductIsValid(product);
        checkIfAmountIsValid(amountOfProduct);
        checkOrderState(orderState);
        checkIfLongIsAboveZero(orderedID, "ordered id");
        this.product = product;
        this.pricePerProduct = product.getPrice();
        this.amountOfProduct = amountOfProduct;
        this.returnedProductAmount = 0;
        this.orderedProductID = orderedID;
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
        this.returnedProductAmount = 0;
        this.orderedProductID = 0;
        this.orderState = OrderState.ORDERED;
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
    public int getPricePerProduct() {
        return pricePerProduct;
    }

    /**
     * Gets the total price of the products.
     * @return the total price.
     */
    @JsonIgnore
    public int getTotalPrice(){
        return pricePerProduct * amountOfProduct;
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

    /**
     * Gets the ordered product id.
     * @return the ordered product id.
     */
    public long getOrderedProductID() {
        return orderedProductID;
    }
}
