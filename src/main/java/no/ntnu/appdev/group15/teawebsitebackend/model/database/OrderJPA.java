package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.OrderRepository;
import org.springframework.stereotype.Service;

/**
 * Represents a connection to an Object-Relational to the database.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@Service
public class OrderJPA implements OrderRegister {

   private OrderRepository orderRepository;

  /**
   * Makes an instance of the orderJPA class.
   * @param orderRepository the order repository that is the connection to the database.
   */
  public OrderJPA(OrderRepository orderRepository) {
    checkIfObjectIsNull(orderRepository, "order repository");
    this.orderRepository = orderRepository;
  }


  @Override
  public void addOrder(Order order) throws CouldNotAddOrderException{
    checkIfOrderIsValid(order);
    if (!orderRepository.existsById(order.getOrderId())){
      orderRepository.save(order);
    } else {
      throw new CouldNotAddOrderException("Order does already exist");
    }
  }

  @Override
  public void removeOrder(Order order) throws CouldNotRemoveOrderException {
    checkIfOrderIsValid(order);
    if (orderRepository.existsById(order.getOrderId())) {
      orderRepository.delete(order);
    } else {
      throw new CouldNotRemoveOrderException("The order does not exist, and cannot be removed");
    }
  }

  @Override
  public void removeOrderWithOrderID(long orderID) throws CouldNotRemoveOrderException {
    checkIfNumberIsAboveZero(orderID, "order ID");
    if(orderRepository.existsById(orderID)){
      orderRepository.deleteById(orderID);
    } else {
      throw new CouldNotRemoveOrderException("The order with id " + orderID + " is not in the system");
    }
  }

  @Override
  public Order getOrderWithId(long orderID) throws CouldNotGetOrderException {
    checkIfNumberIsAboveZero(orderID, "orderID");
    Order orderIDToFind = null;
    if(orderRepository.existsById(orderID)) {
      orderIDToFind = orderRepository.getById(orderID);
    } else {
      throw new CouldNotGetOrderException("The order with id " + orderID + " is not in the system");
    }
    return orderIDToFind;
  }

  @Override
  public List<Order> getAllOrdersOfUser(long userID) {
    checkIfNumberIsAboveZero(userID, "user ID");
    List<Order> ordersOfUser = new ArrayList<>();
    Iterable<Order> iteratedOrders = orderRepository.getAllOrdersForUser(userID);
    iteratedOrders.forEach(order -> ordersOfUser.add(order));
    return ordersOfUser;
  }


  @Override
  public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();
    Iterable<Order> it = orderRepository.findAll();
    it.forEach(order ->orders.add(order));
    return orders;
  }


  /**
   * Checks if the order is not null.
   * @param order the user to check.
   * @throws IllegalArgumentException gets thrown when the user is null.
   */
  private void checkIfOrderIsValid(Order order){
    checkIfObjectIsNull(order, "order");
  }

  /**
   * Checks if the input number is above zero.
   * @param numberToCheck the number to check.
   */
  private void checkIfNumberIsAboveZero(long numberToCheck, String prefix){
    if (numberToCheck <= 0){
      throw new IllegalArgumentException("The " + prefix + " must be larger than zero.");
    }
  }

  /**
   * Checks if a string is of a valid format or not.
   *
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
