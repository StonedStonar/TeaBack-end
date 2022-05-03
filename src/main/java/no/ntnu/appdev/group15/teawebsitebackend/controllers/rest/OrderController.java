package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import no.ntnu.appdev.group15.teawebsitebackend.RegisterTestData;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order controller class. User must be logged in to see orders.
 * Orders can be removed and added.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@RestController
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

  /**
   * Gets an order with a specified id. Only by admin
   */

  /**
   * Adds an order to a user.
   * @param
   */

  /**
   * Removes an order from the system. Only by admin.
   */

  /**
   * Makes a new order.
   */

  /**
   * Makes it possible for an admin to edit orders.
   */




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
}
