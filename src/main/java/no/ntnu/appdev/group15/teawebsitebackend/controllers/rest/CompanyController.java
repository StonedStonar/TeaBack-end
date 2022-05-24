package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents the CompanyController needed to connect Company to database.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */

@RestController
@CrossOrigin
@RequestMapping("/Companies")
public class CompanyController {

    private CompanyRegister companyRegister;

    /**
     * Makes an instance of the CompanyRegister class.
     * @param companyJPA the Company repository to access the database.
     */
    public CompanyController(CompanyJPA companyJPA) {
        checkIfObjectIsNull(companyJPA, "companyJPA");
        companyRegister = companyJPA;
    }

    /**
     * Gets all the companies in the system.
     * @return a list with all the companies.
     */
    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public List<Company> getAllCompanies() {
        return companyRegister.getAllCompanies();
    }


    /**
     * Gets a product with a specified ID.
     * @param id the id of the company.
     * @return the company with the right id.
     * @throws CouldNotGetCompanyException gets thrown if the company could not be found.
     */
    @GetMapping("/{id}")
    public Company getCompanyWithCompanyID(@PathVariable long id) throws CouldNotGetCompanyException {
        return companyRegister.getCompanyWithId(id);
    }

    /**
     * Adds a Company to the system.
     * @param body the body we request.
     * @throws JsonProcessingException gets thrown if the format on the json object is invalid.
     * @throws CouldNotAddCompanyException gets thrown if the company could not be added successfully.
     */
    @PostMapping
    public void addCompany (@RequestBody String body) throws JsonProcessingException, CouldNotAddCompanyException {
        companyRegister.addCompany(makeCompany(body));
    }

    /**
     * Removes a company form the system using id.
     * @param id the companyID of the company.
     * @throws CouldNotRemoveCompanyException gets thrown if the company could not be removed.
     */
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public void removeCompany(@PathVariable Long id) throws CouldNotRemoveCompanyException {
        companyRegister.removeCompanyWithCompanyID(id);
    }

    /**
     * Makes it possible to update a Company in the system
     * @param body the body of the request.
     * @throws JsonProcessingException gets thrown if the format is invalid.
     * @throws CouldNotGetCompanyException gets thrown if company could not be found.
     */
    @PutMapping
    //@PreAuthorize("hasRole('ADMIN')")
    private void updateCompany(@RequestBody String body) throws JsonProcessingException, CouldNotGetCompanyException {
        companyRegister.updateCompany(makeCompany(body));
    }

    /**
     * Makes a new Company.
     * @param body the json object.
     * @return the company from the json object.
     * @throws JsonProcessingException Should be cast if the json does not contain right format.
     */
    private Company makeCompany(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, Company.class);
    }


    @ExceptionHandler(CouldNotRemoveCompanyException.class)
    private ResponseEntity<String> handleCouldNotRemoveCompanyException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CouldNotAddCompanyException.class)
    private ResponseEntity<String> handleCouldNotAddCompanyException(Exception ex) {
        return ResponseEntity.status(HttpStatus.IM_USED).body(ex.getMessage());
    }

    /**
     * Check if an object is null.
     * @param object the object you want to do a check on.
     * @param error the error message the exception should have.
     */
    private void checkIfObjectIsNull (Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
