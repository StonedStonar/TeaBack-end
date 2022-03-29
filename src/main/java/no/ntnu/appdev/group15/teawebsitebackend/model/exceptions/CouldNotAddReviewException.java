package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddReviewException represents an exception that gets thrown when a review could not be added.
 * @author Kenneth Misund
 * @version 0.1
 */
public class CouldNotAddReviewException extends Exception implements Serializable {

    /**
     * Makes an instance of CouldNotAddReviewException.
     * @param message error message to be displayed.
     */
    public CouldNotAddReviewException(String message) {
        super(message);
    }
}
