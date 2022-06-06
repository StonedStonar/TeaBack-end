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
import org.springframework.web.bind.annotation.*;

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

    /**
     * Makes an instance of the UserController class.
     * @param userJPA the service.
     */
    public UserController(UserJPA userJPA) {
        checkIfObjectIsNull(userJPA, "user jpa");
        this.userRegister = userJPA;
    }

    /**
     * Gets all the users in the system.
     * @return a list with all the users.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        return userRegister.getAllUsers();
    }

    /**
     * Gets the user with the specified ID.
     * @param userID the user ID.
     * @return the user matching that ID.
     * @throws CouldNotGetUserException gets thrown if the user is not in the system.
     */
    @GetMapping("/{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserWithID(@PathVariable("userID") long userID) throws CouldNotGetUserException {
        return userRegister.getUserWithUserID(userID);
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

    /**
     * Updates the address of the user.
     * @param body the new address as JSOn.
     * @param authentication the authentication.
     * @throws JsonProcessingException gets thrown if the json object is invalid format.
     * @throws CouldNotGetUserException gets thrown if the user could not be found.
     */
    @PutMapping("/address")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void updateUserAddress(@RequestBody String body, Authentication authentication) throws JsonProcessingException, CouldNotGetUserException {
        Address address = makeAddress(body);
        AccessUser accessUser = getAccessUser(authentication);
        User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        Address addressToEdit = user.getAddress();
        updateAddress(addressToEdit, address);
        userRegister.updateUser(user);
        accessUser.setUser(user);
    }

    /**
     * Gets the user object that is logged in.
     * @param authentication the authentication.
     * @return the user that is logged in.
     * @throws CouldNotGetUserException gets thrown if the user could not be found.
     */
    @GetMapping("/myUser")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User getLoggedInUser(Authentication authentication) throws CouldNotGetUserException {
        AccessUser accessUser = getAccessUser(authentication);
        User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        return user;
    }

    /**
     * Gets the cart of the loggedin user.
     * @param authentication the authentication.
     * @return the cart.
     * @throws CouldNotGetUserException gets thrown if the user could not be found.
     */
    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER')")
    public Cart getCart(Authentication authentication) throws CouldNotGetUserException {
        AccessUser accessUser = getAccessUser(authentication);
        User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        return user.getCart();
    }

    /**
     * Updates the logged in users details.
     * @param authentication the authentication.
     * @param body the JSON object.
     * @throws JsonProcessingException gets thrown if the JSON is invalid format.
     * @throws CouldNotGetUserException gets thrown if the user could not be located.
     */
    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void updateLoggedInUser(Authentication authentication, @RequestBody String body) throws JsonProcessingException, CouldNotGetUserException {
        User user = makeUser(body);
        AccessUser accessUser = getAccessUser(authentication);
        User userToUpdate = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
        updateAddress(userToUpdate.getAddress(), user.getAddress());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setFirstName(user.getFirstName());
        userRegister.updateUser(userToUpdate);
        accessUser.setUser(userToUpdate);
    }

    /**
     * Updates the current cart of the logged in user.
     * @param productMap the map with all the parameters.
     * @param authentication the authentication.
     * @throws CouldNotGetUserException gets thrown if the user could not be found in the DB.
     * @throws CouldNotRemoveCartProductException gets thrown if a product could not be removed in the DB.
     */
    @PutMapping("/cart")
    @PreAuthorize("hasRole('USER')")
    public void updateCart(@RequestParam Map<String, String> productMap, Authentication authentication) throws CouldNotGetUserException, CouldNotRemoveCartProductException {
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
     * Updates the old address to the same values as the new.
     * @param oldAddress the old address.
     * @param newAddress the new address.
     */
    private void updateAddress(Address oldAddress, Address newAddress){
        oldAddress.setCountry(newAddress.getCountry());
        oldAddress.setHouseNumber(newAddress.getHouseNumber());
        oldAddress.setPostalCode(newAddress.getPostalCode());
        oldAddress.setPostalPlace(newAddress.getPostalPlace());
        oldAddress.setStreetName(newAddress.getStreetName());
    }

    /**
     * Gets the access user that is using the page.
     * @param authentication the authentication object.
     * @return the access user of this session.
     */
    private AccessUser getAccessUser(Authentication authentication) {
        return (AccessUser) authentication.getPrincipal();
    }

    /**
     * Handles the CouldNotRemoveUserException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotRemoveUserException.class)
    public ResponseEntity<String> handleCouldNotRemoveUserException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Handles the CouldNotAddUserException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotAddUserException.class)
    public ResponseEntity<String> handleCouldNotAddUserException(Exception exception){
        return ResponseEntity.status(HttpStatus.IM_USED).body(exception.getMessage());
    }

    /**
     * Handles the CouldNotGetUserException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotGetUserException.class)
    public ResponseEntity<String> handleCouldNotGetUserException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Handles the CouldNotRemoveCartProductException exception.
     * @param exception the exception.
     * @return the response entity.
     */
    @ExceptionHandler(CouldNotRemoveCartProductException.class)
    public ResponseEntity<String> handleColdNotRemoveCartProductException(Exception exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
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
