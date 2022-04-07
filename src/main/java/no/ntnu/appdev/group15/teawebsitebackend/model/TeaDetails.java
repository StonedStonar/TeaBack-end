package no.ntnu.appdev.group15.teawebsitebackend.model;

import java.util.ArrayList;
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
    private List<Tag> tagList;

    /**
     * Constructor for the JPA-shit.
     */
    public TeaDetails() {}

    /**
     * makes an instance of teaDetails.
     * @param description the description of the tea.
     * @param ingredients the ingredients in the tea
     */
    public TeaDetails(String description, String ingredients){
        checkString(description, "description");
        this.description = description;

        checkString(ingredients, "ingredients");
        this.ingredients = ingredients;

        this.tagList = new ArrayList<>();
    }

    /**
     * Gets the description of the tea.
     * @return the description of the tea
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the ingredients in the tea
     * @return the ingredients
     */
    public String getIngredients() {
        return ingredients;
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
     * Adding a tag to tagList.
     * @param tagToAdd the tag to add.
     * @throws CouldNotAddTagException
     */
    @Override
    public void addTag(Tag tagToAdd) throws CouldNotAddTagException {
        checkIfObjectIsNull(tagToAdd, "tag to add");
        if (!checkIfTagIsPartOfDetails(tagToAdd)) {
            tagList.add(tagToAdd);
        } else {
            throw new CouldNotAddTagException(tagToAdd + "The tag is already in Tag");
        }
    }

    /**
     * Removes the tag from taglist.
     * @param tagToRemove the tag to remove.
     * @throws CouldNotRemoveTagException
     */
    @Override
    public void removeTag(Tag tagToRemove) throws CouldNotRemoveTagException {
        checkIfObjectIsNull(tagToRemove, "tag to remove");
        if (!checkIfTagIsPartOfDetails(tagToRemove)) {
            tagList.remove(tagToRemove);
        } else {
            throw new CouldNotRemoveTagException(tagToRemove + "could not be removed");
        }
    }

    /**
     * Checks if a string is invalid by being empty.
     * @param stringToCheck the String to check.
     * @param errorPrefix the error if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string is empty or null.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if(stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if a string is invalid by being empty.
     * @param object the String to check.
     * @param errorPrefix the error if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string is empty or null.
     */
    private void checkIfObjectIsNull(Object object, String errorPrefix){
        if (object == null) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be null.");
        }
    }

    /**
     * Checks if the tag is part of details.
     * @param tag the tag to check for.
     * @return
     */
    @Override
    public boolean checkIfTagIsPartOfDetails(Tag tag) {
        boolean valid = tagList.stream().anyMatch(tag1 -> tag.getTagID() == tag1.getTagID());
        return valid;
    }

}
