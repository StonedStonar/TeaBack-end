package no.ntnu.appdev.group15.teawebsitebackend.model;

import java.time.LocalDate;
import java.util.List;

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
    private Boolean cancelled;

    /**
     * Makes an instance of the Order class.
     */
    public Order() {

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
