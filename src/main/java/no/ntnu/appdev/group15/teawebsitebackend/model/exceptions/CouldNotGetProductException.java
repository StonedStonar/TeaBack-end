package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotGetProductException represents an exception that gets thrown when a product could not be found.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotGetProductException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetProductException class.
     *
     * @param message the error message.
     */
    public CouldNotGetProductException(String message) {
        super(message);

    }
}