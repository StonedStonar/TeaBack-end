package no.ntnu.appdev.group15.teawebsitebackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddReviewException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveReviewException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the product class. This class should hold all the information needed for a product.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private long productID;

    private String productName;
    private int price;
    private int amountOfProduct;
    @JsonIgnore
    private boolean isOnSale;

    @OneToOne(targetEntity = ProductDetails.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "productDetailsID")
    private ProductDetails productDetails;

    @OneToOne(targetEntity = Company.class)
    @JoinColumn(name = "companyID")
    private Company company;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "productReviews", joinColumns = @JoinColumn(name = "productID"))
    @Column(name = "productID")
    private List<Review> reviews;

    /**
     * A constructor for the JPA.
     */
    public Product() {
        reviews = new ArrayList<>();
    }

    /**
     * Makes an instance of the Product class.
     * @param id Each product has to have a unique id.
     * @param productName product name of the product.
     * @param price price set for a product.
     * @param size size of products in the collection.
     * @param productDetails details written for a product.
     * @param company company information.
     */
    @JsonCreator
    public Product(@JsonProperty("productID")long id, @JsonProperty("productName") String productName,
                   @JsonProperty ("price")int price, @JsonProperty ("amountOfProduct") int size, @JsonProperty ("productDetails") ProductDetails productDetails, @JsonProperty ("company") Company company) {
        checkIfNumberNotNegative( id, "id");
        this.productID = id;

        setProductName(productName);
        setPrice(price);
        checkIfNumberNotNegative(size, "amount of products");
        this.amountOfProduct = size;

        checkIfObjectIsNull(productDetails, "details");
        this.productDetails = productDetails;
        checkIfObjectIsNull(company, "company");
        this.company = company;

        this.isOnSale = false;

        this.reviews = new ArrayList<>();
    }

    /**
     * Makes another instance of the Product class since it could be nice that the database autogen the id.
     * @param productName product name of the product.
     * @param price price set for a product.
     * @param size size of products in the collection.
     * @param productDetails details written for a product.
     * @param company company information.
     */
    @JsonCreator
    public Product (@JsonProperty("productName") String productName, @JsonProperty ("price") int price, @JsonProperty ("amountOfProduct") int size,
                    @JsonProperty ("productDetails") ProductDetails productDetails, @JsonProperty ("company") Company company) {
        this.productID = 0;
        setProductName(productName);
        setPrice(price);
        checkIfNumberNotNegative(size, "amount of products");
        this.amountOfProduct = size;

        checkIfObjectIsNull(productDetails, "details");
        this.productDetails = productDetails;
        checkIfObjectIsNull(company, "company");
        this.company = company;

        this.isOnSale = false;
        this.reviews = new ArrayList<>();
    }

    /**
     * Sets the company of this product.
     * @param company the company.
     */
    public void setCompany(Company company){
        checkIfObjectIsNull(company, "company");
        this.company = company;
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
    public int getPrice() {
        return price;
    }

    /**
     * Sets the price of a product.
     * @param price price to be set for a product.
     */
    public void setPrice(int price) {
        checkIfNumberNotNegative(price, "price");
        checkIfNumberNotNegative(price, "price");
        this.price = price;
    }

    public boolean isOnSale() {
        return isOnSale;
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
     * Gets the details around a product.
     *
     * @return the details given.
     */
    public ProductDetails getProductDetails() {
        return productDetails;
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

        checkIfObjectIsNull(review, "review");
        if (!reviews.contains(review)) {
            reviews.add(review);
        } else {
            throw new CouldNotAddReviewException("The review is already a part of the product.");
        }
    }

    /**
     * Remove a review from a product.
     * @param review the review to be removed.
     * @throws CouldNotRemoveReviewException gets thrown if the review could not be removed.
     */
    public void removeReview(Review review) throws CouldNotRemoveReviewException {
        checkIfObjectIsNull(review, "review");
        if (!reviews.remove(review)) {
            throw new CouldNotRemoveReviewException("The review was not part of the product.");
        }
    }

    /**
     * Gets all the reviews that have been made in form of a list.
     * @return reviews from list.
     */
    public List<Review> getReviews() {
        return reviews;
    }

    public int getAverageRating() {
       Iterator<Review> it = reviews.iterator();
       int totalSumOfStars = 0;
       while (it.hasNext()) {
           Review review = it.next();
           totalSumOfStars += review.getRating();
       }
       int rating = 0;
       if(reviews.size() != 0) {
           rating = totalSumOfStars/ reviews.size();
       }
       return rating;
    }

    /**
     * The amount of product(s) to be added.
     * @param amountOfProduct amount to be added.
     */
    public void addAmountOfProduct(long amountOfProduct) {
        checkIfNumberNotNegative(amountOfProduct, "amount to be added");
        this.amountOfProduct += amountOfProduct;
    }

    /**
     * The amount of product(s) to be removed. Should never be negative.
     * @param amountOfProduct amount to be removed.
     */
    public void removeAmountOfProduct(int amountOfProduct) {
        checkIfNumberNotNegative(amountOfProduct, "amount to be removed");
        int sum = this.amountOfProduct - amountOfProduct;
        if(sum < 0) {
            throw new IllegalArgumentException("Products should never reach negative amount");
        } else {
            this.amountOfProduct = sum;
        }
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
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    /**
     * Check to make sure that integer values cannot be negative.
     * @param object the object to be checked.
     * @param error exception message to be displayed.
     */
    private void checkIfNumberNotNegative(long object, String error) {
        if (object <= 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative values.");
        }
    }
}
