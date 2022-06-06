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
  private int errors;
  private String illegalPrefix;

  /**
   * Makes an address test instance.
   */
  public AddressTests(){
    this.illegalPrefix = makeExceptionString("IllegalArgumentException");
  }

  /**
   * Makes an exception into the wanted string.
   * @param exceptionName the name of the exception.
   * @return the full exception string.
   */
  private String makeExceptionString(String exceptionName){
    return "Expected to get a " +  exceptionName + " since";
  }

  /**
   * Sets up the test address for testing
   */
  @BeforeEach
  public void setUpTestAddress() {
    try {
      this.address = new Address(1, 6015, "Ålesund", "Ålesundvegen", 123, "Norway");
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
    if (stringBuilder.length() == 0){
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

  /**
   * Tests if PostalCode works with valid parameter.
   */
  @Test
  @DisplayName("Tests if PostalCode works with valid parameter.")
  public void testsIfSetPostalCodeWorksWithValidParameters(){
    int parameter = 123;
    try {
      address.setPostalCode(parameter);
    } catch (IllegalArgumentException e) {
      addError(illegalPrefix, "since the input is valid. ");
    }
    checkIfTestsFailedAndDisplayResult();
    assertEquals(address.getPostalCode(), parameter);
  }

  /**
   * Tests if PostalCode works with invalid parameter.
   */
  @Test
  @DisplayName("Tests if PostalCode works with invalid parameter.")
  public void testsIfSetPostalCodeWorksWithInvalidParameters(){
    try {
      address.setPostalCode(0);
      addError(illegalPrefix, " since the input is zero. ");
    } catch (IllegalArgumentException e) {}
    try {
      address.setPostalCode(-34758);
      addError(illegalPrefix, " since the input is below zero");
    } catch (IllegalArgumentException e) {}

    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Tests if PostalPlace works with valid parameter.
   */
  @Test
  @DisplayName("Tests if PostalPlace works with valid parameter.")
  public void testsIfSetPostalPlaceWorksWithValidParameters(){
    String parameter = "Ålesund";
    try {
      address.setPostalPlace(parameter);
    } catch (IllegalArgumentException e) {
      addError(illegalPrefix, "since the input is valid. ");
    }
    checkIfTestsFailedAndDisplayResult();
    assertEquals(address.getPostalPlace(), parameter);
  }

  /**
   * Tests if PostalPlace works with invalid parameter.
   */
  @Test
  @DisplayName("Tests if PostalPlace works with invalid parameter.")
  public void testsIfSetPostalPlaceWorksWithInvalidParameters(){
    try {
      address.setPostalPlace("");
      addError(illegalPrefix, "since the input is empty. ");
    } catch (IllegalArgumentException e) {}
    try {
      address.setPostalPlace(null);
      addError(illegalPrefix, "since the input is null");
    } catch (IllegalArgumentException e) {}

    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Tests if StreetName works with valid parameter.
   */
  @Test
  @DisplayName("Tests if StreetName works with valid parameter.")
  public void testsIfSetStreetNameWorksWithValidParameters(){
    String parameter = "Trineveien";
    try {
      address.setStreetName(parameter);
    } catch (IllegalArgumentException e) {
      addError(illegalPrefix, "since the input is valid. ");
    }
    checkIfTestsFailedAndDisplayResult();
    assertEquals(address.getStreetName(), parameter);
  }

  /**
   * Tests if StreetName works with invalid parameter.
   */
  @Test
  @DisplayName("Tests if StreetName works with invalid parameter.")
  public void testsIfSetStreetNameWorksWithInvalidParameters(){
    try {
      address.setPostalPlace("");
      addError(illegalPrefix, "since the input is empty. ");
    } catch (IllegalArgumentException e) {}
    try {
      address.setPostalPlace(null);
      addError(illegalPrefix, "since the input is null");
    } catch (IllegalArgumentException e) {}

    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Tests if HouseNumber works with valid parameter.
   */
  @Test
  @DisplayName("Tests if HouseNumber works with valid parameter.")
  public void testsIfSetHouseNumberWorksWithValidParameters(){
    int parameter = 123;
    try {
      address.setHouseNumber(parameter);
    } catch (IllegalArgumentException e) {
      addError(illegalPrefix, "since the input is valid. ");
    }
    checkIfTestsFailedAndDisplayResult();
    assertEquals(address.getHouseNumber(), parameter);
  }

  /**
   * Tests if  works with invalid parameter.
   */
  @Test
  @DisplayName("Tests if  works with invalid parameter.")
  public void testsIfSetHouseNumberWorksWithInvalidParameters(){
    try {
      address.setHouseNumber(0);
      addError(illegalPrefix, " since the input is zero. ");
    } catch (IllegalArgumentException e) {}
    try {
      address.setHouseNumber(-34758);
      addError(illegalPrefix, " since the input is below zero");
    } catch (IllegalArgumentException e) {}

    checkIfTestsFailedAndDisplayResult();
  }


  /**
   * Tests if SetCountry works with valid parameter.
   */
  @Test
  @DisplayName("Tests if SetCountry works with valid parameter.")
  public void testsIfSetCountryWorksWithValidParameters(){
    String parameter = "Norway";
    try {
      address.setCountry(parameter);
    } catch (IllegalArgumentException e) {
      addError(illegalPrefix, "since the input is valid. ");
    }
    checkIfTestsFailedAndDisplayResult();
    assertEquals(address.getCountry(), parameter);
  }


  /**
   * Tests if  works with invalid parameter.
   */
  @Test
  @DisplayName("Tests if  works with invalid parameter.")
  public void testsIfSetCountryWorksWithInvalidParameters(){
    try {
      address.setCountry("");
      addError(illegalPrefix, "since the input is empty. ");
    } catch (IllegalArgumentException e) {}
    try {
      address.setCountry(null);
      addError(illegalPrefix, "since the input is null");
    } catch (IllegalArgumentException e) {}

    checkIfTestsFailedAndDisplayResult();
  }
}
