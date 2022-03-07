package no.ntnu.appdev.group15.teawebsitebackend.model.registers;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.*;

import java.util.List;

/**
 * Represents a basic set of methods a company register should have.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface CompanyRegister {

    /**
     * Adds a company to the register.
     * @param company the company to add.
     * @throws CouldNotAddCompanyException gets thrown if the company could not be added.
     */
    void addCompany(Company company) throws CouldNotAddCompanyException;

    /**
     * Removes a company from the register.
     * @param company the company to remove.
     * @throws CouldNotRemoveCompanyException gets thrown if the company could not be located in the register.
     */
    void removeCompany(Company company) throws CouldNotRemoveCompanyException;

    /**
     * Removes a company with its company id.
     * @param companyID the id of the company.
     * @throws CouldNotRemoveCompanyException gets thrown if the company could not be located in the register.
     */
    void removeCompanyWithCompanyID(long companyID) throws CouldNotRemoveCompanyException;

    /**
     * Gets a company with the matching id.
     * @param companyID the id of the company.
     * @return the company that matches this company id.
     * @throws CouldNotGetCompanyException gets thrown if the company could not be located in the register.
     */
    Company getCompanyWithId(long companyID) throws CouldNotGetCompanyException;

    /**
     * Updates the fields of the company. The company ID must be in the register already to update an entity.
     * @param company the company to update the details of.
     * @throws CouldNotGetCompanyException gets thrown if the company could not be located in the register.
     */
    void updateCompany(Company company) throws CouldNotGetCompanyException;

    /**
     * Gets a list of all the companies in the register.
     * @return the list with all the companies.
     */
    List<Company> getAllCompanies();
}
