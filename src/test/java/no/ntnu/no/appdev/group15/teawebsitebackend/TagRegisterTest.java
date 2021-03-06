package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.Application;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagRegisterTest {

    private String prefixIllegal;

    private TagsRegister tagsRegister;

    private Tag tag;

    private StringBuilder stringBuilder;

    private int errors;


    @Autowired
    public TagRegisterTest(TagJPA tagsJPA){
        this.tagsRegister = tagsJPA;
        prefixIllegal = "Expected to get a IllegalArgumentException since ";
    }

    @BeforeEach
    private void addTestData(){
        try {
            for (Tag tag1 : tagsRegister.getAllTags()) {
                tagsRegister.removeTag(tag1);
            }
            tagsRegister.addTag(new Tag(700, "hei", "hade"));
            tagsRegister.addTag(new Tag(800, "Hello", "Its me"));
            this.tag = tagsRegister.getAllTags().get(0);
        } catch (CouldNotAddTagException | IllegalArgumentException | CouldNotRemoveTagException couldNotAddTagException) {
            fail("Expected the test data to be added before each test and not a " + couldNotAddTagException.getClass().getSimpleName());
        }
        stringBuilder = new StringBuilder();
        errors = 0;
    }


    /**
     * Cleans up the DB after all the tests.
     */
    @AfterAll
    public void cleanUp(){
        try {
            for (Tag tag : tagsRegister.getAllTags()){
                tagsRegister.removeTag(tag);
            }
        } catch (CouldNotRemoveTagException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Adds a new error to the stringBuilder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(error);
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
     * Tests if addTag works with invalid input.
     */
    @Test
    @DisplayName("Tests if addTag works with invalid input.")
    public void testIfAddTagWorksWithInvalidInput(){
        String couldException = "Expected to get a CouldNotAddTagException since ";
        try {
            tagsRegister.addTag(null);
            addError(prefixIllegal, "the input is null.");
        }catch (IllegalArgumentException exception){

        } catch (CouldNotAddTagException exception){
            addError(prefixIllegal, "the input is null.");
        }
        try {
            tagsRegister.addTag(tag);
            addError(couldException, "the input tag has the same id as an old one.");
        }catch (IllegalArgumentException exception){
            addError(couldException, "the input tag has the same id as an old one.");
        }catch (CouldNotAddTagException exception){

        }
        try{
            tagsRegister.addTag(tag);
            addError(couldException, "since the tag is already in the system.");
        }catch (IllegalArgumentException exception){
            addError(couldException, "since the tag is already in the system.");
        }catch (CouldNotAddTagException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if addTag works with valid input.
     */
    @Test
    @DisplayName("Tests if addTag works with valid input.")
    public void testIfAddTagWorksWithValidInput(){
        try {
            tagsRegister.addTag(new Tag(500, "Test", "Test 2"));
        }catch (CouldNotAddTagException | IllegalArgumentException exception){
            addError("Expected the tag to be added ", "since the input is valid.");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if addTagWithDetails works with invalid input.
     */
    @Test
    @DisplayName("Test if addTagWithDetails works with invalid input.")
    public void testIfAddTagWithDetailsWorksWithInvalidInput(){
        try {
            tagsRegister.addTagWithDetails("", "Hello");
            addError(prefixIllegal, "the tagname is empty.");
        }catch (IllegalArgumentException exception){}
        catch (CouldNotAddTagException exception){
            addError(prefixIllegal, "the tagname is empty.");
        }
        try{
            tagsRegister.addTagWithDetails(null, "Hello");
            addError(prefixIllegal, "the tagname is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotAddTagException exception){
            addError(prefixIllegal, "the tagname is null and not a " + exception.getClass().getSimpleName());
        }
        try{
            tagsRegister.addTagWithDetails("Hello", "");
            addError(prefixIllegal, "the descritpion is empty");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotAddTagException exception){
            addError(prefixIllegal, "the descritpion is empty and not a " + exception.getClass().getSimpleName());
        }
        try{
            tagsRegister.addTagWithDetails("hello", null);
            addError(prefixIllegal, "the description is null");
        }catch (IllegalArgumentException exception){}
        catch (CouldNotAddTagException exception){
            addError(prefixIllegal, "the description is null and did not expect a " + exception.getClass().getSimpleName());
        }

        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if addTagWithDetails works with valid input.
     */
    @Test
    @DisplayName("Test if addTagWithDetails works with valid input.")
    public void testIfAddTagWithDetailsWorksWithValidInput(){
        try {
            tagsRegister.addTagWithDetails("Raiise", "Errras");
        }catch (IllegalArgumentException | CouldNotAddTagException exception){
            addError("Expected the tag to be added since the input is valid ","and not a " + exception.getClass().getSimpleName());
        }
    }

    /**
     * Test if removeTag works with invalid input.
     */
    @Test
    @DisplayName("Test if removeTag works with invalid input.")
    public void testIfRemoveTagWorksWithInvalidInput(){
        try {
            tagsRegister.removeTag(null);
            addError(prefixIllegal, "the input is invalid format.");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotRemoveTagException exception){
            addError(prefixIllegal, "the input is invalid format.");
        }

        try{
            tagsRegister.removeTag(new Tag(700, "Pepe", "clasp"));
            addError("Expected to get a CouldNotRemoveTagException since ", "the input is invalid.");
        }catch (IllegalArgumentException exception){
            addErrorWithException("Expected to get a CouldNotRemoveTagException since ", "the input is invalid.", exception);
        }catch (CouldNotRemoveTagException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeTag works with valid tag.
     */
    @Test
    @DisplayName("Tests if removeTag works with valid tag.")
    public void testIfRemoveTagWorksWithValidInput(){
        try {
            tagsRegister.removeTag(tag);
        } catch (CouldNotRemoveTagException | IllegalArgumentException exception) {
            addErrorWithException("Expected the ", "to be added since the input is valid", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeTagWithID works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeTagWithID works with invalid input.")
    public void testIfRemoveTagWithIDWorksWithInvalidInput(){
        try {
            tagsRegister.removeTagWithTagId(0);
            addError(prefixIllegal, "the input is invalid");
        }catch (IllegalArgumentException exception){
        }catch (CouldNotRemoveTagException exception){
            addErrorWithException(prefixIllegal, "the input is invalid", exception);
        }
        try{
            tagsRegister.removeTagWithTagId(-5);
            addError(prefixIllegal, "the input is negative");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotRemoveTagException exception){
            addErrorWithException(prefixIllegal, "the input is negative", exception);
        }
        try{
            tagsRegister.removeTagWithTagId(60000);
            addError("Expected a CouldNotRemoveTagException since ", "the input tag id is not there");
        }catch (IllegalArgumentException exception){
            addErrorWithException("Expected a CouldNotRemoveTagException since ", "the input tag id is not there", exception);
        }catch (CouldNotRemoveTagException exception){}

        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeTagWithID works with valid input.
     */
    @Test
    @DisplayName("Test if removeTagWithID works with valid input.")
    public void testIfRemoveTagWithIDWorksWithValidInput(){
        try {
            tagsRegister.removeTagWithTagId(tag.getTagID());
        } catch (CouldNotRemoveTagException | IllegalArgumentException exception) {
            addErrorWithException("Expected the tag to be removed since its in the regsiter", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getTagWithId works with invalid input
     */
    @Test
    @DisplayName("Tests if getTagWithId works with invalid input")
    public void testIfGetTagWithIDWorksWithInvalidInput(){
        try {
            tagsRegister.getTagWithID(0);
            addError(prefixIllegal, "the input is 0");
        }catch (IllegalArgumentException exception){}
        catch (CouldNotGetTagException exception){
            addErrorWithException(prefixIllegal, "the input is 0", exception);
        }
        try {
            tagsRegister.getTagWithID(-5);
            addError(prefixIllegal, "the input is negative");
        }catch (IllegalArgumentException exception){}
        catch (CouldNotGetTagException exception){
            addErrorWithException(prefixIllegal, "the input is 0", exception);
        }
        try {
            tagsRegister.getTagWithID(60000);
            addError("Expected to get a CouldNotGetTagException since ", "the id is not in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException("Expected to get a CouldNotGetTagException since ", "the id is not in the system", exception);
        }catch (CouldNotGetTagException exception){}

        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getTagWithId works with valid input
     */
    @Test
    @DisplayName("Tests if getTagWithId works with valid input")
    public void testIfGetTagWithIdWorksWithValidInput(){
        try {
            tagsRegister.getTagWithID(tag.getTagID());

        }catch (IllegalArgumentException | CouldNotGetTagException exception){
            addErrorWithException("Expected to get the tag since its a valid input", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if updateTag works with invalid input.
     */
    @Test
    @DisplayName("Test if updateTag works with invalid input.")
    public void testIfUpdateTagWorksWithInvalidInput(){
        try {
            tagsRegister.updateTag(null);
            addError(prefixIllegal, "since the input is null.");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotGetTagException exception){
            addErrorWithException(prefixIllegal, "since the input is null", exception);
        }
        String getException = "Expected to get a CouldNotGetTagException since ";

        try {
            tagsRegister.updateTag(new Tag(70000, "Hello", "Hello"));
            addError(getException, "the tag is not in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException(getException, "the tag is not in the system", exception);
        }catch (CouldNotGetTagException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if updateTag works with valid input
     */
    @Test
    @DisplayName("Test if updateTag works with valid input")
    public void testIfUpdateTagWorksWithValidInput(){
        try {
            String tagname = "halla";
            tag.setTagName(tagname);
            tagsRegister.updateTag(tag);
            assertEquals(tagsRegister.getTagWithID(tag.getTagID()).getTagName(), tagname);
        }catch (IllegalArgumentException | CouldNotGetTagException exception){
            addErrorWithException("Expected the tag to be updated since the input is valid", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }
}