package no.ntnu.no.appdev.group15.teawebsitebackend;


import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests of the teaDetails class.
 * The following tests are performed:
 * <ul>
 *   <li> Successful creation of instance with valid parameters (positive)</li>
 *   <li> Creation of instance with invalid parameters (negative)</li>
 * </ul>
 * @author Trine Merete Staverløkk
 * @version 0.1
 */

public class TeaDetailsTests {
  private TeaDetails teaDetails;
  private StringBuilder stringBuilder;
  private String expectedError;
  private int errors;

  /**
   * Sets up the test teaDetails for testing
   *
   */
  @BeforeEach
  public void setUpTestTeaDetails() {
    try {
      teaDetails = new TeaDetails();
    } catch (IllegalArgumentException exception){
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
  @DisplayName("")
  public void testConstructorWorksWithValidParameters() {
    String errorPrefix = "Expected the creation to be successful since ";
    try {
      TeaDetails teaDetails = new TeaDetails();
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
    String prefix = "Expected an IllegalArgumentException  since ";
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails();
      addError(prefix, "The input  is ");
    }catch (IllegalArgumentException exception) {
    }
  }



}
