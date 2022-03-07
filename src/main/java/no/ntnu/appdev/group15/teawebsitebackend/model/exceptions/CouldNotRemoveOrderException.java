package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveOrderException represents an exception that gets thrown when an order could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveOrderException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveOrderException class.
     *
     * @param message the error message.
     */
    public CouldNotRemoveOrderException(String message) {
        super(message);

    }
}