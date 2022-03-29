package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotChangePasswordException represents an exception that gets thrown when
 * the old password does not match the users set password.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotChangePasswordException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotChangePasswordException class.
     *
     * @param message the error message.
     */
    public CouldNotChangePasswordException(String message) {
        super(message);

    }
}