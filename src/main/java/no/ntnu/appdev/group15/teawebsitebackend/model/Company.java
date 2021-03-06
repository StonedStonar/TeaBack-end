package no.ntnu.appdev.group15.teawebsitebackend.model;

import javax.persistence.*;

/**
 * Represents all information about a company.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@Entity
public class Company {

    @Id
    @GeneratedValue
    private long companyID;

    private String companyName;

    @OneToOne(targetEntity = CompanyDetails.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "companyDetailsID")
    private CompanyDetails details;

    /**
     * Empty constructor for JPA
     */
    public Company() {
    }

    /**
     * Makes an instance of the Company class.
     * @param companyID The unique identified id for company.
     * @param companyName company name.
     * @param details details for a company.
     */
    public Company(long companyID, String companyName, CompanyDetails details) {
        checkIfNumberNotNegative(companyID, "company id");
        this.companyID = companyID;

        setCompanyName(companyName);

        checkIfObjectIsNull(details, "details");
        this.details = details;
    }

     /** Makes an instance of the Company class without the ID.
     * @param companyName company name.
     * @param details details for a company.
     */
    public Company(String companyName, CompanyDetails details){
        setCompanyName(companyName);
        checkIfObjectIsNull(details, "details");
        this.details = details;
    }

    /**
     * Get the unique companyID for a company.
     * @return companyID in long.
     */
    public long getCompanyID() {
        return companyID;
    }

    /**
     * Gets the company name.
     * @return company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        checkString(companyName, "Company name");
        this.companyName = companyName;
    }

    /**
     * Gets the details for a product.
     * @return the details given.
     */
    public CompanyDetails getDetails() {
        return details;
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Check to make sure that integer values cannot go bellow 0. (Negative value)
     * @param number the object to be checked.
     * @param errorMessage exception to be displayed.
     */
    public void checkIfNumberNotNegative(long number, String errorMessage) {
        if(number <= 0) {
            throw new IllegalArgumentException("The " + errorMessage + " cannot be of negative value.");
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
