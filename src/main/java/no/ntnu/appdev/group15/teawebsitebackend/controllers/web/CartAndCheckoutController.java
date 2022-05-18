package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class CartAndCheckoutController {

    /**
     * Makes an instance of the CartAndCheckoutController class.
     */
    public CartAndCheckoutController() {

    }


    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getCart(Model model){
        return "cart";
    }

    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
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
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
