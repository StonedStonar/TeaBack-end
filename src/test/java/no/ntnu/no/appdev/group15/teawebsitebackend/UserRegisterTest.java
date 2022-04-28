package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.Application;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotLoginToUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
//Setter at klassen sakl kjøres av Application.class
@SpringBootTest(classes = Application.class)
//Setter profilen som
@ActiveProfiles("test")
public class UserRegisterTest {

    private UserRegister userRegister;

    private String prefixIllegal;

    private User user;

    private StringBuilder stringBuilder;

    private int errors;


    /**
     * Makes an instance of the UserJPATest class.
     * @param userJPA the connection to the database.
     */
    @Autowired
    public UserRegisterTest(UserJPA userJPA) {
        checkIfObjectIsNull(userJPA, "user JPA");
        this.userRegister = userJPA;
        prefixIllegal = makeExceptionString("IllegalArgumentException");
    }

    /**
     * Makes an exception into the wanted string.
     * @param exceptionName the name of the exception.
     * @return the full exception string.
     */
    private String makeExceptionString(String exceptionName){
        return "Expected to get a " +  exceptionName + " since";
    }

    @BeforeEach
    public void addTestData(){
        try {
            for (User user : userRegister.getAllUsers()) {
                userRegister.removeUser(user);
            }
            Address address = makeValidAddress();
            Address address1 = new Address(2900, "Fagernes", "Fagernesvegen", 33, "Norge");
            userRegister.addUser(new User("Bjarne", "Bjarnesen", address, "bjarne@bjarn.com", "pass",12345678 , Role.ROLE_ADMIN));
            userRegister.addUser(new User("Kjell", "Fjell", address1, "kjell@hoyfjell.com", "passs",72345678 , Role.ROLE_USER));
            //Since every user gets a new ID when they are in i just say that i want the first user when all is done.
            user = userRegister.getAllUsers().get(0);
        }catch (IllegalArgumentException | CouldNotAddUserException | CouldNotRemoveUserException exception){
            fail("Expected the test data to be added before each test and not a " + exception.getClass().getSimpleName());
        }
        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Makes a valid address that is used in testing.
     * @return the address object.
     */
    private Address makeValidAddress(){
        return new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge");
    }

    /**
     * Adds a new error to the string builder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(" ").append(error);
        errors++;
    }

    /**
     * Adds an error with an exception in the title.
     * @param errorPrefix what it should say before the main error.
     * @param error what it should say after the error.
     * @param exception the exception that was not expected.
     */
    private void addErrorWithException(String errorPrefix, String error, Exception exception){
        addError(errorPrefix, error);
        stringBuilder.append(" and not a ").append(exception.getClass().getSimpleName());
    }

    /**
     * Checks if the tests failed and displays the results.
     */
    private void checkIfTestsFailedAndDisplayResult(){
        if(stringBuilder.length() == 0){
            assertTrue(true);
        }else {
            fail("\nAmount of errors " + errors + " listed errors: " + stringBuilder.toString());
        }
    }

    /**
     * Makes a user that is not in the register by default.
     * @return the user.
     */
    private User makeUserNotInRegister(){
        return new User("Pepe", "Clap", makeValidAddress(), "pepe@pepe.com", "pass",12345668 ,Role.ROLE_USER);
    }

    /**
     * Tests if the addUser method works with invalid input.
     */
    @Test
    @DisplayName("Tests if the add method works with invalid input.")
    public void testIfAddUserWorksWithInvalidInput(){
        try {
            userRegister.addUser(null);
            addError(prefixIllegal, "the input is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotAddUserException exception){
            addErrorWithException(prefixIllegal, "the input is null", exception);
        }
        String duplicatePrefix = makeExceptionString("CouldNotAddUserException");
        try {
            userRegister.addUser(user);
            addError(duplicatePrefix, "the user is already in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException(duplicatePrefix, "the user is already in the system", exception);
        }catch (CouldNotAddUserException exception){
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if the addUser method works with valid input.
     */
    @Test
    @DisplayName("Tests if the addUser method works with valid input.")
    public void testIfAddUserWorksWithValidInput(){
        try {
            userRegister.addUser(makeUserNotInRegister());
        }catch (IllegalArgumentException | CouldNotAddUserException exception){
            addErrorWithException("Expected the method to work since", "the input user is not in the system", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeUser works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeUser works with invalid input.")
    public void testIfRemoveUserWorksWithInvalidInput(){
        try {
            userRegister.removeUser(null);
            addError(prefixIllegal, "the input is null");
        } catch (IllegalArgumentException exception){

        } catch (CouldNotRemoveUserException exception) {
            addErrorWithException(prefixIllegal, "the input is null", exception);
        }
        try {
            userRegister.removeUser(makeUserNotInRegister());
            addError(prefixIllegal, "the input user is not in the system");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotRemoveUserException exception){
            addErrorWithException(prefixIllegal, "the input user is not in the system", exception);
        }

        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeUser works with valid input.
     */
    @Test
    @DisplayName("Tests if removeUser works with valid input.")
    public void testIfRemoveUserWorksWithValidInput(){
        try {
            userRegister.removeUser(user);
        }catch (IllegalArgumentException | CouldNotRemoveUserException exception){
            addErrorWithException("Expected the method to work since", "the input is valid", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeUserWithID works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeUserWithID works with invalid input.")
    public void testIfRemoveUserWithIDWorksWithInvalidInput(){
        try {
            userRegister.removeUserWithID(-5);
            addError(prefixIllegal, "the input number is negative");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotRemoveUserException exception){
            addErrorWithException(prefixIllegal, "the input number is negative", exception);
        }
        String removePrefix = makeExceptionString("CouldNotAddUserException");
        try{
            userRegister.removeUserWithID(50000L);
            addError(removePrefix, "the input id is not used");
        }catch (IllegalArgumentException exception){
            addErrorWithException(removePrefix, "the input id is not used", exception);
        }catch (CouldNotRemoveUserException exception){
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if removeUserWithID works with valid input.
     */
    @Test
    @DisplayName("Tests if removeUserWithID works with invalid input.")
    public void testIfRemoveUserWithIdWorksWithValidInput(){
        try {
            userRegister.removeUserWithID(user.getUserId());
        }catch (IllegalArgumentException | CouldNotRemoveUserException exception){
            addErrorWithException("Expected the user to be removed since", "the input is valid", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if loginToUser works with invalid input.
     */
    @Test
    @DisplayName("Tests if loginToUser works with invalid input.")
    public void testIfLoginToUserWorksWithInvalidInput(){
        try {
            userRegister.loginToUser("", "pass");
            addError(prefixIllegal, "the input email is empty");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotLoginToUserException exception){
            addErrorWithException(prefixIllegal, "the input email is empty", exception);
        }
        try {
            userRegister.loginToUser(null, "pass");
            addError(prefixIllegal, "the input email is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotLoginToUserException exception){
            addErrorWithException(prefixIllegal, "the input email is null", exception);
        }
        try {
            userRegister.loginToUser("bjarne@bjarnesen.com", "");
            addError(prefixIllegal, "the input password is empty");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotLoginToUserException exception){
            addErrorWithException(prefixIllegal, "the input password is empty", exception);
        }
        try {
            userRegister.loginToUser("bjarne@bjarnesen.com", null);
            addError(prefixIllegal, "the input password is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotLoginToUserException exception){
            addErrorWithException(prefixIllegal, "the input password is null", exception);
        }
        String loginPrefix = makeExceptionString("CouldNotLoginToUserException");
        try {
            User user2 = makeUserNotInRegister();
            userRegister.loginToUser(user2.getEmail(), user2.getPassword());
            addError(loginPrefix, "the input user is not in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException(loginPrefix, "the input user is not in the system", exception);
        }catch (CouldNotLoginToUserException exception){

        }

        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if loginToUser works with valid input.
     */
    @Test
    @DisplayName("Tests if loginToUser works with valid input.")
    public void testIfLoginToUserWorksWithValidInput(){
        try {
            userRegister.loginToUser(user.getEmail(), user.getPassword());
        }catch (IllegalArgumentException | CouldNotLoginToUserException exception){
            addErrorWithException("Expected the user to be logged in since the input is valid", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getUserWithID works with invalid input.
     */
    @Test
    @DisplayName("Tests if getUserWithID works with invalid input.")
    public void testIfGetUserWithIDWorksWithInvalidInput(){
        try {
            userRegister.getUserWithUserID(-5);
            addError(prefixIllegal, "the input is negative");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotGetUserException exception){
            addErrorWithException(prefixIllegal, "the input is negative", exception);
        }
        String getPrefix = makeExceptionString("CouldNotGetUserException");
        try {
            userRegister.getUserWithUserID(5000);
            addError(prefixIllegal, "the input id is not in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException(getPrefix, "the input id is not in the system", exception);
        }catch (CouldNotGetUserException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getUserWithID works with valid input.
     */
    @Test
    @DisplayName("Tests if getUserWithID works with valid input.")
    public void testIfGetUserWithIdWorksWithValidInput(){
        try {
            userRegister.getUserWithUserID(user.getUserId());
        }catch (IllegalArgumentException | CouldNotGetUserException exception){
            addErrorWithException("Expected to get a user since the input is valid", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }


    /**
     * Tests if getUserWithEmail works with invalid input.
     */
    @Test
    @DisplayName("Tests if getUserWithEmail works with invalid input.")
    public void testIfGetUserWithEmailWorksWithInvalidInput(){
        try {
            userRegister.getUserWithEmail("");
            addError(prefixIllegal, "the input is empty");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotGetUserException exception){
            addErrorWithException(prefixIllegal, "the input is empty", exception);
        }
        try {
            userRegister.getUserWithEmail(null);
            addError(prefixIllegal, "the input is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotGetUserException exception){
            addErrorWithException(prefixIllegal, "the input is null", exception);
        }
        String getPrefix = makeExceptionString("CouldNotGetUserException");
        try {
            userRegister.getUserWithEmail(makeUserNotInRegister().getEmail());
            addError(getPrefix, "the user is not in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException(getPrefix, "the user is not in the system", exception);
        }catch (CouldNotGetUserException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if getUserWithEmail works with valid input
     */
    @Test
    @DisplayName("Tests if getUserWithEmail works with valid input")
    public void testIfGetUserWithEmailWorksWithValidInput(){
        try {
            userRegister.getUserWithEmail(user.getEmail());
        }catch (IllegalArgumentException | CouldNotGetUserException exception){
            addErrorWithException("Expected to get the user since the input is valid", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if updateUser works with invalid input.
     */
    @Test
    @DisplayName("Tests if updateUser works with invalid input.")
    public void testIfUpdateUserWorksWithInvalidInput(){
        try {
            userRegister.updateUser(null);
            addError(prefixIllegal, "the input is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotGetUserException exception){
            addErrorWithException(prefixIllegal, "the input is null", exception);
        }
        String getPrefix = makeExceptionString("CouldNotGetUserException");
        try {
            userRegister.updateUser(makeUserNotInRegister());
            addError(getPrefix, "the user is not in the system");
        }catch (IllegalArgumentException exception){
            addErrorWithException(getPrefix, "the user is not in the system", exception);
        }catch (CouldNotGetUserException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if updateUser works with valid input.
     */
    @Test
    @DisplayName("Tests if updateUser works with valid input.")
    public void testIfUpdateUserWorksWithValidInput(){
        try {
            user.setFirstName("pepega");
            userRegister.updateUser(user);
        }catch (IllegalArgumentException | CouldNotGetUserException exception){
            addErrorWithException("Expecetd the user to be updated since the input is valid", "", exception);
        }
        checkIfTestsFailedAndDisplayResult();
    }






    /**
     * Checks if an object is null.
     *
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
