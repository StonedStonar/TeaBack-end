package no.ntnu.appdev.group15.teawebsitebackend.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents an order made by a user.
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long orderID;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "userID")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = OrderedProduct.class)
    @JoinTable(name = "orderedProducts",
            joinColumns = @JoinColumn(name= "orderID", referencedColumnName = "orderID"),
            inverseJoinColumns = @JoinColumn(name= "orderedProductID", referencedColumnName = "orderedProductID")
    )
    private List<OrderedProduct> orderedProductList; //many to many

    @Enumerated
    private OrderState orderState;

    @OneToOne(targetEntity = OrderAddress.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "orderAddressID")
    private OrderAddress orderAddress;
    private String deliveryMethod;
    private LocalDate dateOfOrder;
    private LocalDate shippedDate;
    private String paymentMethod;
    private boolean cancelled;

    public Order(){}

    /**
     * Makes an instance of the Order class.
     */
    //Todo: Skriv dokumentasjon
    public Order(long orderID, User user, List<OrderedProduct> orderedProductList,
                 OrderState orderState, Address address, String deliveryMethod,
                 LocalDate dateOfOrder, String paymentMethod, boolean cancelled) {

        checkIfNumberNotNegative(orderID, "order ID");
        this.orderID = orderID;

        checkIfObjectIsNull(user, "user");
        this.user = user;

        checkIfObjectIsNull(orderedProductList, "ordered products");
        this.orderedProductList = orderedProductList;

        checkIfObjectIsNull(orderState, "orderState");
        this.orderState = orderState;

        checkIfObjectIsNull(address, "address");
        this.orderAddress = new OrderAddress(address);

        checkString(deliveryMethod, "DeliveryMethod");
        this.deliveryMethod = deliveryMethod;

        checkIfDateIsBeforeOrEqualToCurrentDate(dateOfOrder);
        this.dateOfOrder = dateOfOrder;

        //checkIfObjectIsNull(shippedDate, "Shipped date");
        //this.shippedDate = null;

        checkString(paymentMethod, "Payment method");
        this.paymentMethod = paymentMethod;

        this.cancelled = cancelled;
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
     * Gets the ID
     * @return the ID
     */
    public long getID() {
        return orderID;
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
        return orderAddress;
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
     * @param orderState the order state.
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
     * Check to make sure that integer values cannot be negative.
     * @param object the object to be checked.
     * @param error exception message to be displayed.
     */
    private void checkIfNumberNotNegative(long object, String error) {
        if (object <= 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative values.");
        }
    }

    /**
     * Checks if an input date is before or equal to today's date.
     * @param localDate the date to check.
     */
    private void checkIfDateIsBeforeOrEqualToCurrentDate(LocalDate localDate){
        checkIfObjectIsNull(localDate, "date");
        if (LocalDate.now().isBefore(localDate)){
            throw new IllegalArgumentException("The set date is after current date.");
        }
    }

    /**
     * TODO cancelled order, later not now.
     */

    /**
     * Checks if date is after current date.
     * @param date the date to check.
     */
    private void checkIfDateIsAfterCurrentDate(LocalDate date){
        checkIfObjectIsNull(date, "date");
        if (LocalDate.now().isAfter(date)) {
            throw new IllegalArgumentException("Date cannot be later than the current date");
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
