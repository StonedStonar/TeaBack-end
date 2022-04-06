package no.ntnu.appdev.group15.teawebsitebackend.model;

import java.time.LocalDate;
/**
 * Represents the Review class. Users/customers should be able to add or delete their reviews for products.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */

public class Review {

    private String reviewTitle;
    private String customerName;
    private User user;
    private String comment;

    private LocalDate dateOfReview;
    private int rating;


    /**
     * Makes an instance of the review class. Should hold all the information needed to make a review.
     * @param title title of the review.
     * @param name customer name to be entered.
     * @param user username to be entered.
     * @param comment the comment description.
     * @param date the review was posted.
     * @param rating rating given from 0-5.
     */
    public Review (String title, String name, User user,String comment, LocalDate date, int rating) {
        checkString(title, "review");
        this.reviewTitle = title;
        checkString(name, "name");
        this.customerName = name;
        checkIfObjectIsNull(user, "user");
        this.user = user;
        //Litt usikker her, for det må være mulig å sende bare rating og ikke en full review med kommentar kanskje?.
        this.comment = comment;
        this.dateOfReview = date;

        if (rating >= 0 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("The rating cannot be higher than 5 or less than 0");
        }
    }

    /**
     * Gets the name of the customer that leaves a review.
     * @return customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets the user/username for a person.
     * @return user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the comment that has been written.
     * @return written comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets the date and time when the review was posted.
     * @return time and date.
     */
    public LocalDate getDateOfReview() {
        return dateOfReview;
    }

    /**
     * Gets the rating from 0-5 that a customer/user has left.
     * @return rating from 0-5.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Gets the title for the review.
     * @return entered title.
     */
    private String getTitle() {
        return reviewTitle;
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
}
