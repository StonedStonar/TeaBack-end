package no.ntnu.appdev.group15.teawebsitebackend.model.registers;

import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

import java.util.List;

/**
 * Represents what basic methods a tags register should have.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface TagsRegister {

    /**
     * Adds a tag to the register.
     * @param tag the tag to add.
     * @throws CouldNotAddTagException gets thrown if the tag could not be added.
     */
    void addTag(Tag tag) throws CouldNotAddTagException;

    /**
     * Adds a tag with tag name and tag description.
     * @param tagName the tag name.
     * @param tagDescription the tag description.
     * @throws CouldNotAddTagException gets thrown if the tag could not be added.
     */
    void addTagWithDetails(String tagName, String tagDescription) throws CouldNotAddTagException;

    /**
     * Removes a tag from the register.
     * @param tag the tag to remove.
     * @throws CouldNotRemoveTagException gets thrown if the tag could not be located.
     */
    void removeTag(Tag tag) throws CouldNotRemoveTagException;

    /**
     * Removes a tag with the tag id.
     * @param tagID the id of the tag.
     * @throws CouldNotRemoveTagException gets thrown if the tag could not be located.
     */
    void removeTagWithTagId(long tagID) throws CouldNotRemoveTagException;

    /**
     * Gets the tag with the matching tag id.
     * @param tagID the id of the tag.
     * @return the tag that has this id.
     * @throws CouldNotGetTagException gets thrown if the tag could not be located.
     */
    Tag getTagWithID(long tagID) throws CouldNotGetTagException;

    /**
     * Updates a tag and its fields. Cannot alter the key.
     * @param tag the tag to update and the changes.
     * @throws CouldNotGetTagException gets thrown if the tag could not be located.
     */
    void updateTag(Tag tag) throws CouldNotGetTagException;

    /**
     * Gets all the tags in the register.
     * @return a list with all the tags.
     */
    List<Tag> getAllTags();
}
