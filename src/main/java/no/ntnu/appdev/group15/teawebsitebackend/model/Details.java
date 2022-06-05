package no.ntnu.appdev.group15.teawebsitebackend.model;

import javax.persistence.*;

/**
 * Represents details for a thing.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Details {

    @Id
    @GeneratedValue
    private long detailsID;

    private String description;

    public Details(){

    }

    /**
     * Makes an instance of the Details class.
     * @param description the description.
     */
    public Details(String description) {
        checkString(description, "description");
        this.description = description;
        this.detailsID = 0;
    }

    /**
     * Makes an instance of the Details class.
     * @param detailsID the id of the details.
     * @param description the description.
     */
    public Details(long detailsID, String description){
        checkString(description, "description");
        checkIfNumberIsAboveZero(detailsID, "detailsID");
        this.description = description;
        this.detailsID = detailsID;
    }

    /**
     * Gets the description of the tea.
     * @return the description of the tea
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the details' id.
     * @return the details' id.
     */
    public long getDetailsID(){
        return detailsID;
    }

    /**
     * Sets a description of the tea.
     * @param description a new description.
     */
    public void setDescription(String description) {
        checkString(description, "ingredients");
        this.description = description;
    }

    /**
     * Checks if the input number is above zero.
     * @param numberToCheck the number to check.
     */
    private void checkIfNumberIsAboveZero(long numberToCheck, String prefix){
        if (numberToCheck <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be larger than zero.");
        }
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
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
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
