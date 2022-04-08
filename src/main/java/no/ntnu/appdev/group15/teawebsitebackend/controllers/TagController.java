package no.ntnu.appdev.group15.teawebsitebackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.appdev.group15.teawebsitebackend.RegisterTestData;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.TagRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@RestController
@CrossOrigin
@RequestMapping("/tags")
public class TagController {

    private TagsRegister tagsRegister;

    /**
     * Makes an instance of the TagController class.
     * @param tagRepository the tag repository to access the DB.
     */
    public TagController(TagRepository tagRepository) {
        tagsRegister = new TagJPA(tagRepository);
        System.out.println("Ello");
        try {
            RegisterTestData.addTestTags(tagsRegister);
        }catch (CouldNotAddTagException couldNotAddTagException){
            System.err.println("Could not add test tags to register.");
        }
    }

    @GetMapping
    public List<Tag> getAllTags(){
        return tagsRegister.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag getTagWithID(@PathVariable Long id) throws CouldNotGetTagException {
        return tagsRegister.getTagWithID(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void addTagWithDetails(@RequestBody String body) throws JsonProcessingException, CouldNotAddTagException {
        tagsRegister.addTag(getTag(body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(@PathVariable Long id) throws CouldNotRemoveTagException {
        tagsRegister.removeTagWithTagId(id);
    }

    /**
     * Gets the tag from the body.
     * @param body the body of the html tag.
     * @return the tag that this json object is.
     * @throws JsonProcessingException gets thrown if the json format is invalid.
     */
    private Tag getTag(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, Tag.class);
    }

    @ExceptionHandler(CouldNotRemoveTagException.class)
    private ResponseEntity<String> handleCouldNotRemoveTagException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(CouldNotAddTagException.class)
    private ResponseEntity<String> handleCouldNotAddTagException(Exception exception){
        return ResponseEntity.status(HttpStatus.IM_USED).body(exception.getMessage());
    }

    @ExceptionHandler(CouldNotGetTagException.class)
    private ResponseEntity<String> handleCouldNotGetTagException(Exception exception){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(exception.getMessage());
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
