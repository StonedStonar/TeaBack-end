package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Represents the CompanyDetails class. Should give the information for a company.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@Entity
public class CompanyDetails extends Details{

    @Transient
    private Address address;

    /**
     * Makes an instance of the CompanyDetails.
     */
    public CompanyDetails() {

    }

    /**
     * Makes an instance of the CompanyDetails Class.
     * @param description the description to be written.
     * @param address the address for a company.
     */
    public CompanyDetails(String description, Address address) {
        super(description);
        checkIfObjectIsNull(address, "address");
        this.address = address;
    }

    /**
     * Gets the address for a company.
     * @return address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
