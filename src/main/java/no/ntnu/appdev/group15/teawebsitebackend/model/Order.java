package no.ntnu.appdev.group15.teawebsitebackend.model;

import java.time.LocalDate;
import java.util.List;
import org.aspectj.weaver.ast.Or;

/**
 * Represents the order class. Represents an order made by the user.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
public class Order {

    private Long orderID;
    private User user;
    private List<OrderedProduct> orderedProductList; //many to many
    private OrderState orderState;
    private Address address;
    private String deliveryMethod;
    private LocalDate dateOfOrder;
    private LocalDate shippedDate;
    private String paymentMethod;

    /**
     * Makes an instance of the Order class.
     */
    public Order(Long orderID, User user, List<OrderedProduct> orderedProductList,
                 OrderState orderState, Address address, String deliveryMethod,
                 LocalDate dateOfOrder, LocalDate shippedDate, String paymentMethod) {
        checkIfObjectIsNull(orderID, "order ID");
        this.orderID = orderID;

        checkIfObjectIsNull(user, "user");
        this.user = user;

        this.orderedProductList = orderedProductList;

        checkIfObjectIsNull(orderState, "orderState");
        this.orderState = orderState;

        checkIfObjectIsNull(address, "address");
        this.address = address;

        checkString(deliveryMethod, "DeliveryMethod");
        this.deliveryMethod = deliveryMethod;

        checkIfObjectIsNull(dateOfOrder, "Date of order");
        this.dateOfOrder = dateOfOrder;

        checkIfObjectIsNull(shippedDate, "Shipped date");
        this.shippedDate = shippedDate;

        checkString(paymentMethod, "Payment method");
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the user.
     * @return the user.
     */
    public User getUser(){
        return user;
    }

    /**
     * Gets the ordered product(s).
     * @return the ordered product(s).
     */
    public List<OrderedProduct> getOrderedProductList() {
        return orderedProductList;
    }

    /**
     * Gets the address of the user.
     * @return the address of the user.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Gets the delivery method.
     * @return the delivery method.
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the order state.
     * @param orderState the orderstate.
     */
    public void setOrderState(OrderState orderState) {
        checkIfObjectIsNull(orderState, "orderState");
        this.orderState = orderState;
    }

    /**
     * Gets order state.
     * @return the order state.
     */
    public OrderState getOrderState() {
        return orderState;
    }

    /**
     * Gets the date of order.
     * @return the date of order.
     */
    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    /**
     * Gets the date of shipping.
     * @return the shipping date.
     */
    public LocalDate getShippedDate() {
        return shippedDate;
    }

    /**
     * Gets payment method.
     * @return the payment method.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }


    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
