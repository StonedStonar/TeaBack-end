package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Review;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the review class.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class ReviewTests {

    private User user;

    private int errors;

    private String illegalPrefix;

    private StringBuilder stringBuilder;

    /**
     * Makes the basic reivew test object.
     */
    private ReviewTests(){
        this.illegalPrefix = makeExceptionString("IllegalArgumentException");
    }

    /**
     * Makes an exception into the wanted string.
     * @param exceptionName the name of the exception.
     * @return the full exception string.
     */
    private String makeExceptionString(String exceptionName){
        return "Expected to get a " +  exceptionName + " since";
    }

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    private void setUpTests() {
        try {
            this.user = new User("Lars", lastName, address "lars@testesen.com", "pass", 41560823, Role.ROLE_USER);
        } catch (IllegalArgumentException exception) {
            fail("Expected the test objects to be made since the input is valid.");
        }

        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Makes a predefined review used for testing.
     * @return the review that is made.
     */
    private Review makeReview(){
        return new Review("Test", "Testesen", user, "Hello", LocalDate.now(), 5);
    }

    /**
     * Adds a new error to the stringbuilder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(" ").append(error);
        errors++;
    }

    /**
     * Adds an error with an exception in the title.
     * @param errorPrefix what it should say before the main error.
     * @param error what it should say after the error.
     * @param exception the exception that was not expected.
     */
    private void addErrorWithException(String errorPrefix, String error, Exception exception){
        addError(errorPrefix, error);
        stringBuilder.append(" and not a ").append(exception.getClass().getSimpleName());
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
     * Tests if constructor works with invalid input.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid input.")
    public void testIfConstructorWorksWithInvalidInput(){
        String validTitle = "Title";
        String validName = "name";
        String comment = "comment";
        LocalDate date = LocalDate.now();
        int rating = 3;
        //Review review = new Review(validTitle, validName, user, comment, date, rating);
        try {
            Review review = new Review(null, validName, user, comment, date, rating);
            addError(illegalPrefix, "the input title is null");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review("", validName, user, comment, date, rating);
            addError(illegalPrefix, "the input title is empty");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, null, user, comment, date, rating);
            addError(illegalPrefix, "the input name is null");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, "", user, comment, date, rating);
            addError(illegalPrefix, "the input name is empty");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, null, comment, date, rating);
            addError(illegalPrefix, "the input user is null");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, user, null, date, rating);
            addError(illegalPrefix, "the input comment is null");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, user, "", date, rating);
            addError(illegalPrefix, "the input comment is empty");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, user, comment, null, rating);
            addError(illegalPrefix, "the input date is null");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, user, comment, LocalDate.now().plusDays(1), rating);
            addError(illegalPrefix, "the input date is one day in the future");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, user, comment, date, -2);
            addError(illegalPrefix, "the input rating is -2");
        }catch (IllegalArgumentException exception){}
        try {
            Review review = new Review(validTitle, validName, user, comment, date, 500);
            addError(illegalPrefix, "the input rating is 500");
        }catch (IllegalArgumentException exception){}

        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if constructor works with valid input.
     */
    @Test
    @DisplayName("Tests if constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        try {
            Review review = makeReview();
        }catch (IllegalArgumentException exception){
            addError("Expected the reivew to be made since the input is valid", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

}
