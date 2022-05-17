package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.controllers.rest.UserController;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class ProfileController {

    private UserController userController;

    /**
     * Makes an instance of the LoginController class.
     */
    public ProfileController(UserController userController) {
        this.userController = userController;
    }

    @PostMapping("/user")
    public String updateProfile(@RequestParam Map<String, String> map){
        System.err.println("Hei");

        return "redirect:/profile?error=true";
    }

//    @PostMapping("/address")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public String updateAddress(@RequestParam("streetName") String streetName, @RequestParam("houseNumber") int houseNumber,
//                                @RequestParam("postalCode") int postalCode, @RequestParam("postalPlace") String postalPlace,
//                                @RequestParam("country") String country, BindingResult bindingResult){
//        System.err.println(streetName + houseNumber);
//        bindingResult.addError(new ObjectError("streetname", "luli"));
//        return "errors/profile";
//        //return "redirect:/profile";
//    }

    @PostMapping("/address")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String updateAddress(@RequestParam Map<String, String> map, Authentication authentication){
        System.err.println("Hei");

        return "errors/profile";
        //return "redirect:/profile";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getUserPage(Model model, Authentication authentication){
        return getProfilePage(authentication, model);
    }

    private String getProfilePage(Authentication authentication, Model model){
        User user = getUser(authentication);
        if (authentication != null){
            authentication.getAuthorities().forEach(System.err::println);
        }
        addUserToModel(model, user);
        addAddressToModel(model, user);

        return "profile";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/editProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getEditProfilePage(Model model, Authentication authentication){
        User user = getUser(authentication);
        addUserToModel(model, user);
        addAddressToModel(model, user);
        return "editProfile";
    }

    /**
     * Adds the user to the input model.
     * @param model the model to add user to.
     * @param user the user that is logged in.
     */
    private void addUserToModel(Model model, User user){
        model.addAttribute("user", user);

    }

    /**
     * Adds the address object to the model.
     * @param model the model to add to.
     * @param user the user that is logged in.
     */
    private void addAddressToModel(Model model, User user){
        model.addAttribute("address", user.getAddress());
    }

    private User getUser(Authentication authentication){
        AccessUser accessUser = (AccessUser) authentication.getPrincipal();
        return accessUser.getUser();
    }
    /**
     * Checks if a string is of a valid format or not.
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
