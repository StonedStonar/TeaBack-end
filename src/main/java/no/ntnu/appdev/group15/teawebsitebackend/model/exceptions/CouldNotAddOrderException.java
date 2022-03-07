package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddOrderException represents an exception that gets thrown when an order could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddOrderException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddOrderException class.
     *
     * @param message the error message.
     */
    public CouldNotAddOrderException(String message) {
        super(message);

    }
}