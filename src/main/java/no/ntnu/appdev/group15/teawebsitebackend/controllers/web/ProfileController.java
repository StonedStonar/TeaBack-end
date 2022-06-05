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

    /**
     * Returns the register a new user page.
     * @param model the model.
     * @param httpSession the HTTP session for data.
     * @param authentication the authentication.
     * @return the register user page URL.
     */
    @GetMapping("/registerUser")
    public String registerUser(Model model, HttpSession httpSession, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        addAllAttributes(model, httpSession);
        return "registerPage";
    }

    /**
     * Makes a new user and directs the page to login.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param eMail the email of the user.
     * @param phoneNumber the phone number of the user.
     * @param password the password of the user.
     * @param secondPassword the second password of the user.
     * @param httpSession the HTTP session to store data.
     * @return redirects the page to login.
     */
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
                parameterBuilder.setPageName("login");
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

    /**
     * Updates the profile of a user and then redirects it to the edit profile page.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param eMail the email of the user.
     * @param phoneNumber the phone number.
     * @param authentication the authentication of the user.
     * @return redirects the window to the editProfile page.
     */
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

    /**
     * Updates the password of the user.
     * @param password the new password of the user.
     * @param oldPassword the old password of the user.
     * @param secondPassword the verification of the password.
     * @param authentication the authentication.
     * @return a redirect to the editProfile page.
     */
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

    /**
     * Updates the address of the user.
     * @param streetName the street name.
     * @param houseNumber the house number.
     * @param postalCode the postal code.
     * @param postalPlace the postal place.
     * @param country the country.
     * @param authentication the authentication.
     * @return redirects the page to editProfile page.
     */
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

    /**
     * Gets the page where the former orders of a user is.
     * @param model the model.
     * @param authentication the authentication.
     * @return the former orders HTML name.
     */
    @GetMapping("/formerOrders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getFormerOrders(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        List<Order> orders = orderRegister.getAllOrdersOfUser(user.getUserId());
        model.addAttribute("orders", orders);
        return "formerOrder";
    }

    /**
     * Gets the profile page of a user.
     * @param model the model.
     * @param authentication the authentication.
     * @return the profile page of the user.
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getUserPage(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        addUserToModel(model, user);
        addAddressToModel(model, user);

        return "profile";
    }

    /**
     * Gets the login page.
     * @param authentication the authentication.
     * @param model the model.
     * @return the login page.
     */
    @GetMapping("/login")
    public String getLoginPage(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);
        return "login";
    }

    /**
     * Gets the edit profile page.
     * @param model the model.
     * @param authentication the authentication.
     * @param httpSession the HTTP session in case data needs to be transfered.
     * @return the HTML name of editProfile page.
     */
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
