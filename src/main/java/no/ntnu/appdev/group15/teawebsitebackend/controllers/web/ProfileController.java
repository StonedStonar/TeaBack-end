package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
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
public class ProfileController extends WebController{

    private UserRegister userRegister;

    private OrderRegister orderRegister;

    /**
     * Makes an instance of the LoginController class.
     * @param userJPA the user JPA.
     * @param orderJPA the order JPA.
     */
    public ProfileController(UserJPA userJPA, OrderJPA orderJPA) {
        super();
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
