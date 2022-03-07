package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveProductException represents an exception that gets thrown when a product could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveProductException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveProductException class.
     * @param message the error message.
     */
    public CouldNotRemoveProductException(String message) {
        super(message);

    }
}