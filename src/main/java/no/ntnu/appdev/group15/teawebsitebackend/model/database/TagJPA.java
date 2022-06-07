package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a register that has a database as its implementation.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Service
public class TagJPA implements TagsRegister {

    private TagRepository tagRepository;

    /**
     * Makes an instance of the TagJPA class.
     * @param tagRepository the tag repository that is the connection to the DB.
     */
    public TagJPA(TagRepository tagRepository) {
        checkIfObjectIsNull(tagRepository, "tag repository");
        this.tagRepository = tagRepository;
    }

    @Override
    public void addTag(Tag tag) throws CouldNotAddTagException {
        checkIfTagIsValid(tag);
        addTagWithDetails(tag.getTagName(), tag.getTagDescription());
    }

    @Override
    public void addTagWithDetails(String tagName, String tagDescription) throws CouldNotAddTagException {
        checkString(tagName, "tag name");
        checkString(tagDescription, "tag description");
        if (tagRepository.getTagWithTagName(tagName).isEmpty()){
            Tag tag = new Tag(Long.MAX_VALUE, tagName, tagDescription);
            tagRepository.save(tag);
        }else {
            throw new CouldNotAddTagException("The tag with the name " + tagName + " is already in the system.");
        }
    }

    @Override
    public void removeTag(Tag tag) throws CouldNotRemoveTagException {
        checkIfTagIsValid(tag);
        if (checkIfTagIsInRegister(tag.getTagID())){
            tagRepository.delete(tag);
        }else {
            throw new CouldNotRemoveTagException("The tag with id " + tag.getTagID() + " could not be located.");
        }
    }

    @Override
    public void removeTagWithTagId(long tagID) throws CouldNotRemoveTagException {
        checkIfTagIDIsAboveZero(tagID);
        if (checkIfTagIsInRegister(tagID)){
            tagRepository.deleteById(tagID);
        }else {
            throw new CouldNotRemoveTagException("The tag with tag id " + tagID + " could not be located.");
        }
    }

    @Override
    public Tag getTagWithID(long tagID) throws CouldNotGetTagException {
        checkIfTagIDIsAboveZero(tagID);
        Optional<Tag> optionalTag = tagRepository.findById(tagID);
        if (optionalTag.isEmpty()){
            throw new CouldNotGetTagException("The tag with id " + tagID + " is not in the system.");
        }
        return optionalTag.get();
    }

    @Override
    public void updateTag(Tag tag) throws CouldNotGetTagException {
        checkIfTagIsValid(tag);
        if (checkIfTagIsInRegister(tag.getTagID()) && tagRepository.getTagWithTagName(tag.getTagName()).isEmpty()){
            tagRepository.save(tag);
        }else {
            throw new CouldNotGetTagException("The tag with id and name " + tag.getTagID() + " " + tag.getTagName() + "could not be found.");
        }
    }

    @Override
    public List<Tag> getAllTags() {
        List<Tag> tagList = new ArrayList<>();
        tagRepository.findAll().forEach(tagList::add);
        return tagList;
    }

    /**
     * Checks if the tag id is above zero.
     * @param tagID the tag id to check.
     */
    private void checkIfTagIDIsAboveZero(long tagID){
        checkIfNumberIsAboveZero(tagID, "tag id");
    }

    /**
     * Checks if the tag with this id is already in the register.
     * @param tagID the tag id to check for.
     * @return <code>true</code> if the tag is in the register.
     *         <code>false</code> if the tag is not in the regsiter.
     */
    private boolean checkIfTagIsInRegister(long tagID){
        return tagRepository.existsById(tagID);
    }

    /**
     * Checks if the tag is not null.
     * @param tag the tag to check.
     */
    private void checkIfTagIsValid(Tag tag){
        checkIfObjectIsNull(tag, "tag");
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
