package no.ntnu.appdev.group15.teawebsitebackend.model.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddCompanyException represents an exception that gets thrown when a company could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddCompanyException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddCompanyException class.
     *
     * @param message the error message.
     */
    public CouldNotAddCompanyException(String message) {
        super(message);

    }
}