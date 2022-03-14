package no.ntnu.appdev.group15.teawebsitebackend.model;

/**
 * Represents the state of an order or ordered product.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public enum OrderState {

    /**
     * The state of an order when it has been ordered.
     */
    ORDERED,

    /**
     * The state of an order when it has been packed into a box and waiting to be sent.
     */
    PACKED,

    /**
     * The state of an order when it has been packed
     */
    SENT,

    /**
     * The state when an order has been returned.
     */
    RETURNED,

    /**
     * The state when an order has been cancelled before it was packed.
     */
    CANCELLED,

    /**
     * The state when an order has been delivered.
     */
    DELIVERED,

    /**
     * The state when one of a product has been returned but not all of them.
     */
    PARTIALRETURN;
}
