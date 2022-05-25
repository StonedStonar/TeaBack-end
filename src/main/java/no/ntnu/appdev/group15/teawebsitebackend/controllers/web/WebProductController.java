package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebProductController {

    private ProductRegister productRegister;

    /**
     * Makes an instance of the ProductController class.
     */
    public WebProductController(ProductJPA productJPA) {
        this.productRegister = productJPA;
    }

    @GetMapping("/editProduct")
    public String getEditProduct(){
        return "editProduct";
    }

    /**
     * Gets the access user that is using the page.
     * @param authentication the authentication object.
     * @return the access user of this session.
     */
    private AccessUser getAccessUser(Authentication authentication){
        return (AccessUser) authentication.getPrincipal();
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
