package no.ntnu.appdev.group15.teawebsitebackend.model.repositories;

import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Represents the connection to the database for products.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
}
