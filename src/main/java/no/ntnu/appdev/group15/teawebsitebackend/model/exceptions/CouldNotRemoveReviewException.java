package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveReviewException represents an exception that gets thrown when a review could not be removed.
 * @author Kenneth Misund
 * @version 0.1
 */
public class CouldNotRemoveReviewException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveTagException class.
     *
     * @param review the error message.
     */
    public CouldNotRemoveReviewException(String review) {
        super(review);
    }
}