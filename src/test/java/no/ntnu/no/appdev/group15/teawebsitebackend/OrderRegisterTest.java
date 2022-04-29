package no.ntnu.no.appdev.group15.teawebsitebackend;

import static no.ntnu.appdev.group15.teawebsitebackend.model.OrderState.ORDERED;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.Application;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderedProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotLoginToUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * Represents a test-class for OrderRepository methods.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class OrderRegisterTest {

  private Product product;
  private OrderRegister orderRegister;

  private Order order;
  private StringBuilder stringBuilder;
  private int errors;
  private User user;

  private String prefixIllegal;
  private String prefixAdd;

  @Autowired
  public OrderRegisterTest(OrderJPA orderJPA, UserJPA userJPA, ProductJPA productJPA) {
    checkIfObjectIsNull(orderJPA, "OrderJPA");
    this.orderRegister = orderJPA;
    prefixIllegal = makeExceptionString("IllegalArgumentException");
    prefixAdd = makeExceptionString("CouldNotAddOrderException");

    try {
      userJPA.addUser(new User("Bjarne", "Bjarnesen", new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge"), "bjarne@bjarn.com", "pass",12345678 , Role.ROLE_ADMIN));
      user = userJPA.loginToUser("bjarne@bjarn.com", "pass");
      productJPA.addProduct(new Product( "Green Leaf Tea", 11.99f, 7, new TeaDetails(), new Company()));
      product = productJPA.getAllProducts().get(0);
    } catch (CouldNotAddUserException | CouldNotLoginToUserException | CouldNotAddProductException | IndexOutOfBoundsException e) {
      fail("Default product or user could not be added " + e.getClass().getSimpleName());
    }
  }

  /**
   * Makes an exception into a desired string
   * @param exceptionName the name of the exception
   * @returnthe exception string
   */
  private String makeExceptionString(String exceptionName) {
    return "Expected to a " + exceptionName + " since";
  }

  @BeforeEach
  public void addTestData(){
    try {
      for (Order order1 : orderRegister.getAllOrders()) {
        orderRegister.removeOrder(order1);
      }
      orderRegister.addOrder(new Order(123L, user, makeListWithOrderedProducts(), ORDERED,
          user.getAddress(), "Posten", LocalDate.now().minusDays(1), "Klarna", false));
        order = orderRegister.getAllOrders().get(0);
    } catch (IllegalArgumentException | CouldNotAddOrderException | CouldNotRemoveOrderException | IndexOutOfBoundsException exception){
      fail("Expected the order to be made, since the input it valid. " + exception.getClass().getSimpleName());
    }

    this.stringBuilder = new StringBuilder();
    this.errors = 0;
  }

  /**
   * Creates an order that does not exist in register.
   * @return
   */
  public Order makeOrderNotInRegister() {
    return new Order()
  }


  /**
   * Makes an ordered list of products.
   * @return the list
   */
  private List<OrderedProduct> makeListWithOrderedProducts() {
    List<OrderedProduct> orderedProductList = new ArrayList<>();
    orderedProductList.add(new OrderedProduct(product, 2));
    return orderedProductList;
  }


  /**
   * Adds a new error to the string builder.
   * @param errorPrefix what it should say before the error.
   * @param error the error to append.
   */
  private void addError(String errorPrefix, String error){
    stringBuilder.append("\n").append(errorPrefix).append(" ").append(error);
    errors++;
  }

  /**
   * Adds an error with an exception in the title.
   * @param errorPrefix what it should say before the main error.
   * @param error what it should say after the error.
   * @param exception the exception that was not expected.
   */
  private void addErrorWithException(String errorPrefix, String error, Exception exception){
    addError(errorPrefix, error);
    stringBuilder.append(" and not a ").append(exception.getClass().getSimpleName());
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
   * Tests if addOrder works with invalid input.
   */
  @Test
  @DisplayName("Tests if addOrder works with invalid input.")
  public void testsIfAddOrderWorksWithInvalidInput() {
    try {
      orderRegister.addOrder(null);
      addError(prefixIllegal, "input cannot be null");
    } catch (IllegalArgumentException exception) {

    }catch (CouldNotAddOrderException e) {
      addErrorWithException(prefixIllegal, "input cannot be null", e);
    }
    try {
      orderRegister.addOrder(order);
      addError(prefixAdd, "the order is already in the system");
    } catch (IllegalArgumentException exception){
      addErrorWithException(prefixAdd, "the order is already in the system", exception);
    }catch (CouldNotAddOrderException exception) {
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Tests if addOrder works with valid input.
   */
  @Test
  @DisplayName("Tests if addOrder works with valid input.")
  public void testsIfAddOrderWorksWithValidInput() {
    try {
      orderRegister.addOrder(order);
    } catch (IllegalArgumentException | CouldNotAddOrderException e) {
      addErrorWithException("Expected the method to work since ", "the input is valid", e);
    }
    checkIfTestsFailedAndDisplayResult();
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
