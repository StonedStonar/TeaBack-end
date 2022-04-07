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
 * </ul>
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
public class AddressTests {
  private Address address;
  private StringBuilder stringBuilder;
  private String expectedError;
  private int errors;

  /**
   * Sets up the test address for testing
   */
  @BeforeEach
  public void setUpTestAddress() {
    try {
      address = new Address(1, 6015, "Ålesund", "Ålesundvegen", 123, "Norway");
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

  /**
   * Checks that creation of instance is succesfull with valid parameters (positive test)
   */
  @Test
  @DisplayName("Testing creation of instance with valid parameters")
  public void testConstructorWorksWithValidParameters() {
    String errorPrefix = "Expected the creation to be successful since ";
    try {
      Address address = new Address(2, 6015, "Ålesund", "Ålesundvegen", 666, "Norway");
    } catch (IllegalArgumentException exception) {
      addError(errorPrefix, "the input is valid");
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Checks that creation of instance is not successful with invalid parameters (negative test9
   */
  @Test
  @DisplayName("Testing creation of instance with invalid parameters")
  public void testConstructorWorksWithInvalidParameters() {
    String prefix = "Expected an IllegalArgumentExeption since ";
    try {
      address = new Address(8, 0, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input postalCode is zero");
    } catch (IllegalArgumentException exception) {
    }
    try {
      address = new Address(9,-10, "Ålesund", "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input postalCode is below zero");
    } catch (IllegalArgumentException exception) {
    }
    try {
      address = new Address(10,6015, null, "Ålesundvegen", 123, "Norway");
      addError(prefix, "the input postalPlace is null");
    } catch (IllegalArgumentException exception) {
    }
    try {
      address = new Address(11,6015, "Ålesund", null, 123, "Norway");
      addError(prefix, "the input postalPlace is null");
    } catch (IllegalArgumentException exception) {
    }
    try {
      address = new Address(12,6015, "Ålesund", "Ålesundvegen", 0, "Norway");
      addError(prefix, "the input houseNumber is zero");
    } catch (IllegalArgumentException exception) {
    }
    try {
      address = new Address(13,6015, "Ålesund", "Ålesundvegen", -10, "Norway");
      addError(prefix, "the input postalCode is below zero");
    } catch (IllegalArgumentException exception) {
    }
    try {
      address = new Address(14,6015, "Ålesund", "Ålesundvegen", 123, null);
      addError(prefix, "the input country is null");
    } catch (IllegalArgumentException exception) {
    }

    checkIfTestsFailedAndDisplayResult();
  }
}
