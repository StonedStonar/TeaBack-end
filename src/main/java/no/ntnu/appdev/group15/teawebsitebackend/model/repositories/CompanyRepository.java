package no.ntnu.appdev.group15.teawebsitebackend.model.repositories;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import org.springframework.data.repository.CrudRepository;

/**
 * Represents the connection to the database for companies.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {
}
