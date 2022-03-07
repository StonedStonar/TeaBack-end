package no.ntnu.appdev.group15.teawebsitebackend.model.registers;

import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.*;

import java.util.List;

/**
 * Represents a basic set of methods a orderRegister should have.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface OrderRegister {

    /**
     * Adds a new order to the register.
     * @param order the new order to add.
     * @throws CouldNotAddOrderException gets thrown if the order could not be added.
     */
    void addOrder(Order order) throws CouldNotAddOrderException;

    /**
     * Removes an order from the register.
     * @param order the order to remove.
     * @throws CouldNotRemoveOrderException gets thrown if the order with that id could not be located.
     */
    void removeOrder(Order order) throws CouldNotRemoveOrderException;

    /**
     * Removes an order with order id.
     * @param orderID the id of the order.
     * @throws CouldNotRemoveOrderException gets thrown if the order with that id could not be located.
     */
    void removeOrderWithOrderID(long orderID) throws CouldNotRemoveOrderException;

    /**
     * Gets an order with order id.
     * @param orderID the id of the order.
     * @return the order that matches the order id.
     * @throws CouldNotGetOrderException gets thrown if the order with that id could not be located.
     */
    Order getOrderWithId(long orderID) throws CouldNotGetOrderException;

    /**
     * Gets a list with all the orders of a user. This can return a list with 0 elements.
     * @param userID the user to get all the orders of.
     * @return a list with all the orders of the user.
     */
    List<Order> getAllOrdersOfUser(long userID);

    /**
     * Gets all the orders in the system.
     * @return a list with all the orders of a user in the system.
     */
    List<Order> getAllOrders();
}
