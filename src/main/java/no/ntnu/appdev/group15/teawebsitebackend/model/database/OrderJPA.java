package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;

/**
 * Represents a connection to an Object-Relational to the database.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */

public class OrderJPA implements OrderRegister {

   private


  @Override
  public void addOrder(Order order) throws CouldNotAddOrderException {

  }

  @Override
  public void removeOrder(Order order) throws CouldNotRemoveOrderException {

  }

  @Override
  public void removeOrderWithOrderID(long orderID) throws CouldNotRemoveOrderException {

  }

  @Override
  public Order getOrderWithId(long orderID) throws CouldNotGetOrderException {
    return null;
  }

  @Override
  public List<Order> getAllOrdersOfUser(long userID) {
    return null;
  }

  @Override
  public List<Order> getAllOrders() {
    return null;
  }
}
