package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotChangePasswordException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class ProfileController {

    private UserRegister userRegister;

    private OrderRegister orderRegister;

    /**
     * Makes an instance of the LoginController class.
     * @param userJPA the user JPA.
     * @param orderJPA the order JPA.
     */
    public ProfileController(UserJPA userJPA, OrderJPA orderJPA) {
        this.userRegister = userJPA;
        this.orderRegister = orderJPA;
    }


    @GetMapping("/registerUser")
    public String registerUser(Model model, HttpSession httpSession){
        addAllAttributes(model, httpSession);
        return "registerPage";
    }

    @PostMapping("/makeUser")
    public RedirectView makeUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                 @RequestParam("eMail") String eMail, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("password") String password,
                                 @RequestParam("secondPassword") String secondPassword, HttpSession httpSession){
        ParameterBuilder parameterBuilder = new ParameterBuilder("registerUser");
        try {
            checkString(password, "password");
            checkString(secondPassword, "second password");
            if (secondPassword.equals(password)) {
                User user = new User(firstName, lastName, eMail, Long.parseLong(phoneNumber), password);
                userRegister.addUser(user);
            }else {
                parameterBuilder.addParameter("passwordMismatch", "true");
            }
        }catch (CouldNotAddUserException exception){
            parameterBuilder.addParameter("invalidEmail", "true");
        }catch (IllegalArgumentException exception){
            parameterBuilder.addParameter("invalidFields", "true");
        }
        if (!parameterBuilder.isFirstAppend()){
            httpSession.setAttribute("firstName", firstName);
            httpSession.setAttribute("lastName", lastName);
            httpSession.setAttribute("phoneNumber", phoneNumber);
            httpSession.setAttribute("eMail", eMail);
        }else {
            parameterBuilder.addParameter("userMade", "true");
        }
        return new RedirectView(parameterBuilder.buildString(), true);
    }

    @PutMapping("/user")
    public RedirectView updateProfile(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                @RequestParam("eMail") String eMail, @RequestParam("phoneNumber") long phoneNumber,
                                Authentication authentication){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProfile");
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        try {
            checkString(firstName, "first name");
            checkString(lastName, "last name");
            checkString(eMail, "email");
            checkIfLongIsAboveZero(phoneNumber, "phone number");
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(eMail);
            user.setPhoneNumber(phoneNumber);
            userRegister.updateUser(user);
            parameterBuilder.addParameter("changedDetails", "true");
        }catch (IllegalArgumentException exception){
            parameterBuilder.addParameter("invalidField", "true");
        } catch (CouldNotGetUserException e) {
            parameterBuilder.addParameter("criticalError", "true");
        }

        return new RedirectView(parameterBuilder.buildString(), true);
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

    @PutMapping("/password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RedirectView updatePassword(@RequestParam("password") String password, @RequestParam("oldPassword") String oldPassword,
                                       @RequestParam("secondPassword") String secondPassword, Authentication authentication){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProfile");
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        try {
            checkString(password, "password");
            checkString(secondPassword, "second password");
            if (password.equals(secondPassword)){
                user.changePassword(oldPassword, password);
                userRegister.updateUser(user);
                parameterBuilder.addParameter("passwordChanged", "true");
            }else {
                parameterBuilder.addParameter("passwordMisMatch", "true");
            }
        } catch (CouldNotChangePasswordException exception) {
            parameterBuilder.addParameter("oldPasswordMisMatch", "true");
        } catch (IllegalArgumentException exception){
            parameterBuilder.addParameter("invalidPasswordInput", "true");
        } catch (CouldNotGetUserException exception) {
            parameterBuilder.addParameter("fatalPasswordError", "true");
        }
        return new RedirectView(parameterBuilder.buildString(), true);
    }

    @PutMapping("/address")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RedirectView updateAddress(@RequestParam("streetName") String streetName, @RequestParam("houseNumber") int houseNumber,
                                @RequestParam("postalCode") int postalCode, @RequestParam("postalPlace") String postalPlace,
                                @RequestParam("country") String country, Authentication authentication){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProfile");
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        try {
            Address address = new Address(postalCode, postalPlace, streetName, houseNumber, country);
            user.setAddress(address);
            userRegister.updateUser(user);
            parameterBuilder.addParameter("changedAddress", "true");
        }catch (IllegalArgumentException exception){
            parameterBuilder.addParameter("invalidAddressInput", "true");
        }catch (CouldNotGetUserException exception){
            parameterBuilder.addParameter("fatalAddressError", "true");
        }

        return new RedirectView(parameterBuilder.buildString(), true);
    }

    @GetMapping("/formerOrders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getFormerOrders(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        List<Order> orders = orderRegister.getAllOrdersOfUser(user.getUserId());
        model.addAttribute("orders", orders);
        return "formerOrder";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getUserPage(Model model, Authentication authentication){
        return getProfilePage(authentication, model);
    }

    private String getProfilePage(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        addUserToModel(model, user);
        addAddressToModel(model, user);

        return "profile";
    }


    @GetMapping("/login")
    public String getLoginPage(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);
        return "login";
    }

    @GetMapping("/editProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getEditProfilePage(Model model, Authentication authentication, HttpSession httpSession){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        addAllAttributes(model, httpSession);
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
     * Adds all the attributes to the model.
     * @param model the model.
     * @param httpSession the http session.
     */
    private void addAllAttributes(Model model, HttpSession httpSession){
        Iterator<String> it = httpSession.getAttributeNames().asIterator();
        while (it.hasNext()){
            String attributeName = it.next();
            model.addAttribute(attributeName, httpSession.getAttribute(attributeName));
            httpSession.removeAttribute(attributeName);
        }
    }

    /**
     * Checks if the email is not an invalid value. Also checks that the email contains "@" and "."
     * @param email the email to check.
     * @throws IllegalArgumentException gets thrown if the email is invalid format.
     */
    private void checkIfEmailIsNotInvalid(String email){
        checkString(email, "email");
        String reg = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(reg);
        if (!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("The email must be the correct format.");
        }
    }

    /**
     * Checks if the input long is above zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     * @throws IllegalArgumentException gets thrown if the number is below or equal to zero.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be above zero.");
        }
    }
}
