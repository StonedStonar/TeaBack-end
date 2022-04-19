package no.ntnu.no.appdev.group15.teawebsitebackend;

import java.time.LocalDate;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;

import no.ntnu.appdev.group15.teawebsitebackend.model.OrderState;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderedProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests of the teaDetails class.
 * The following tests are performed:
 * <ul>
 *   <li> Successful creation of instance with valid parameters (positive)</li>
 *   <li> Creation of instance with invalid parameters (negative)</li>
 *   <li> Test of setOrderState(), with invalid parameters (negative)</li>
 * </ul>
 * @author Trine Merete Staverløkk
 * @version 0.1
 */

public class OrderTest {
  private Order order;
  private StringBuilder stringBuilder;
  private int errors;

  @BeforeEach
  public void setUpTest(){
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
    } catch (IllegalArgumentException exception){
      fail("Expected the order to be made, since the input it valid.");
    }

    this.stringBuilder = new StringBuilder();
    this.errors = 0;
  }

  /**
   * Adds a new error to the stringbuilder.
   * @param errorPrefix what it should say before the error.
   * @param error the error to append.
   */
  private void addError(String errorPrefix, String error){
    stringBuilder.append("\n").append(errorPrefix).append(error);
    errors++;
  }

  /**
   * Checks if the tests failed and displays the results.
   */
  private void checkIfTestsFailedAndDisplayResult(){
    if(stringBuilder.length() == 0){
      assertTrue(true);
    }else {
      fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
    }
  }

  /**
   * Checks if constructor works with invalid parameters.
   */
  @Test
  @DisplayName("Checks if constructor works with invalid parameters.")
  public void testIfConstructorWorksWithInvalidParameters(){
    String prefix = "Expected an IllegalArgumentException since ";
    try {
      order = new Order(0, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input orderID cannot be 0");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, null, List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input user cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), null, OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input orderedProductList cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), null,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input orderState cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          null, "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input address cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input deliveryMethod cannot be empty");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), null, 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input deliveryMethod cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", null, 2022-05-04, "Klarna");
      addError(prefix, "The input dateOfOrder cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input  cannot be ");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input  cannot be ");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input  cannot be ");
    } catch (IllegalArgumentException exception){}
    try {
      order = new Order(934857348, new User(), List<OrderedProduct>(), OrderState.ORDERED,
          new Address(), "Posten", 2022-04-04, 2022-05-04, "Klarna");
      addError(prefix, "The input  cannot be ");
    } catch (IllegalArgumentException exception){}

  }

}
