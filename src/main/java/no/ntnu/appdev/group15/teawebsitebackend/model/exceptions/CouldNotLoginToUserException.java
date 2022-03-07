package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotLoginToUserException represents an exception that gets thrown when login details mismatch given paramteres.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotLoginToUserException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotLoginToUserException class.
     *
     * @param message the error message.
     */
    public CouldNotLoginToUserException(String message) {
        super(message);

    }
}