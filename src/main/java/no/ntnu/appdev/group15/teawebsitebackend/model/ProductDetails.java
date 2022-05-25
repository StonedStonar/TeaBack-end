package no.ntnu.appdev.group15.teawebsitebackend.model;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * Represents a tea-details class, containing details about a specific tea.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@Entity
public class ProductDetails extends Details{

    private String ingredients;

    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(name = "productTags",
            joinColumns = @JoinColumn(name= "detailsID", referencedColumnName = "detailsID"),
            inverseJoinColumns = @JoinColumn(name= "tagID", referencedColumnName = "tagID")
    )
    private List<Tag> tagList;

    public ProductDetails() {
        super();
    }

    /**
     * makes an instance of teaDetails.
     * @param description the description of the tea.
     * @param ingredients the ingredients in the tea
     */
    public ProductDetails(String description, String ingredients){
        super(description);
        checkString(ingredients, "ingredients");
        this.ingredients = ingredients;
        this.tagList = new ArrayList<>();
    }

    /**
     * makes an instance of teaDetails.
     * @param detailsId the details' id.
     * @param description the description of the tea.
     * @param ingredients the ingredients in the tea
     */
    public ProductDetails(long detailsId, String description, String ingredients){
        super(detailsId, description);
        checkString(ingredients, "ingredients");
        this.ingredients = ingredients;
        this.tagList = new ArrayList<>();
    }

    /**
     * Gets the ingredients in the tea
     * @return the ingredients
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Adding a tag to tagList.
     * @param tagToAdd the tag to add.
     * @throws CouldNotAddTagException gets thrown if the tag is already in the details.
     */
    public void addTag(Tag tagToAdd) throws CouldNotAddTagException {
        checkIfObjectIsNull(tagToAdd, "tag to add");
        if (!checkIfTagIsPartOfDetails(tagToAdd)) {
            tagList.add(tagToAdd);
        } else {
            throw new CouldNotAddTagException(tagToAdd + "The tag is already in Tag");
        }
    }

    /**
     * Removes the tag from tag list.
     * @param tagToRemove the tag to remove.
     * @throws CouldNotRemoveTagException gets thrown if the tag could not be removed.
     */
    public void removeTag(Tag tagToRemove) throws CouldNotRemoveTagException {
        checkIfObjectIsNull(tagToRemove, "tag to remove");
        if (!checkIfTagIsPartOfDetails(tagToRemove)) {
            tagList.remove(tagToRemove);
        } else {
            throw new CouldNotRemoveTagException(tagToRemove + "could not be removed");
        }
    }

    /**
     * Gets all the tags within this product details.
     * @return gets all the tags.
     */
    public List<Tag> getTagList(){
        return tagList;
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
    public boolean checkIfTagIsPartOfDetails(Tag tag) {
        boolean valid = tagList.stream().anyMatch(tag1 -> tag.getTagID() == tag1.getTagID());
        return valid;
    }

}
