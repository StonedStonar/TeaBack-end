package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@RestController
@RequestMapping("/users")
public class UserController {


    private final UserRegister userRegister;

    private final ProductRegister productRegister;

    private final OrderRegister orderRegister;

    /**
     * Makes an instance of the UserController class.
     * @param userJPA the service.
     * @param productJPA the product JPA.
     */
    public UserController(UserJPA userJPA, ProductJPA productJPA, OrderJPA orderJPA) {
        checkIfObjectIsNull(userJPA, "user jpa");
        this.userRegister = userJPA;
        this.productRegister = productJPA;
        this.orderRegister = orderJPA;
    }

    /**
     * Gets all the users in the system.
     * @return a list with all the users.
     */
    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        return userRegister.getAllUsers();
    }

    /**
     * Adds a user to the system.
     * @param body the body of the request.
     * @throws JsonProcessingException gets thrown if the format on the json object is invalid.
     * @throws CouldNotAddUserException gets thrown if the user could not be added.
     */
    @PostMapping
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


    @PostMapping("/address")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void updateUser(@RequestBody String body, Authentication authentication) throws JsonProcessingException, CouldNotGetUserException {
        Address address = makeAddress(body);
        AccessUser accessUser = getAccessUser(authentication);
        User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        Address addressToEdit = user.getAddress();
        addressToEdit.setCountry(address.getCountry());
        addressToEdit.setHouseNumber(address.getHouseNumber());
        addressToEdit.setPostalCode(address.getPostalCode());
        addressToEdit.setPostalPlace(address.getPostalPlace());
        addressToEdit.setStreetName(address.getStreetName());
        userRegister.updateUser(user);
        accessUser.setUser(user);
    }

    @GetMapping("/myUser")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User getLoggedInUser(Authentication authentication) throws CouldNotGetUserException {
        AccessUser accessUser = getAccessUser(authentication);
        User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        return user;
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void updateUser(Authentication authentication, @RequestBody String body) throws JsonProcessingException, CouldNotGetUserException {
        User user = makeUser(body);
        AccessUser accessUser = getAccessUser(authentication);
        if (user.getUserId() == accessUser.getUser().getUserId()){
            userRegister.updateUser(user);
            accessUser.setUser(user);
        }else {
            throw new CouldNotGetUserException("The user ID of the input JSON body does not match the set ID of the user");
        }
    }


    @PutMapping("/cart")
    @PreAuthorize("hasRole('USER')")
    public void updateCart(@RequestParam Map<String, String> productMap, Authentication authentication) throws CouldNotGetUserException, CouldNotRemoveCartProductException {
        System.out.print("Pepegaclap");
        AccessUser accessUser = getAccessUser(authentication);
        User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        Cart cart = user.getCart();
        List<CartProduct> cartProducts = cart.getAllProducts().stream().filter(prod -> productMap.containsKey(Long.toString(prod.getProduct().getProductID()))).toList();
        for (CartProduct cartProduct : cartProducts){
            int amount = Integer.parseInt(productMap.get(Long.toString(cartProduct.getProduct().getProductID())));
            if (amount == 0){
                cart.removeCartProduct(cartProduct.getProduct());
            }else {
                cartProduct.setAmount(amount);
            }
        }
        userRegister.updateUser(user);
        accessUser.setUser(user);
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
    @GetMapping("/normal")
    public String normal(){
        return "Page without any permissions needed.";
    }

    /**
     * Shows a page where users with "USER" role can see.
     * @return a string that is displayed.
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userPage(){
        return "This is the user page.";
    }

    /**
     * Shows a page where users with "ADMIN" role can see.
     * @return a string that is displayed.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
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
     * Makes a new address.
     * @param body the json object.
     * @return the user from the json object.
     * @throws JsonProcessingException if the json is not the right format.
     */
    private Address makeAddress(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, Address.class);
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
