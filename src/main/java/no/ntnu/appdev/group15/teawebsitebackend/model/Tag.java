package no.ntnu.appdev.group15.teawebsitebackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Represents a basic tag class. This class is used to describe something by giving it tags.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Entity //To say to JPA that this is a table.
public class Tag {

    @Id //The primary key in the DB. Should only be set once.
    @GeneratedValue
    private long tagID;

    @Column(unique = true)
    private String tagName;

    private String tagDescription;

    /**
     * This constructor is here ONLY for JPA.
     * I am doing research on how to get rid of this useless little sh*****.
     */
    public Tag() {

    }

    /**
     * Makes an instance of the tag object.
     * @param tagID the id of the tag.
     * @param tagName the name of the tag.
     * @param tagDescription the description of the tag.
     * @throws IllegalArgumentException gets thrown if the input parameters are invalid format.
     */
    @JsonCreator
    public Tag(@JsonProperty("tagID") long tagID, @JsonProperty("tagName") String tagName, @JsonProperty("tagDescription") String tagDescription) {
        checkIfTagIDIsAboveZero(tagID);
        checkIfTagNameIsValid(tagName);
        checkIfTagDescriptionIsValid(tagDescription);
        this.tagID = tagID;
        this.tagDescription = tagDescription;
        this.tagName = tagName;
    }

    /**
     * Gets the tag's id.
     * @return the id of the tag.
     */
    public long getTagID() {
        return tagID;
    }

    /**
     * Gets the name of the tag.
     * @return the name of the tag.
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Sets the tag name to a new value.
     * @param tagName the new tag name.
     */
    public void setTagName(String tagName) {
        checkIfTagNameIsValid(tagName);
        this.tagName = tagName;
    }

    /**
     * Gets the description of the tag.
     * @return the description of the tag.
     */
    public String getTagDescription() {
        return tagDescription;
    }

    /**
     * Sets the description to a new value.
     * @param tagDescription the new description of the tag.
     * @throws IllegalArgumentException gets thrown if the input is empty or null.
     */
    public void setTagDescription(String tagDescription) {
        checkIfTagDescriptionIsValid(tagDescription);
        this.tagDescription = tagDescription;
    }

    /**
     * Checks if the tag id is above zero.
     * @param tagID the tag id to check.
     * @throws IllegalArgumentException gets thrown if the tag id is below or equal to zero.
     */
    private void checkIfTagIDIsAboveZero(long tagID){
        checkIfLongIsAboveZero(tagID, "tag id");
    }

    /**
     * Checks if the tag name is not null.
     * @param tagName the tag name to check for.
     * @throws IllegalArgumentException gets thrown if the tag name is empty or null.
     */
    private void checkIfTagNameIsValid(String tagName){
        checkString(tagName, "tag name");
    }

    /**
     * Checks if the tag description is not null.
     * @param tagDescription the tag description to check for.
     * @throws IllegalArgumentException gets thrown if the tag description is empty or null.
     */
    private void checkIfTagDescriptionIsValid(String tagDescription){
        checkString(tagDescription, "tag description");
    }

    /**
     * Checks if the input long is above zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     * @throws IllegalArgumentException gets thrown if the number is below or equal to zero.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be above zero.");
        }
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string is empty or null.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
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
