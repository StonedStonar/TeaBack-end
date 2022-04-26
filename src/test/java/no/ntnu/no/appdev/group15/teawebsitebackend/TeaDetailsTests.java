package no.ntnu.no.appdev.group15.teawebsitebackend;


import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
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
  private int errors;
  private String prefixIllegal;

  /**
   * Sets up the test teaDetails for testing
   */
  @BeforeEach
  public void setUpTestTeaDetails() {
    try {
      teaDetails = new TeaDetails();
    } catch (IllegalArgumentException exception) {
    }
    stringBuilder = new StringBuilder();
    errors = 0;
  }

  /**
   * Adds a new error to the stringBuilder.
   *
   * @param errorPrefix what it should say before the error.
   * @param error       the error to append.
   */
  private void addError(String errorPrefix, String error) {
    stringBuilder.append("\n").append(errorPrefix).append(error);
    errors++;
  }

  /**
   * Adds an error with an exception in the title.
   *
   * @param errorPrefix what it should say before the main error.
   * @param error       what it should say after the error.
   * @param exception   the exception that was not expected.
   */
  private void addErrorWithException(String errorPrefix, String error, Exception exception) {
    addError(errorPrefix, error);
    stringBuilder.append(" and not a ").append(exception.getClass().getSimpleName());
  }

  /**
   * Checks if the tests failed and display the results.
   */
  private void checkIfTestsFailedAndDisplayResult() {
    if (stringBuilder.length() == 0) {
      assertTrue(true);
    } else {
      fail("\nAmount of errors: " + errors + " listed errors: " + stringBuilder.toString());
    }
  }

  /**
   * Checks that creation of instance is successful with valid parameters (positive test)
   */
  @Test
  @DisplayName("")
  public void testConstructorWorksWithValidParameters() {
    String errorPrefix = "Expected the creation to be successful since ";
    try {
      TeaDetails teaDetails = new TeaDetails("warm", "herbs");
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
      teaDetails = new TeaDetails("", "herbs");
      addError(prefix, "The input ingredients is empty");
    } catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails(null, "herbs");
      addError(prefix, "The input ingredients is null");
    } catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails("warm", "");
      addError(prefix, "The input herbs is empty");
    } catch (IllegalArgumentException exception) {
    }
    try {
      teaDetails = new TeaDetails("warm", null);
      addError(prefix, "The input herbs is null");
    } catch (IllegalArgumentException exception) {
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Check if setDescription works with invalid parameters
   */
  @Test
  @DisplayName("Check if setDescription works with invalid parameters")
  public void testIfSetDescriptionWorksWithInvalidParameters() {
    String errorPrefix = "Expected to get an IllegalArgumentException since,  ";
    try {
      teaDetails.setDescription(null);
    } catch (IllegalArgumentException exception) {
      addError(errorPrefix, "the input is null");
    }
    try {
      teaDetails.setDescription("");
    } catch (IllegalArgumentException exception) {
      addError(errorPrefix, "the input is empty");
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Check if setDescription works with valid parameters
   */
  @Test
  @DisplayName("Check if setDescription works with valid parameters")
  public void testIfSetDescriptionWorksWithValidParameters() {
    String errorPrefix = "Expected the description to be set since ";
    String parameter = "Warm";
    try {
      teaDetails.setDescription(parameter);
    } catch (IllegalArgumentException exception) {
      addError(errorPrefix, "the input is valid.");
    }
    checkIfTestsFailedAndDisplayResult();
    assertEquals(teaDetails.getDescription(), parameter);
  }


  /**
   * Check if addTag works with invalid parameters
   */
  @Test
  @DisplayName("Check if addTag works with invalid parameters")
  public void testIfAddTagWorksWithInvalidParameters() {
    String errorPrefix = "Expected to get an IllegalArgumentException since,  ";
    try {
      teaDetails.addTag(null);
    } catch (CouldNotAddTagException exception) {
      addError(errorPrefix, "the input is null");
    }
  }

  /**
   * Check if addTag works with valid parameters
   */
  @Test
  @DisplayName("Check if addTag works with valid parameters")
  public void testIfAddTagWorksWithValidParameters() {
    String errorPrefix = "Expected the description to be set since ";
    try {
      teaDetails.addTag(new Tag());
    } catch (IllegalArgumentException | CouldNotAddTagException exception) {
      addErrorWithException(errorPrefix, "the input is valid. ", exception);
    }
    checkIfTestsFailedAndDisplayResult();
  }

  /**
   * Check if removeTag works with invalid parameters
   */
  @Test
  @DisplayName("Check if removeTag works with invalid parameters")
  public void testIfRemoveTagWorksWithInvalidParameters() {
    String errorPrefix = "Expected to get an IllegalArgumentException since,  ";
    try {
      teaDetails.removeTag(null);
    } catch (CouldNotRemoveTagException couldNotRemoveTagException) {
      addError(errorPrefix, "the input is invalid");
    }
  }


  /**
   * Check if removeTag works with valid parameters
   */
  @Test
  @DisplayName("Check if removeTag works with valid parameters")
  public void testIfRemoveTagWorksWithValidParameters() {
    String errorPrefix = "Expected the description to be set since ";
    try {
      teaDetails.removeTag(new Tag());
    } catch (IllegalArgumentException | CouldNotRemoveTagException exception) {
      addErrorWithException(errorPrefix, "The input is valid", exception);
    }
    checkIfTestsFailedAndDisplayResult();
  }

}

