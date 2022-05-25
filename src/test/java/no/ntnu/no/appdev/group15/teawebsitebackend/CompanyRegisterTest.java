package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.Application;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.CompanyDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kenneth Johansen Misund
 * @version 0.1
 */

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyRegisterTest {

    private CompanyRegister companyRegister;
    private String expectedError;
    private Company company;
    private StringBuilder stringBuilder;
    private int errors;

    /**
     * Makes an instance of the CompanyRegisterTest class.
     * @param companyJPA the connection to the database.
     */
    @Autowired
    public CompanyRegisterTest (CompanyJPA companyJPA)  {
        checkIfObjectIsNull(companyJPA, "companyJPA");
        this.companyRegister = companyJPA;
        expectedError = "Expected to get an IllegalArgumentException since";
    }

    /**
     * Cleans up the DB after the tests.
     */
    @AfterAll
    public void removeAllCompanies(){
        if (companyRegister instanceof CompanyJPA){
            List<Company> companies = companyRegister.getAllCompanies();
            System.out.println("REmoiving all companies");
            try {
                for (Company company1 : companies) {
                    companyRegister.removeCompany(company1);
                }
            }catch (CouldNotRemoveCompanyException exception){
                exception.printStackTrace();
            }
        }
    }

    /**
     * Adds some test data to be used for testing.
     */
    @BeforeEach
    public void addTestData() {
        try {
            for (Company company : companyRegister.getAllCompanies()) {
                companyRegister.removeCompany(company);
            }
            Company company = (new Company("Yogi Tea", new CompanyDetails("Best i Verden", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            Company company1 = (new Company("Yogi Tea", new CompanyDetails("Best i Verden", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            Company company2 = (new Company("Clipper Tea", new CompanyDetails("Tea of great quality", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            Company company3 = (new Company("Pukka Tea", new CompanyDetails("Veldig Salt Tea", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            Company company4 = (new Company("Fair Trade", new CompanyDetails("Vi er et firma som opprett holder orden, slik at tea blir solgt og opprettholdt inn for rettighetene.", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            companyRegister.addCompany(company);
            companyRegister.addCompany(company1);
            companyRegister.addCompany(company2);
            companyRegister.addCompany(company3);
            companyRegister.addCompany(company4);

            this.company = companyRegister.getAllCompanies().get(0);
        } catch (IllegalArgumentException | CouldNotAddCompanyException | CouldNotRemoveCompanyException ex) {
            fail ("Expected the test data to be added before each test and not a " + ex.getClass().getSimpleName());
        }
        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Adds a new error to the stringBuilder.
     * @param expectedError what error we should expect.
     * @param error the error to append.
     */
    private void addError(String expectedError, String error) {
        stringBuilder.append("\n").append(expectedError).append(" ").append(error);
        errors++;
    }

    /**
     * Adds an error with a relevant exception in the title.
     * @param expectedError what it should say before the main error appear.
     * @param error what it should say after the error.
     * @param ex the exception that was not expected.
     */
    private void addErrorWithException(String expectedError, String error, Exception ex) {
        addError(expectedError, error);
        stringBuilder.append(" and not a ").append(ex.getClass().getSimpleName());
    }

    /**
     * Checks if the tests failed and displays the results.
     */
    private void checkIfTestsFailedAndDisplayResult() {
        if (stringBuilder.length() == 0) {
            assertTrue(true);
        } else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }

    /**
     * Tests if the addCompany works with invalid input format.
     */
    @Test
    @DisplayName("Tests if the addCompany works with invalid input")
    public void testIfAddCompanyWorksWithInvalidInput() {
        try {
            companyRegister.addCompany(null);
            addError(expectedError, "the input is invalid since its null");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotAddCompanyException ex) {
            addErrorWithException(expectedError, "the input is null", ex);
        }
        String duplicatedErrorMessage = "Expected to get a couldNotAddCompanyException since";
        try {
            companyRegister.addCompany(company);
            addError(duplicatedErrorMessage, "the user is already in the system");
        } catch (IllegalArgumentException ex) {
            addErrorWithException(duplicatedErrorMessage, "the company already exist/found in the system", ex);
        } catch (CouldNotAddCompanyException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if addCompany works with valid input format.
     */
    @Test
    @DisplayName("Tests if addCompany works with valid input.")
    public void testIfAddCompanyWorksWithValidInput() {
        try {
            companyRegister.addCompany(new Company("Pukka Tea", new CompanyDetails("Veldig Salt Tea", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        } catch (IllegalArgumentException | CouldNotAddCompanyException ex) {
            addErrorWithException("Expected the add method to work since", "company input is not in the system and valid,", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeCompany works with invalid input format.
     */
    @Test
    @DisplayName("Test if removeCompany works with invalid input")
    public void testIfRemoveCompanyWorksWithInvalidInput() {
        try {
            companyRegister.removeCompany(null);
            addError(expectedError, "the input is null");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotRemoveCompanyException ex) {
            addErrorWithException(expectedError, "the input is null", ex);
        }
        try {
            companyRegister.removeCompany(new Company("Tea Pukka", new CompanyDetails("Tea Tea og meir Tea", new Address(2323, "Ørebro", "Skåne vegen", 21, "Sverige"))));
            addError(expectedError, "the company is not found in the system");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotRemoveCompanyException ex) {
            addErrorWithException(expectedError, "the company is not found in the system", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeProduct works with valid input.
     */
    @Test
    @DisplayName("Test if removeCompany works with valid input")
    public void testIfRemoveCompanyWorksWithValidInput() {
        try {
            companyRegister.removeCompany(company);
        } catch (IllegalArgumentException | CouldNotRemoveCompanyException ex) {
            addErrorWithException("Expected the remove method to work since", "the input is of valid format and there should companies in the register", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeCompanyWithCompanyID works with invalid input format.
     */
    @Test
    @DisplayName("Tests if removeCompanyWithCompanyID works with invalid input")
    public void testIfRemoveCompanyWithIDWorksWithInvalidInput() {
        try {
            companyRegister.removeCompanyWithCompanyID(-4);
            addError(expectedError, "the input number is of negative value");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotRemoveCompanyException ex) {
            addErrorWithException(expectedError, "the input number is of negative values", ex);
        }
        String removeException = "Expected to get a CouldNotRemoveCompanyException since";
        try {
            companyRegister.removeCompanyWithCompanyID(23412512L);
            addError(removeException, "the input id for CompanyID is never used");
        } catch (IllegalArgumentException ex) {
            addErrorWithException(removeException, "the input id for CompanyID is never used", ex);
        } catch (CouldNotRemoveCompanyException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if removeCompanyWithCompanyID works with valid input.
     */
    @Test
    @DisplayName("Tests if removeCompanyWithCompanyID works with valid input")
    public void testIfRemoveCompanyWithCompanyIDWorksWithValidInput() {
        try {
            companyRegister.removeCompanyWithCompanyID(company.getCompanyID());
        } catch (IllegalArgumentException | CouldNotRemoveCompanyException ex) {
            addErrorWithException("Expeceted the company to be removed since ", "the input is valid", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getCompanyWithID works with invalid input.
     */
    @Test
    @DisplayName("Tests if getCompanyWithID works with invalid input")
    public void testIfGetCompanyWithIDWorksWithInvalidInput() {
        try {
            companyRegister.getCompanyWithId(0);
            addError(expectedError, "the input is 0");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotGetCompanyException ex) {
            addErrorWithException(expectedError, "the input is 0", ex);
        }
        try {
            companyRegister.getCompanyWithId(-12);
            addError(expectedError, "the input is of negative value");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotGetCompanyException ex) {
            addErrorWithException(expectedError, "the input is of negative value", ex);
        }
        try {
            companyRegister.getCompanyWithId(80000);
            addError("Expected to get a CouldNotGetCompanyException since ", "the companyID is not in the system.");
        } catch (IllegalArgumentException ex) {
            addErrorWithException("Expected to get a CouldNotGetCompanyException since ", "the companyID is not in the system.", ex);
        } catch (CouldNotGetCompanyException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if getCompanyWithID works with valid input.
     */
    @Test
    @DisplayName("Tests if getCompanyWithID works with valid input")
    public void testIfGetCompanyWithIDWorksWithValidInput() {
        try {
            companyRegister.getCompanyWithId(company.getCompanyID());
        } catch (IllegalArgumentException | CouldNotGetCompanyException ex) {
            addErrorWithException("Expected to get the company since the input should be of valid format", "", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if updateCompany works with invalid input.
     */
    @Test
    @DisplayName("Tests if updateCompany works with invalid input")
    public void testIfUpdateCompanyWithInvalidInput() {
        try {
            companyRegister.updateCompany(null);
            addError(expectedError, "the input is null");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotGetCompanyException ex) {
            addErrorWithException(expectedError, "the input is null", ex);
        }
        String getException = "Expected to get a CouldNotGetCompanyException";
        try {
            companyRegister.updateCompany(new Company(12221,"Tea Pukka", new CompanyDetails("Tea Tea og meir Tea", new Address(2323, "Ørebro", "Skåne vegen", 21, "Sverige"))));
            addError(expectedError, "the company do not exist in the system and cannot then be updated.");
        } catch (IllegalArgumentException ex) {
            addErrorWithException(getException, "the company is not found/exist in the system", ex);
        } catch (CouldNotGetCompanyException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if updateCompany works with valid input format.
     */
    @Test
    @DisplayName("tests if updateCompany works with valid input")
    public void testIfUpdateCompanyWorksWithValidInput() {
        try {
            String newCompanyName = "Slushy Slush Tea";
            company.setCompanyName(newCompanyName);
            companyRegister.updateCompany(company);
            assertEquals(companyRegister.getCompanyWithId(company.getCompanyID()).getCompanyName(), newCompanyName);
        } catch (IllegalArgumentException | CouldNotGetCompanyException ex) {
            addErrorWithException("Expected the company to be updated since the input should be of valid format", "", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the error should contain.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
