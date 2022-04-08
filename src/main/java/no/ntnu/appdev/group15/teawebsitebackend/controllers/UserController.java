package no.ntnu.appdev.group15.teawebsitebackend.controllers;

import io.swagger.annotations.Authorization;
import no.ntnu.appdev.group15.teawebsitebackend.RegisterTestData;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@RestController
public class UserController {

    private UserJPA userJPA;

    /**
     * Makes an instance of the UserController class.
     * @param userJPA the service.
     */
    public UserController(UserJPA userJPA) {
        checkIfObjectIsNull(userJPA, "user jpa");
        this.userJPA = userJPA;
        try {
            RegisterTestData.addTestUsers(userJPA);
        }catch (CouldNotAddUserException exception){
            System.err.println("The users could not be added.");
        }
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String userPage(@PathVariable Long id){
        return "You just wrote " + id + ".";
    }

    @GetMapping("")
    public String normal(){
        return "Page without any permissions needed.";
    }

    @GetMapping("user")
    public String userPage(){
        return "This is the user page.";
    }

    @GetMapping("admin")
    public String adminPage(){
        return "This is the admin page.";
    }


    /**
     * Checks if a string is of a valid format or not.
     *
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
