package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.Application;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.TagRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TagJPATests {

    private String prefixIllegal;

    private TagJPA tagJPA;

    private StringBuilder stringBuilder;

    private int errors;

    @Autowired
    public TagJPATests(TagJPA tagJPA){
        this.tagJPA = tagJPA;
    }

    @BeforeEach
    private void addTestData(){
        try {
            tagJPA.getAllTags().forEach(tag -> {
                try {
                    tagJPA.removeTag(tag);
                } catch (CouldNotRemoveTagException e) {
                    e.printStackTrace();
                }
            });
            tagJPA.addTag(new Tag(700, "hei", "hade"));
            tagJPA.addTag(new Tag(800, "Hello", "Its me"));
        } catch (CouldNotAddTagException couldNotAddTagException) {
            fail("Expected the test data to be added before each test.");
        }
        stringBuilder = new StringBuilder();
        errors = 0;
        prefixIllegal = "Expected to get a IllegalArgumentException since ";
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
        if(stringBuilder.isEmpty()){
            assertTrue(true);
        }else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }

    /**
     * Tests if addTag works with invalid input.
     */
    @Test
    public void testIfAddTagWorksWithInvalidInput(){
        String couldException = "Expected to get a CouldNotAddTagException since ";
        try {
            tagJPA.addTag(null);
            addError(prefixIllegal, "the input is null.");
        }catch (IllegalArgumentException exception){

        } catch (CouldNotAddTagException exception){
            addError(prefixIllegal, "the input is null.");
        }
        try {
            tagJPA.addTag(new Tag(6, "hei", "3"));
            addError(couldException, "the input tag has the same id as an old one.");
        }catch (IllegalArgumentException exception){
            addError(couldException, "the input tag has the same id as an old one.");
        }catch (CouldNotAddTagException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if addTag works with valid input.
     */



}
