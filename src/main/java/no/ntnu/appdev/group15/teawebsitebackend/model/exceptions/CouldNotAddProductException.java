package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddProductException represents an exception that gets thrown when a product could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddProductException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddProductException class.
     *
     * @param message the error message.
     */
    public CouldNotAddProductException(String message) {
        super(message);

    }
}