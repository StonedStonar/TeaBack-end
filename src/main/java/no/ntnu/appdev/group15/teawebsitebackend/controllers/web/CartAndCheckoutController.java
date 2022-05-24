package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class CartAndCheckoutController {

    private OrderRegister orderRegister;

    /**
     * Makes an instance of the CartAndCheckoutController class.
     */
    public CartAndCheckoutController(OrderJPA orderJPA) {
        this.orderRegister = orderJPA;
    }


    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getCart(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        model.addAttribute("cart", user.getCart());
        return "cart";
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
     * Adds a logged in attribute to the model.
     * @param authentication the authentication.
     * @param model the model.
     */
    private void addLoggedInAttributes(Authentication authentication, Model model){
        boolean loggedIn = authentication != null;
        model.addAttribute("loggedIn", loggedIn);
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
