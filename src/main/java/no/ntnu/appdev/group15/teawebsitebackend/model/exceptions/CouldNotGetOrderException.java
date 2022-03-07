package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotGetOrderException represents an exception that gets thrown when the order could not be located.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotGetOrderException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetOrderException class.
     *
     * @param message the error message.
     */
    public CouldNotGetOrderException(String message) {
        super(message);

    }
}