package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Has all the tests for the tag class.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class TagTests {

    private Tag testTag;

    private StringBuilder stringBuilder;

    private int errors;

    @BeforeEach
    public void makeTestTag(){
        try {
            this.testTag = new Tag(1, "Milk", "The substance that we are milking the cows for.");
        }catch (IllegalArgumentException exception){
            fail("Expected the test tag to be made since the input is valid.");
        }
        //Over kan du se eksempelet jeg mente. Ignorer dette under. Det som er under er ikke et MUST men gjør tetsingen mindre smertefull
        //for min del.
        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Adds a new error to the stringbuilder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(error);
        errors++;
    }

    /**
     * Checks if the tests failed and displays the results.
     */
    private void checkIfTestsFailedAndDisplayResult(){
        if(stringBuilder.length() == 0){
            assertTrue(true);
        }else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }

    /**
     * Tests if the constructor works with invalid parameters.
     */
    @DisplayName("Tests if the constructor works with invalid parameters.")
    @Test
    public void testIfTagConstructorWorksWithInvalidParameters(){
        String errorPrefix = "Excepted to get a IllegalArgumentException ";
        try {
            Tag tag = new Tag(0, "hei", "nei");
            addError(errorPrefix, "since tag id is zero.");
        }catch (IllegalArgumentException exception){}
        try{
            Tag tag = new Tag(1, "", "Hello");
            addError(errorPrefix, "since the tag name is invalid.");
        }catch (IllegalArgumentException exception){}
        try {
            Tag tag = new Tag(1, null, "Hello");
            addError(errorPrefix, "the tag name is null");
        }catch (IllegalArgumentException exception){};
        try{
            Tag tag = new Tag(1, "Hei", "");
            addError(errorPrefix, "since the tag description is invalid.");
        }catch (IllegalArgumentException exception){}
        try {
            Tag tag = new Tag(1, "hei", null);
            addError(errorPrefix, "since the tag description is invalid");
        }catch (IllegalArgumentException exception){}

        //Checks if there are any errors and displays the result.
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if constructor works with valid input.
     */
    @Test
    @DisplayName("Tests if constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        String errorPrefix = "Expected the tag to be made since ";
        try {
            Tag tag = new Tag(1, "hei", "faen");
        }catch (IllegalArgumentException exception){
            addError(errorPrefix,"the input is valid.");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setTagName works with invalid input.
     */
    @DisplayName("Tests if setTagName works with invalid input.")
    @Test
    public void testIfSetTagNameWorksWithInvalidParameters(){
        String errorPrefix = "Expected to get a IllegalArgumentException ";
        try {
            testTag.setTagName("");
            addError(errorPrefix, "since the string is empty.");
        }catch (IllegalArgumentException exception){}
        try{
            testTag.setTagName(null);
            addError(errorPrefix, "since the input is null.");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setTagName works with valid input.
     */
    @DisplayName("Tests if setTagName works with valid input.")
    @Test
    public void testIfSetNameWorksWithValidInput(){
        String errorPrefix = "Expected the name to be set ";
        String parameter = "Heeeeerrr";
        try {
            testTag.setTagName(parameter);
        }catch (IllegalArgumentException exception){
            addError(errorPrefix,"since the input is valid");
        }
        checkIfTestsFailedAndDisplayResult();
        assertEquals(testTag.getTagName(), parameter);
    }

    /**
     * Tests if setDescription works with invalid input.
     */
    @DisplayName("Tests if setDescription works with invalid input.")
    @Test
    public void testIfSetDescriptionWorksWithInvalidInput(){
        String errorPrefix = "Expected to get a IllegalArgumentException ";
        try {
            testTag.setTagDescription("");
            addError(errorPrefix, "since the input is empty.");
        }catch (IllegalArgumentException exception){}
        try {
            testTag.setTagDescription(null);
            addError(errorPrefix, "since the input is null");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setDescription works with valid input
     */
    @DisplayName("Tests if setDescription works with valid input")
    @Test
    public void testIfSetDescriptionWorksWithValidInput(){
        String errorPrefix = "Expected the description to be changed ";
        String parameter = "hei";
        try {
            testTag.setTagDescription(parameter);
        }catch (IllegalArgumentException exception){
            addError(errorPrefix, "since the input is valid.");
        }
        checkIfTestsFailedAndDisplayResult();
        assertEquals(testTag.getTagDescription(), parameter);
    }
}
