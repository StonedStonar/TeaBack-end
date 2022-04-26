package no.ntnu.appdev.group15.teawebsitebackend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents an order made by a user.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@Entity(name = "order")
public class Order {

    @Id
    @GeneratedValue
    private Long orderID;
    @Transient
    private User user;
    @Transient
    private List<OrderedProduct> orderedProductList; //many to many
    private OrderState orderState;
    @Transient
    private Address address;
    private String deliveryMethod;
    private LocalDate dateOfOrder;
    private LocalDate shippedDate;
    private String paymentMethod;
    private boolean cancelled;

    public Order(){}

    /**
     * Makes an instance of the Order class.
     */
    public Order(Long orderID, User user, List<OrderedProduct> orderedProductList,
                 OrderState orderState, Address address, String deliveryMethod,
                 LocalDate dateOfOrder, String paymentMethod, boolean cancelled) {
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

        //checkIfObjectIsNull(shippedDate, "Shipped date");
        //this.shippedDate = null;

        checkString(paymentMethod, "Payment method");
        this.paymentMethod = paymentMethod;

        this.cancelled = false;
    }

    /**
     * Checks if date is after current date.
     * @param date the date to check.
     */
    private void checkIfDateIsAfterCurrentDate(LocalDate date){
        checkIfObjectIsNull(date, "date");
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be later than the current date");
        }
    }

    /**
     * Sets the shipping date for an ordered product. Only sets shipped date for ordered and packed products.
     * @param shippedDate shipped date to set.
     */
    public void setShippedDate(LocalDate shippedDate){
        checkIfDateIsAfterCurrentDate(shippedDate);
        if (shippedDate.isAfter(dateOfOrder)){
            this.shippedDate = shippedDate;
            orderedProductList.stream().filter(orderedProduct -> orderedProduct.getOrderState() == OrderState.ORDERED || orderedProduct.getOrderState() == OrderState.PACKED)
                .forEach(orderedProduct -> orderedProduct.setOrderState(OrderState.SENT));
        } else {
            throw new IllegalArgumentException("The shipped date cannot be before the ordered date");
        }
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
     * TODO cancelled order, later not now.
     */

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
