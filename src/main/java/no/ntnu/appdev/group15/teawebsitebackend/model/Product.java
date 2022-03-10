package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddReviewException;

/**
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

    /**
     * Makes an instance of the Product class.
     */
    public Product(long id, String productName, float price, int size) {
        this.productID = id;

        this.productName = productName;

        checkIfProductIsAboveZero(price, "product price");
        this.price = price;

        if (size < 0) {
            throw new IllegalArgumentException("amount of products cannot be less than zero");
        } else {
            this.amountOfProduct = size;
        }
    }

    /**
     * Gets the unique productID for a product.
     * @return productID in long.
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Gets the product name given for a product.
     * @return product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name for a product.
     * @param productName to be set.
     */
    public void setProductName(String productName) {
        checkString(productName, "product name");
        this.productName = productName;
    }

    /**
     * Gets  the price given for a product.
     * @return the price.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Gets the size/amount of products in a collection of products.
     * @return int size/amount.
     */
    public int getAmountOfProduct() {
        return amountOfProduct;
    }

    /**
     * Gets the details around a product.
     * @return the details given.
     */
    public Details getDetails() {
        return details;
    }

    /**
     * Gets the Company.
     * @return the company name.
     */
    public Company getCompany() {
        return company;
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
     * @param product the product to be checked.
     * @param error the message the exception should hold.
     */
    private void checkIfProductIsAboveZero(float product, String error) {
        if (product >= 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative or zero.");
        }
    }
}
