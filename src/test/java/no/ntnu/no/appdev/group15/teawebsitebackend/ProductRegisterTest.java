package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.Application;
import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRegisterTest {

    private CompanyJPA companyJPA;

    private UserJPA userJPA;

    private ProductRegister productRegister;
    private String expectedError;
    private Product product;
    private StringBuilder stringBuilder;
    private int errors;

    private User user;

    private List<Company> companies;

    /**
     * Makes an instance of the ProductRegisterTest class.
     * @param productJPA the connection to the database.
     * @param companyJPA the connection to the database.
     * @param userJPA the connection to the database.
     */
    @Autowired
    public ProductRegisterTest (ProductJPA productJPA, CompanyJPA companyJPA, UserJPA userJPA) {
        checkIfObjectIsNull(productJPA, "productJPA");
        checkIfObjectIsNull(companyJPA, "companyJPA");
        checkIfObjectIsNull(userJPA, "userJPA");
        this.productRegister = productJPA;
        this.companyJPA = companyJPA;
        this.userJPA =  userJPA;
        if (companyJPA.getAllCompanies().isEmpty()){
            List<Company> companies = makeCompanies();
            try {
                for (Company company : companies) {
                    companyJPA.addCompany(company);
                }
                if (userJPA.getAllUsers().isEmpty()){
                    userJPA.addUser(new User("Bjarne", "Bjarnesen", new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge"), "bjarne@bjarn.com", "pass",12345678 , Role.ROLE_ADMIN));
                }
            }catch (CouldNotAddCompanyException | CouldNotAddUserException exception){
                fail("Expected the test data to be added since its valid and not a " + expectedError.getClass().getSimpleName());
            }
        }
        this.companies = companyJPA.getAllCompanies();
        this.user = userJPA.getAllUsers().get(0);
        expectedError = "Expected to get an IllegalArgumentException since ";
    }

    /**
     * Cleans up the databases.
     */
    @AfterAll
    public void cleanUp(){
        try {
            for (Product product1 : productRegister.getAllProducts()) {
                productRegister.removeProduct(product1);
            }
            for (Company company : companyJPA.getAllCompanies()) {
                companyJPA.removeCompany(company);
            }
            for (User user : userJPA.getAllUsers()){
                userJPA.removeUser(user);
            }
        } catch (CouldNotRemoveCompanyException | CouldNotRemoveProductException | CouldNotRemoveUserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a list with test companies.
     * @return the list with the companies.
     */
    private List<Company> makeCompanies(){
        List<Company> companies = new ArrayList<>();
        companies.add(new Company("PepeINC", new CompanyDetails("pepe", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        companies.add(new Company("LuliClapAS", new CompanyDetails("Luli", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        companies.add(new Company("CompanyINC", new CompanyDetails("Nja", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        return companies;
    }

    /*
     * Adds some test data to be used for testing.
     */
   @BeforeEach
   public void addTestData() {
        try {
            for (Product product : productRegister.getAllProducts()) {
                productRegister.removeProduct(product);
            }
            Product product = new Product( "Green Leaf Tea", 1199, 7, new ProductDetails("Lul", "Tea", "p"), companies.get(0));
            Product product1 = new Product( "Black water Leaf Tea", 999, 3, new ProductDetails("Lul", "Tea", "p"), companies.get(1));
            Product product2 = new Product( "Mushroom High Tea", 699, 2, new ProductDetails("Lul", "Tea", "p"), companies.get(2));
            Product product3 = new Product( "Blue Herb Tea", 599, 1, new ProductDetails("Lul", "Tea", "p"), companies.get(0));
            product.addReview(new Review("Lul", "clap", user, "Good good", LocalDate.now(), 4));
            productRegister.addProduct(product);
            productRegister.addProduct(product1);
            productRegister.addProduct(product2);
            productRegister.addProduct(product3);

            this.product = productRegister.getAllProducts().get(0);
        } catch (IllegalArgumentException | CouldNotAddProductException | CouldNotRemoveProductException | CouldNotAddReviewException ex) {
            fail("Expected the test data to be added before each test and not a " + ex.getClass().getSimpleName());
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
     * Adds an error with an exception in the title.
     * @param expectedError what it should say before the main error.
     * @param error what it should say after the error.
     * @param exception the exception that was not expected.
     */
   private void addErrorWithException(String expectedError, String error, Exception exception) {
        addError(expectedError, error);
        stringBuilder.append(" and not a ").append(exception.getClass().getSimpleName());
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
     * Tests if the addProduct method works with invalid input.
     */
    @Test
    @DisplayName("Tests if the addProducts works with invalid input.")
    public void testIfAddProductWorksWithInvalidInput() {
        try {
            productRegister.addProduct(null);
            addError(expectedError, "the input is invalid since its null");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotAddProductException ex) {
            addErrorWithException(expectedError, "the input is null", ex);
        }

        String duplicateErrorMessage = "Expected to get a CouldNotAddProductException since";
        try {
            productRegister.addProduct(product);
            addError(duplicateErrorMessage, "the user is already in the system");
        } catch (IllegalArgumentException ex) {
            addErrorWithException(duplicateErrorMessage, "the product already exist in the system", ex);
        } catch (CouldNotAddProductException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if the addProduct method works with valid input.
     */
    @Test
    @DisplayName("Tests if addProduct works with valid input.")
    public void testIfAddProductWorksWithValidInput() {
        try {
            productRegister.addProduct(new Product("Green Leaf Tea", 119, 7, new ProductDetails("Lul", "Tea", "p"), companies.get(0)));
        } catch (IllegalArgumentException | CouldNotAddProductException ex) {
            addErrorWithException("Expected the add method to work since", "the input product is not in the system.", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeProduct works with invalid input.
     */
    @Test
    @DisplayName("Tests if the removeProduct works with invalid input.")
    public void testIfRemoveProductWorksWithInvalidInput() {
        try {
           productRegister.removeProduct(null);
           addError(expectedError, "the input is null");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotRemoveProductException ex) {
            addErrorWithException(expectedError, "the input is null", ex);
        }
        try {
            productRegister.removeProduct(new Product("Pepehang hard", 10000, 3, new ProductDetails("Lul", "Tea", "p"), new Company()));
                addError(expectedError, "the product is not found in the system");
            } catch (IllegalArgumentException ex) {

            } catch (CouldNotRemoveProductException ex) {
            addErrorWithException(expectedError, "the product is not found in the system", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeProduct works with valid input.
     */
    @Test
    @DisplayName("Tests if the removeProduct works with valid input")
    public void testIfRemoveProductWorksWithValidInput() {
        try {
            productRegister.removeProduct(product);
        } catch (IllegalArgumentException | CouldNotRemoveProductException ex) {
            addErrorWithException("Expected the remove method to work since", "the input is of valid format", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeProductWithProductID works with invalid input format.
     */
    @Test
    @DisplayName("Tests if removeProductWithProductID works with invalid input format.")
    public void testIfRemoveProductWithProductIDWorksWithInvalidFormat() {
        try {
            productRegister.removeProductWithProductID(-9);
            addError(expectedError, "the input number is of negative value");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotRemoveProductException ex) {
            addErrorWithException(expectedError, "the input number is of negative value", ex);
        }
        String removeExpectedError = "Expected to get a CouldNotRemoveProductException since";
        try {
            productRegister.removeProductWithProductID(111010L);
            addError(removeExpectedError, "the input id is never used");
        } catch (IllegalArgumentException ex) {
            addErrorWithException(removeExpectedError, "the input id is never used", ex);
        } catch (CouldNotRemoveProductException ex) {

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeProductWithProductID works with valid input format.
     */
    @Test
    @DisplayName("Tests if removeProductWithProductID works with valid input format.")
    public void testIfRemoveProductWithProductIDWorksWithValidFormat() {
        try {
            productRegister.removeProductWithProductID(product.getProductID());
        } catch (IllegalArgumentException | CouldNotRemoveProductException ex) {
            addErrorWithException("Expected the product to be removed since ", "the input is valid", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getProductWithID works with invalid input.
     */
    @Test
    @DisplayName("Tests if getProductWithID works with invalid input")
    public void testIfGetProductWithIDWorksWithInvalidInput() {
        try {
            productRegister.getProduct(0);
            addError(expectedError, "the input is 0");
        } catch (IllegalArgumentException ex) {

        }catch (CouldNotGetProductException ex) {
            addErrorWithException(expectedError, "the input is 0", ex);
        }
        try {
            productRegister.getProduct(-8);
            addError(expectedError, "the input is of negative value");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotGetProductException ex) {
            addErrorWithException(expectedError, "the input is of negative value", ex);
        }
        try {
            productRegister.getProduct(90000);
            addError("Expected to get a CouldNotGetProductException since", "the id is not in the system.");
        } catch (IllegalArgumentException ex) {
            addErrorWithException("Expected to get a CouldNotGetProductException since", "the id is not in the system.", ex);
        } catch (CouldNotGetProductException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getProductWithID works with valid input
     */
    @Test
    @DisplayName("Tests if getProductWithId works with valid input")
    public void testIfGetProductWithIDWorksWithValidInput() {
        try {
            productRegister.getProduct(product.getProductID());
        } catch (IllegalArgumentException | CouldNotGetProductException ex) {
            addErrorWithException("Expected to get the product since the input is of valid format", "", ex);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if updateProduct works with invalid input.
     */
    @Test
    @DisplayName("Tests if updateProduct works with invalid input")
    public void testIfUpdateWorksWithInvalidInput() {
        try {
            productRegister.updateProduct(null);
            addError(expectedError, "the input is null");
        } catch (IllegalArgumentException ex) {

        } catch (CouldNotGetProductException ex) {
            addErrorWithException(expectedError, "the input is null", ex);
        }
        String getException = "Expected to get a CouldNotGetProductException";
        try {
            productRegister.updateProduct(new Product(90000, "Pepeppepepe", 2992, 2, new ProductDetails("Lul", "Tea", "p"), new Company()));
            addError(expectedError, "the product do not exist in the system");
        } catch (IllegalArgumentException ex) {
            addErrorWithException(getException, "the product do not exist in the system", ex);
        } catch (CouldNotGetProductException ex) {
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Test if updateProduct works with valid input format.
     */
    @Test
    @DisplayName("Tests if updateProduct works with valid input")
    public void testIfUpdateWorksWithValidInput() {
        try {
            String newProductName = "Chuck is here but not Norris";
            product.setProductName(newProductName);
            productRegister.updateProduct(product);
            assertEquals(productRegister.getProduct(product.getProductID()).getProductName(), newProductName);
        } catch (IllegalArgumentException | CouldNotGetProductException ex) {
            addErrorWithException("Expected the product to be updated since the input should be of valid format", "", ex);
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
