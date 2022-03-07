package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddTagException represents an exception that gets thrown when a tag could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddTagException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddTagException class.
     *
     * @param message the error message.
     */
    public CouldNotAddTagException(String message) {
        super(message);

    }
}