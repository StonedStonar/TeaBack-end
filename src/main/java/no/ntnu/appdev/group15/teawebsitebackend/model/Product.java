package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddReviewException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveReviewException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the product class. This class should hold all the information needed for a product.
 * @author Kenneth Misund
 * @version 0.1
 */
public class Product {

    private long productID;
    private String productName;
    private float price;
    private int amountOfProduct;

    private Details details;
    private Company company;
    List<Review> reviews;

    /**
     * Makes an instance of the Product class.
     */
    public Product(long id, String productName, float price, int size, Details details, Company company) {
        checkIfObjectIsNull(id, "id");
        this.productID = id;

        setProductName(productName);
        setPrice(price);
        setAmountOfProduct(size);

        this.details = details;
        this.company = company;

        this.reviews = new ArrayList<>();
    }

    /**
     * Gets the unique productID for a product.
     *
     * @return productID in long.
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Gets the product name given for a product.
     *
     * @return product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name for a product.
     *
     * @param productName to be set.
     */
    public void setProductName(String productName) {
        checkString(productName, "product name");
        this.productName = productName;
    }

    /**
     * Gets  the price given for a product.
     *
     * @return the price.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Sets the price of a product.
     * @param price price to be set for a product.
     */
    public void setPrice(float price) {
        checkIfFloatNotNegative(price, "price");
        this.price = price;
    }

    /**
     * Gets the size/amount of products in a collection of products.
     *
     * @return int size/amount.
     */
    public int getAmountOfProduct() {
        return amountOfProduct;
    }

    /**
     * Sets the amount of products in a collection.
     * @param amountOfProduct amount of products to be set.
     */
    public void setAmountOfProduct(int amountOfProduct) {
        checkIfIntegerNotNegative(amountOfProduct, "amount of product(s)");
        this.amountOfProduct = amountOfProduct;
    }

    /**
     * Gets the details around a product.
     *
     * @return the details given.
     */
    public Details getDetails() {
        return details;
    }

    /**
     * Gets the Company.
     *
     * @return the company name.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Adds a review to a product.
     * @param review the review to be added.
     * @throws CouldNotAddReviewException gets thrown if the review could not be added.
     */
    public void addReview(Review review) throws CouldNotAddReviewException {
    }

    /**
     * Remove a review from a product.
     * @param review the review to be removed.
     * @throws CouldNotRemoveReviewException gets thrown if the review could not be removed.
     */
    public void removeReview(Review review) throws CouldNotRemoveReviewException {
    }

    /**
     * The amount of product(s) to be added.
     * @param amountOfProduct amount to be added.
     */
    public void addAmountOfProduct(int amountOfProduct) {
        checkIfIntegerNotNegative(amountOfProduct, "amount to be added");
        this.amountOfProduct += amountOfProduct;
    }

    /**
     * The amount of product(s) to be removed.
     * @param amountOfProduct amount to be removed.
     */
    public void removeAmountOfProduct(int amountOfProduct) {
        checkIfIntegerNotNegative(amountOfProduct, "amount to be removed");
        this.amountOfProduct -= amountOfProduct;
    }


    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    /**
     * Checks if an float value is above or zero so that we cannot enter negative values.
     *
     * @param product the product to be checked.
     * @param error   the message the exception should hold.
     */
    private void checkIfFloatNotNegative(float product, String error) {
        if (product < 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative values.");
        }
    }

    /**
     * Check to make sure that integer values cannot be negative.
     * @param object the object to be checked.
     * @param error exception message to be displayed.
     */
    private void checkIfIntegerNotNegative(int object, String error) {
        if (object < 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative values.");
        }
    }
}
