package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderedProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveProductException;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order controller class. User must be logged in to see orders.
 * Orders can be removed and added.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController {

  private OrderRegister orderRegister;

  /**
   * Makes an instance of the OrderController class.
   * @param orderJPA
   */
  public OrderController (OrderJPA orderJPA){
    checkIfObjectIsNull(orderJPA, "orderJPA");
    this.orderRegister = orderJPA;
  }

  /**
   * gets all the orders made by a user.
   * @return a list with all the orders made by a user.
   */
  @GetMapping
  public List<Order> getALlOrders(OrderedProduct orderedProduct) {
    List<Order> orders = orderRegister.getAllOrders();
    return orders;
  }


  /**
   * Gets an order with a specified id. Only by admin
   */
  //@PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public Order getOrderWithSpecifiedId(@PathVariable Long id) throws CouldNotGetOrderException {
    Order order = orderRegister.getOrderWithId(id);
    return order;
  }


//  /**
//   * Adds an order to a user.
//   * @param orderedProduct the order to be added
//   * @param id the id of the user to add the order to
//   *           TODO this one
//   */
//  @GetMapping
//  public void addOrderToUser(OrderedProduct orderedProduct, Long id) {
//
//  }


  /**
   * Removes an order from the system. Only by admin.
   * @param id the id of the order to remove
   */
  //@PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public void removeOrder(@PathVariable Long id)
      throws CouldNotGetOrderException, CouldNotRemoveOrderException {
    orderRegister.removeOrder(getOrderWithSpecifiedId(id));
  }



//  /**
//   * Edit an order. Only possible for admin-users.
//   * TODO this motherfucker
//   */
//  //@PreAuthorize("hasRole('ADMIN')")
//  @GetMapping
//  public String getEditOrder(Model model, Authentication authentication, HttpSession httpSession){
//    addLoggedInAttributes(authentication, model);
//    //Order order = getAccessUser(authentication).getOrder();
//
//    return "editOrder";
//  }




  @ExceptionHandler(CouldNotRemoveProductException.class)
  private ResponseEntity<String> handleCouldNotRemoveProductException(Exception ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(CouldNotAddProductException.class)
  private ResponseEntity<String> handleCouldNotAddProductException(Exception ex) {
    return ResponseEntity.status(HttpStatus.IM_USED).body(ex.getMessage());
  }

  @ExceptionHandler(CouldNotGetProductException.class)
  private ResponseEntity<String> handleCouldNotGetProductException(Exception ex) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  private ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
  }

  /**
   * Adds a logged in attribute to the model.
   * @param authentication the authentication.
   * @param model the model.
   */
  private void addLoggedInAttributes(Authentication authentication, Model model){
    boolean loggedIn = authentication != null;
    boolean admin = false;
    model.addAttribute("loggedIn", loggedIn);
    if (loggedIn){
      admin = getAccessUser(authentication).getUser().getRole() == Role.ROLE_ADMIN;
    }
    model.addAttribute("isAdmin", admin);
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
   * Checks if an object is null.
   * @param object the object you want to check.
   * @param error  the error message the exception should have.
   * @throws IllegalArgumentException gets thrown if the object is null.
   */
  private void checkIfObjectIsNull(Object object, String error) {
    if (object == null) {
      throw new IllegalArgumentException("The " + error + " cannot be null.");
    }
  }

  /**
   * Checks if a string is of a valid format or not.
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

}
