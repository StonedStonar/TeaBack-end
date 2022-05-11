package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddCartProductException represents an exception that gets thrown when a cart product could not be added.
 *
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddCartProductException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddCartProductException class.
     *
     * @param message the error message.
     */
    public CouldNotAddCartProductException(String message) {
        super(message);

    }
}