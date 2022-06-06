package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a basic web controller with methods that is inherited.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebController {

    /**
     * Makes an instance of the WebController class.
     */
    public WebController() {

    }

    /**
     * Adds a logged in attribute to the model.
     * @param authentication the authentication.
     * @param model the model.
     */
    protected void addLoggedInAttributes(Authentication authentication, Model model){
        boolean loggedIn = authentication != null;
        boolean admin = false;
        model.addAttribute("loggedIn", loggedIn);
        if (loggedIn){
            admin = getAccessUser(authentication).getUser().getRole() == Role.ROLE_ADMIN;
        }
        model.addAttribute("isAdmin", admin);
    }

    /**
     * Adds all the attributes to the model.
     * @param model the model.
     * @param httpSession the http session.
     */
    protected void addAllAttributes(Model model, HttpSession httpSession){
        Iterator<String> it = httpSession.getAttributeNames().asIterator();
        while (it.hasNext()){
            String attributeName = it.next();
            Object object = httpSession.getAttribute(attributeName);
            if (object instanceof List){
                List list = (List) object;
                object = convertListToValues(list);
            }
            model.addAttribute(attributeName, object);
            httpSession.removeAttribute(attributeName);
        }
    }

    /**
     * Gets the access user that is using the page.
     * @param authentication the authentication object.
     * @return the access user of this session.
     */
    protected AccessUser getAccessUser(Authentication authentication){
        return (AccessUser) authentication.getPrincipal();
    }


    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
     */
    protected void checkString(String stringToCheck, String errorPrefix) {
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
    protected void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    /**
     * Takes in a list and converts it to values with "," between them.
     * @param list the list.
     * @return the string that has all the numbers in it.
     */
    protected String convertListToValues(List list){
        StringBuilder stringBuilder = new StringBuilder();
        if (!list.isEmpty() && list.get(0) instanceof Long){
            List<Long> longs = list;
            for(long number: longs){
                if (!stringBuilder.isEmpty()){
                    stringBuilder.append(",");
                }
                stringBuilder.append(number);

            }
        }
        return stringBuilder.toString();
    }
}
