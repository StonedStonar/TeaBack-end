package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a register that has the database as its implementation.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@Service
public class CompanyJPA implements CompanyRegister {

    private CompanyRepository companyRepository;

    /**
     * Makes an instance of the CompanyJPA class.
     * @param companyRepository the company repository that works as our connection to the database.
     */
    public CompanyJPA(CompanyRepository companyRepository) {
        checkIfObjectIsNull(companyRepository, "the company repository");
        this.companyRepository = companyRepository;
    }

    @Override
    public void addCompany(Company company) throws CouldNotAddCompanyException {
        checkIfCompanyIsValid(company);
        if(!companyRepository.existsById(company.getCompanyID())) {
            companyRepository.save(company);
        } else {
            throw new CouldNotAddCompanyException("The companyID is already in use. (have to be unique to be added)");
        }
    }

    @Override
    public void removeCompany(Company company) throws CouldNotRemoveCompanyException {
        checkIfCompanyIsValid(company);
        removeCompanyWithCompanyID(company.getCompanyID());
    }

    @Override
    public void removeCompanyWithCompanyID(long companyID) throws CouldNotRemoveCompanyException {
        checkIfNumberIsAboveZero(companyID, "companyID");
        if (companyRepository.existsById(companyID)) {
            companyRepository.deleteById(companyID);
        } else {
            throw new CouldNotRemoveCompanyException("The company with this id " + companyID +
                " is not found in the system.");
        }
    }

    @Override
    public Company getCompanyWithId(long companyID) throws CouldNotGetCompanyException {
        checkIfNumberIsAboveZero(companyID, "companyID");
        Optional<Company> optionalCompany = companyRepository.findById(companyID);
        if (optionalCompany.isEmpty()) {
            throw new CouldNotGetCompanyException("The company with this id " + companyID + " is not found in the system.");
        }
        return optionalCompany.get();
    }

    @Override
    public void updateCompany(Company company) throws CouldNotGetCompanyException {
        checkIfCompanyIsValid(company);
        if (companyRepository.existsById(company.getCompanyID())) {
            companyRepository.save(company);
        } else {
            throw new CouldNotGetCompanyException("the company with id " + company.getCompanyID() + " is not found in the system.");
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        Iterable<Company> it = companyRepository.findAll();
        it.forEach(company -> companies.add(company));
        return companies;
    }

    /**
     * Check if a company is valid and not null.
     * @param company the company we want to check.
     */
    private void checkIfCompanyIsValid(Company company) {
        checkIfObjectIsNull(company, "company  was expected");
    }

    /**
     * Checks if the input number is above zero.
     * @param numberToCheck the number to check.
     */
    private void checkIfNumberIsAboveZero(long numberToCheck, String prefix){
        if (numberToCheck <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be larger than zero.");
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
