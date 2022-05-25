package no.ntnu.no.appdev.group15.teawebsitebackend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;

import no.ntnu.appdev.group15.teawebsitebackend.model.OrderState;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderedProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.ProductDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static no.ntnu.appdev.group15.teawebsitebackend.model.OrderState.ORDERED;
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
      order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED,
          makeAddress(),
              "Posten", LocalDate.now().minusDays(1), "Klarna", false);
    } catch (IllegalArgumentException exception){
      fail("Expected the order to be made, since the input it valid.");
    }

    this.stringBuilder = new StringBuilder();
    this.errors = 0;
  }

  /**
   * Makes a default address.
   * @return the default address.
   */
  private Address makeAddress(){
    return new Address(2910, "Aurdal", "HeiStreet", 15, "Country");
  }

  /**
   * Adds a new error to the string builder.
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
   * Makes an ordered list of products.
   * @return the list
   */
  private List<OrderedProduct> makeListWithOrderedProducts() {
    List<OrderedProduct> orderedProductList = new ArrayList<>();
    orderedProductList.add(new OrderedProduct(new Product(1, "Fjell laks Te", 329, 100, new ProductDetails("Lul", "Tea"), new Company()), 2));
    return orderedProductList;
  }

  /**
   * Checks if constructor works with invalid parameters.
   */
  @Test
  @DisplayName("Checks if constructor works with invalid parameters.")
  public void testIfConstructorWorksWithInvalidParameters(){
    String prefix = "Expected an IllegalArgumentException since ";
    LocalDate orderedDate = LocalDate.now().minusDays(1);
    LocalDate shippedDate = LocalDate.now();
    try {
        order = new Order(0L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", orderedDate, "Klarna", false);
        addError(prefix, "The input orderID cannot be 0");
    }catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, null, makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", orderedDate, "Klarna", false);
        addError(prefix, "The input user cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), null, ORDERED, makeAddress(), "Posten", orderedDate, "Klarna", false);
        addError(prefix, "The input orderedProductList cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), null, makeAddress(), "Posten", orderedDate, "Klarna", false);
        addError(prefix, "The input orderState cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, null, "Posten", orderedDate, "Klarna", false);
        addError(prefix, "The input address cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "", orderedDate, "Klarna", false);
        addError(prefix, "The input deliveryMethod cannot be empty");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), null, orderedDate, "Klarna", false);
        addError(prefix, "The input deliveryMethod cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", null, "Klarna", false);
        addError(prefix, "The input dateOfOrder cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", orderedDate, "", false);
        addError(prefix, "The input paymentMethod cannot be empty");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", orderedDate, null, false);
        addError(prefix, "The input paymentMethod cannot be null");
    } catch (IllegalArgumentException exception){}
    try {
        order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", orderedDate.plusDays(2), "Klarna", false);
        addError(prefix, "The input orderedDate cannot be after todays date");
    } catch (IllegalArgumentException exception){}

    checkIfTestsFailedAndDisplayResult();

  }

  /**
   * Tests is the constructor works with valid parameters.
   */
  @Test
  @DisplayName("Checks if the constructor works with valid parameters")
  public void testIfConstructorWorksWithValidParameters(){
    String prefix = "Expected an IllegalArgumentException since ";

    try {
      Order order = new Order(123L, new User(), makeListWithOrderedProducts(), ORDERED, makeAddress(), "Posten", LocalDate.now().minusDays(1), "Klarna", false);
    } catch (IllegalArgumentException exception) {
      addError(prefix, "Valid format. ");
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Parameters checked: date before orderDate
   */
  @Test
  @DisplayName("Checks if setShippedDate works with invalid parameters. ")
  public void testIfSetShippedDateWorksWithInvalidParameters(){
    String prefix = "Invalid input, ";
    try {
      order.setShippedDate(LocalDate.now().minusDays(1));
      addError(prefix, "date is after todays date. ");
    } catch (IllegalArgumentException exception) {

    }
    try {
      order.setShippedDate(null);
      addError(prefix, "date cannot be null");
    } catch (IllegalArgumentException exception) {
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Checks if setShippedDate works with valid parameters.
   */
  @Test
  @DisplayName("Checks if setShippedDate works with valid parameters. ")
  public void testIfSetShippedDateWorksWithValidParameters(){
    try {
      order.setShippedDate(LocalDate.now().plusDays(1));
    } catch (IllegalArgumentException exception) {
      addError("Expected shipped date to be set since, ", "date is a valid input. ");
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Checks if setOrderState works with invalid parameters.
   */
  @Test
  @DisplayName("Checks if setOrderState works with invalid parameters. ")
  public void testIfSetOrderStateWorksWithInvalidParameters(){
    String prefix = "Expected an IllegalArgumentException since ";
    try {
      order.setOrderState(null);
      addError(prefix, "the orderState is null. ");
    } catch (IllegalArgumentException exception) {}

    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Checks if setOrderState works with valid parameters.
   */
  @Test
  @DisplayName("Checks if setOrderState works with valid parameters. ")
  public void testIfSetOrderStateWorksWithValidParameters(){
    try {
      OrderState orderState = ORDERED;
      order.setOrderState(orderState);
      assertEquals(order.getOrderState(), orderState);
    } catch (IllegalArgumentException e){
      addError("Expected the orderState to be set because the input is valid. ", "");
    }

    checkIfTestsFailedAndDisplayResult();
  }


}
