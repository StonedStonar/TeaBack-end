package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * @param tagJPA the tag repository to access the DB.
     */
    public TagController(TagJPA tagJPA) {
        tagsRegister = tagJPA;
    }

    /**
     * Gets all the tags in the system.
     * @return a list with all the tags.
     */
    @GetMapping
    public List<Tag> getAllTags(){
        return tagsRegister.getAllTags();
    }

    /**
     * Gets a tag with a specified ID.
     * @param id the id of the tag.
     * @return the tag with this id.
     * @throws CouldNotGetTagException gets thrown if the tag could not be found.
     */
    @GetMapping("/{id}")
    public Tag getTagWithID(@PathVariable Long id) throws CouldNotGetTagException {
        return tagsRegister.getTagWithID(id);
    }

    /**
     * Adds a tag to the system.
     * @param body the tag as a JSON.
     * @throws JsonProcessingException gets thrown if the format on the JSON is invalid.
     * @throws CouldNotAddTagException gets thrown if the tag could not be added.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addTagWithDetails(@RequestBody String body) throws JsonProcessingException, CouldNotAddTagException {
        tagsRegister.addTag(getTag(body));
    }

    /**
     * Deletes a tag from the service.
     * @param id the id of the tag.
     * @throws CouldNotRemoveTagException gets thrown if the tag could not be removed.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(@PathVariable Long id) throws CouldNotRemoveTagException {
        tagsRegister.removeTagWithTagId(id);
    }

    /**
     * Makes it possible to update tags that are in the system.
     * @param body the body of the request.
     * @throws JsonProcessingException gets thrown if the format is invalid.
     * @throws CouldNotGetTagException gets thrown if the tag could not be located.
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTag(@RequestBody String body) throws JsonProcessingException, CouldNotGetTagException {
        tagsRegister.updateTag(getTag(body));
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

    /**
     * Handles the CouldNotRemoveTagException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotRemoveTagException.class)
    private ResponseEntity<String> handleCouldNotRemoveTagException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Handles the CouldNotAddTagException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotAddTagException.class)
    private ResponseEntity<String> handleCouldNotAddTagException(Exception exception){
        return ResponseEntity.status(HttpStatus.IM_USED).body(exception.getMessage());
    }

    /**
     * Handles the CouldNotGetTagException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotGetTagException.class)
    private ResponseEntity<String> handleCouldNotGetTagException(Exception exception){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(exception.getMessage());
    }
}
