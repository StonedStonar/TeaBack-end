package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveCompanyException represents an exception that gets thrown when a company could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveCompanyException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveCompanyException class.
     *
     * @param message the error message.
     */
    public CouldNotRemoveCompanyException(String message) {
        super(message);

    }
}