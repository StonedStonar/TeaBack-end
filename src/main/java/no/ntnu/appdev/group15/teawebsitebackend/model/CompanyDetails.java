package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

/**
 * Represents the CompanyDetails class. Should give the information for a company.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public class CompanyDetails implements Details {

    private String description;
    private Address address;


    /**
     * Makes an instance of the CompanyDetails Class.
     * @param description the description to be written.
     * @param address the address for a company.
     */
    public CompanyDetails(String description, Address address) {
        setDescription(description);

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
     * Gets the description about a company.
     * @return description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for a company.
     * @param description a new description.
     */
    @Override
    public void setDescription(String description) {
        checkString(description, "description for company details");
        this.description = description;
    }

    /**
     * Adds a tag for the company.
     * @param tag the tag to add.
     * @throws CouldNotAddTagException
     */
    @Override
    public void addTag(Tag tag) throws CouldNotAddTagException {

    }

    /**
     * Removes a tag for the company.
     * @param tag the tag to remove.
     * @throws CouldNotRemoveTagException
     */
    @Override
    public void removeTag(Tag tag) throws CouldNotRemoveTagException {

    }

    /**
     * Checks if the tag is part of the details given for a company.
     * @param tag the tag to check for.
     * @return tag.
     */
    @Override
    public boolean checkIfTagIsPartOfDetails(Tag tag) {
       return false;
    }


    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
