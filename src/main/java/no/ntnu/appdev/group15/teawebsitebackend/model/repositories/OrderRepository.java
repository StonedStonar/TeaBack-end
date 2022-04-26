package no.ntnu.appdev.group15.teawebsitebackend.model.repositories;

import java.util.Optional;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Represents the connection to the database
 */
public interface OrderRepository extends CrudRepository<Order, Long>{

  /**
   * Gets all orders by userID
   * @param userID the user to get the orders for
   * @return order from user
   */
  @Query(value = "SELECT * FROM orders WHERE userID = :userID", nativeQuery = true)
  Iterable<Order> getAllOrdersForUser(@Param("userID")long userID);
}
