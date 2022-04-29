package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

import javax.persistence.Inheritance;

/**
 * Represents a basic object that describes anything in general terms.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface Details {

    /**
     * Gets the description of the details object. This can be product description and so on.
     * @return the description.
     */
    String getDescription();

    /**
     * Sets the description of the details object.
     * @param description a new description.
     */
    void setDescription(String description);

    /**
     * Adds a tag to the details object.
     * @param tag the tag to add.
     * @throws CouldNotAddTagException gets thrown if the tag could not be added.
     */
    void addTag(Tag tag) throws CouldNotAddTagException;

    /**
     * Removes a tag from the details object.
     * @param tag the tag to remove.
     * @throws CouldNotRemoveTagException gets thrown if the tag could not be found.
     */
    void removeTag(Tag tag) throws CouldNotRemoveTagException;

    /**
     * Checks if the tag is a part of this details object
     * @param tag the tag to check for.
     * @return <code>true</code> if the tag is a part of this details object.
     *         <code>false</code> if the tag is not a part of this details object.
     */
    boolean checkIfTagIsPartOfDetails(Tag tag);

}
