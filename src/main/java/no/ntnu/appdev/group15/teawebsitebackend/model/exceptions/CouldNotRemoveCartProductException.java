package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveCartProductException represents an exception that gets thrown when a cart product could not be removed.
 *
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveCartProductException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveCartProductException class.
     *
     * @param message the error message.
     */
    public CouldNotRemoveCartProductException(String message) {
        super(message);

    }
}