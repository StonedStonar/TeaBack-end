package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Order;

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
 *   <li> Test of setOrderState(), with invalid parameters (negative)</li>
 * </ul>
 * @author Trine Merete Staverløkk
 * @version 0.1
 */

public class OrderTest {
  private Order testOrder;
  private StringBuilder stringBUilder;
  private int errors;

  @BeforeEach
  public void setUpTestOrder() {
    try {
      this.testOrder = new Order(203948, User, );
    }

  }
}
