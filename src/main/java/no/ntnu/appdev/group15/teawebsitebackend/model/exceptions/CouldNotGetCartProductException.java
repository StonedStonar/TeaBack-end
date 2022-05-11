package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotGetCartProductException represents an exception that gets thrown when a cart product could not be found.
 *
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotGetCartProductException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetCartProductException class.
     *
     * @param message the error message.
     */
    public CouldNotGetCartProductException(String message) {
        super(message);

    }
}