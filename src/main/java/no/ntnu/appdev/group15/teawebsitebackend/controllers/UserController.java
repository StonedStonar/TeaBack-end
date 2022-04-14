package no.ntnu.appdev.group15.teawebsitebackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.appdev.group15.teawebsitebackend.RegisterTestData;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
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
public class UserController {

    private UserRegister userRegister;

    /**
     * Makes an instance of the UserController class.
     * @param userJPA the service.
     */
    public UserController(UserJPA userJPA) {
        checkIfObjectIsNull(userJPA, "user jpa");
        this.userRegister = userJPA;
        try {
            RegisterTestData.addTestUsers(userJPA);
        }catch (CouldNotAddUserException exception){
            System.err.println("The test users could not be added.");
        }
    }

    /**
     * Gets all the users in the system.
     * @return a list with all the users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        return userRegister.getAllUsers();
    }

    /**
     * Adds a user to the system.
     * @param body the body of the request.
     * @throws JsonProcessingException gets thrown if the format on the json object is invalid.
     * @throws CouldNotAddUserException gets thrown if the user could not be added.
     */
    @PostMapping("/users")
    public void addUser(@RequestBody String body) throws JsonProcessingException, CouldNotAddUserException {
        userRegister.addUser(makeUser(body));
    }

    /**
     * Removes a user from the system.
     * @param id the id of the user.
     * @throws CouldNotRemoveUserException gets thrown if the user could not be removed.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void removeUser(@PathVariable Long id) throws CouldNotRemoveUserException {
        userRegister.removeUserWithID(id);
    }

    /**
     * Makes a new user.
     * @param body the json object.
     * @return the user from the json object.
     * @throws JsonProcessingException if the json is not the right format.
     */
    private User makeUser(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, User.class);
    }

    /**
     * Shows a page where you need to be logged in to see.
     * @param id the id that should be shown on the page.
     * @return string that is shown in the browser.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String userPage(@PathVariable Long id){
        return "You just wrote " + id + ".";
    }

    /**
     * The normal page where no-one needs to log in to see.
     * @return a string that is displayed.
     */
    @GetMapping("")
    public String normal(){
        return "Page without any permissions needed.";
    }

    /**
     * Shows a page where users with "USER" role can see.
     * @return a string that is displayed.
     */
    @GetMapping("user")
    public String userPage(){
        return "This is the user page.";
    }

    /**
     * Shows a page where users with "ADMIN" role can see.
     * @return a string that is displayed.
     */
    @GetMapping("admin")
    public String adminPage(){
        return "This is the admin page.";
    }


    @ExceptionHandler(CouldNotRemoveUserException.class)
    private ResponseEntity<String> handleCouldNotRemoveUserException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(CouldNotAddUserException.class)
    private ResponseEntity<String> handleCouldNotAddUserException(Exception exception){
        return ResponseEntity.status(HttpStatus.IM_USED).body(exception.getMessage());
    }

    /**
     * Checks if an object is null.
     *
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
