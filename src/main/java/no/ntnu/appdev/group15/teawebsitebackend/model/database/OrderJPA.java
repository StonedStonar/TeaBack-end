package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    if (!orderRepository.existsById(order.getOrderID())){
      orderRepository.save(order);
    } else {
      throw new CouldNotAddOrderException("Order does already exist");
    }
  }

  @Override
  public void removeOrder(Order order) throws CouldNotRemoveOrderException {
    checkIfOrderIsValid(order);
    if (orderRepository.existsById(order.getOrderID())) {
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

    Optional<Order> opOrder = orderRepository.findById(orderID);
    if (opOrder.isEmpty()) {
      throw new CouldNotGetOrderException("The order with id " + orderID + " is not in the system");
    }
    return opOrder.get();
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

  //TODO LAG TESTER
  @Override
  public void updateOrder(Order order) throws CouldNotGetOrderException {
    checkIfOrderIsValid(order);
    if (orderRepository.existsById(order.getOrderID())){
      orderRepository.save(order);
    } else {
      throw new CouldNotGetOrderException("Order could not be found");
    }
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
