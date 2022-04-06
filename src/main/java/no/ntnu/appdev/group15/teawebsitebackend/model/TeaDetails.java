package no.ntnu.appdev.group15.teawebsitebackend.model;

import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

/**
 * Represents a tea-details class, containing details about a specific tea.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
public class TeaDetails implements Details {

    private String description;
    private String ingredients;
    private List<Tag> taglist;

    /**
     * Constructor for the JPA-shit
     */
    public TeaDetails() {}

    /**
     * makes an instance of teaDetails
     * @param description the description of the tea.
     * @param ingredients the ingredients in the tea
     * @param taglist list of tags
     *
     */
    public TeaDetails(String description, String ingredients, List<Tag> taglist){
        //TODO checkssss
        this.description = description;
        this.ingredients = ingredients;
        this.taglist = taglist;
    }

    /**
     * Gets the description of the tea
     * @return the description of the tea
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a description of the tea
     * @param description a new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adding a tag to taglist
     * @param tagToAdd the tag to add.
     * @throws CouldNotAddTagException
     */
    @Override
    public void addTag(Tag tagToAdd) throws CouldNotAddTagException {
    }

    /**
     * Removes the tag from taglist.
     * @param tagToRemove the tag to remove.
     * @throws CouldNotRemoveTagException
     */
    @Override
    public void removeTag(Tag tagToRemove) throws CouldNotRemoveTagException {

    }

    /**
     * Checks if the tag is part of details
     * @param tag the tag to check for.
     * @return
     */
    @Override
    public boolean checkIfTagIsPartOfDetails(Tag tag) {
        return false;
    }

    /**
     * Checks if the tag is part of details with tag ID.
     * @param tagID the ID of the tag
     * @return
     */
    public boolean checkIfTagIsPartOfDetailsWithTagID(Long tagID) {
        return false;
    }
}
