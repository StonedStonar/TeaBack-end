package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderedProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents the oorder controller class, which is needed to connect order(s) to the database.
 * User must be logged in to see orders.
 *
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
   *
   * @param orderJPA
   */
  public OrderController(OrderJPA orderJPA) {
      checkIfObjectIsNull(orderJPA, "orderJPA");
      this.orderRegister = orderJPA;
  }

  /**
   * gets all the orders made by a user.
   *
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

  /**
   * Makes new order.
   *
   * @param body the body of the HTML-document.
   */
  private Order makeOrder(String body) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(body, Order.class);
  }


  /**
   * Adds an order to a user.
   *
   * @param body the json object
   */
  @PostMapping
  public void addOrderToUser(@RequestBody String body) throws JsonProcessingException, CouldNotAddOrderException {
    Order order = makeOrder(body);
    orderRegister.addOrder(order);
  }


  /**
   * Removes an order from the system. Only by admin.
   *
   * @param id the id of the order to remove
   */
  //@PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public void removeOrder(@PathVariable Long id)
      throws CouldNotGetOrderException, CouldNotRemoveOrderException {
    orderRegister.removeOrder(getOrderWithSpecifiedId(id));
  }


  /**
   * Edit an order. Only possible for admin-users.
   *
   * @param body the json object
   * @throws CouldNotGetOrderException gets thrown if the order could not be edited.
   */
  @PutMapping
  //@PreAuthorize("hasRole('ADMIN')")
  public void editOrder(String body) throws CouldNotGetOrderException, JsonProcessingException {
    orderRegister.updateOrder(makeOrder(body));
  }


  /**
   * Gets the access-user that is using the page.
   *
   * @param authentication the authentication object.
   * @return the access-user of this session.
   */
  private AccessUser getAccessUser(Authentication authentication) {
    return (AccessUser) authentication.getPrincipal();
  }


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
