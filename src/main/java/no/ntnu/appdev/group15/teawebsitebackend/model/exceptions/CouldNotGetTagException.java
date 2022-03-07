package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotGetTagException represents an exception that gets thrown when a tag could not be located.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotGetTagException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetTagException class.
     *
     * @param message the error message.
     */
    public CouldNotGetTagException(String message) {
        super(message);

    }
}