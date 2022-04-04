package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests of the Address class.
 * The following tests are performed:
 * <ul>
 *   <li> Successful creation of instance with valid parameters (positive)</li>
 *   <li> Creation of instance with invalid parameters (negative)</li>
 *   <li> Test postalCode with invalid parameters (null and below zero)(negative)</li>
 *   <li> Test postalCode with valid parameter (positive) </li>
 *   <li> Test postalPlace with invalid parameter (integer, null) (negative)</li>
 *   <li> Test postalPlace with valid parameter (positive) </li>
 *   <li> Test streetName with invalid parameter (integer, null) (negative)</li>
 *   <li> Test streetName with valid parameter (positive) </li>
 *   <li> Test houseNumber with invalid parameters (null and below zero)(negative)</li>
 *   <li> Test houseNumber with valid parameter (positive) </li>
 *   <li> Test country with invalid parameter (integer, null) (negative)</li>
 *   <li> Test country with valid parameter (positive) </li>
 * </ul>
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
public class AddressTests {
  private Address address;
  private StringBuilder stringBuilder;
  private int errors;

  /**
   * Sets up te test address for testing
   */
  @BeforeEach
  public void setUpTestAddress() {
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway");
    } catch (IllegalArgumentException exception) {
        fail("Expected successful creation since the input is valid. ");
    }
    stringBuilder = new StringBuilder();
    errors = 0;
  }

  /**
   * Adds a new error to the stringBuilder.
   * @param errorPrefix what it should say before the error.
   * @param error the error to append.
   */
  private void addError(String errorPrefix, String error) {
    stringBuilder.append("\n").append(errorPrefix).append(error);
    errors++;
  }

  /**
   * Checks if the tests failed and display the results.
   */
  private void checkIfTestsFailedAndDisplayResult() {
    if (stringBuilder.isEmpty()){
      assertTrue(true);
    } else {
      fail("\nAmount of errors: " + errors + " listed errors: " + stringBuilder.toString());
    }
  }

  @Test
  @DisplayName("Testing creation of instance with invalid parameters")
  public void testConstructorWorksWithInvalidParameters() {
    String prefix = "Expected an IllegalArgumentExeption since ";
    try {
      address = new Address(0, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input postalCode is zero");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(-10, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input postalCode is below zero");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, null, "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input postalPlace is null");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", null, 123, "Norway");
      addError(prefix, "the input postalPlace is null");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", 0, "Norway");
      addError(prefix, "the input houseNumber is below accepted number");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", -10, "Norway");
      addError(prefix, "the input postalCode is below zero");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", 123, null);
      addError(prefix, "the input country is null");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input  is ");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input  is ");
    } catch (IllegalArgumentException exeption) {
    }
    try {
      address = new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input  is ");
    } catch (IllegalArgumentException exeption) {
    }
  }


}
