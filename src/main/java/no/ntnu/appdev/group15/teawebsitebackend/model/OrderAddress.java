package no.ntnu.appdev.group15.teawebsitebackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;


/**
 * Represents a order address.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Entity
public class OrderAddress extends Address {

    /**
     * Empty constructor for order address.
     */
    public OrderAddress() {
    }

    /**
     * Makes an instance of the OrderAddress class.
     * @param addressID the id of the address.
     * @param postalCode The postal code
     * @param postalPlace The place
     * @param streetName the address
     * @param houseNumber The house number
     * @param country The country
     * @throws IllegalArgumentException gets thrown if the input parameters are invalid.
     */
    @JsonCreator
    public OrderAddress(@JsonProperty("addressID") long addressID, @JsonProperty("postalCode") int postalCode,
                        @JsonProperty("postalPlace") String postalPlace, @JsonProperty("streetName") String streetName,
                        @JsonProperty("houseNumber") int houseNumber, @JsonProperty("country") String country) {
        super(addressID, postalCode, postalPlace, streetName, houseNumber, country);
    }

    /**
     * Makes an instance of the OrderAddress class.
     * @param address the address of the person.
     * @throws IllegalArgumentException gets thrown if the input parameters are invalid.
     */
    public OrderAddress(Address address){
        super(address.getPostalCode(), address.getPostalPlace(), address.getStreetName(), address.getHouseNumber(), address.getCountry());
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
