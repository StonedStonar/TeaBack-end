package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveTagException represents an exception that gets thrown when a tag could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveTagException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveTagException class.
     *
     * @param message the error message.
     */
    public CouldNotRemoveTagException(String message) {
        super(message);

    }
}