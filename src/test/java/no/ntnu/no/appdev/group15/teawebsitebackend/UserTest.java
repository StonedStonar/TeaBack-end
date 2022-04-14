package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotChangePasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the user class.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class UserTest {

    private User user;

    private StringBuilder stringBuilder;

    private int errors;

    /**
     * Sets up the test user for testing.
     */
    @BeforeEach
    public void setUpTestUser(){
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "lars@testesen.com", "pass", Role.ROLE_USER);
        }catch (IllegalArgumentException exception){
            fail("Expected the user to be made since the input is invalid. ");
        }
        //The stuff under is used for my new testing. For the old style we only need those above.

        stringBuilder = new StringBuilder();
        errors = 0;
    }

    /**
     * Adds a new error to the stringbuilder.
     * @param errorPrefix what it should say before the error.
     * @param error the error to append.
     */
    private void addError(String errorPrefix, String error){
        stringBuilder.append("\n").append(errorPrefix).append(error);
        errors++;
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

    //Todo: Under har vi to metoder som sjekker om setFirstName funker som forventet på en mer forståelig måte.
    // Både negativ og positiv test. Se på kommentarene og dokumentasjon for forklaringer

    /**
     * Tests if setFirstName works with invalid input.
     * Negative test. Tests wrong input.
     */
    @Test
    @DisplayName("Tests if setFirstName works with invalid input.")
    public void testIfSetFirstNameWorksWithInvalidInput(){
        try {
            //Setter navnet til tom streng. Da burde vi få en exception.
            user.setFirstName("");
            //Om vi ikke får en feil går den videre nedover ellers stopper den.
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            //The test is ok since the exception got thrown. Then we just assert true.
            assertTrue(true);
        }
    }

    /**
     * Tests if setFirstName works with valid input.
     * Positive test. Tests a scenario where the input is correct.
     */
    @Test
    @DisplayName("Tests if setFirstName works with valid input.")
    public void testIfSetFirstNameWorksWithValidInput(){
        try {
            //Nye navnet.
            String newName = "bjarne";
            //Prøver å sette det nye navnet under.
            user.setFirstName(newName);
            //Om vi ikke får en exception så sjekker vi at det nye navnet faktisk ble satt ved å se om de er det samme navnet.
            assertEquals(newName, user.getFirstName());
        }catch (IllegalArgumentException exception){
            //Skjer om vi får en IllegalArgumentException. Burde ikke komme siden navnet faktisk er valid.
            fail("Expected the name to be changed since the input is invalid.");
        }
    }

    //Todo: Eksemplene er bare de to metodene over. Resten er en ny og mer kompleks versjon som lar meg teste det samme med mindre plass og flere tester.

    /**
     * Checks if the constructor works with invalid parameters.
     */
    @Test
    @DisplayName("Checks if the constructor works with invalid parameters")
    public void testIfConstructorWorksWithInvalidParameters(){
        String prefix = "Expected a IllegalArgumentException since ";
        try {
            user = new User("", "Bjarnesen", new Address(), "lars@testesen.com", "pass", Role.ROLE_USER);
            addError(prefix, "the input firstname is empty.");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User(null, "Bjarnesen", new Address(), "lars@testesen.com", "pass", Role.ROLE_USER);
            addError(prefix, "the input firstname is null.");
        }catch (IllegalArgumentException exception){
            //I do nothing in here since i except to get a exception. Only registers errors above the catch.
        }
        try {
            user = new User("Lars", "", new Address(), "lars@testesen.com", "pass", Role.ROLE_USER);
            addError(prefix, "the input last name is empty");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", null, new Address(), "lars@testesen.com", "pass", Role.ROLE_USER);
            addError(prefix, "the last name is null");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", null, "lars@testesen.com", "pass", Role.ROLE_USER);
            addError(prefix, "the address is null");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "", "pass", Role.ROLE_USER);
            addError(prefix, "the email is empty");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", new Address(), null, "pass", Role.ROLE_USER);
            addError(prefix, "the email is null");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "larstestesen.com", "pass", Role.ROLE_USER);
            addError(prefix, "the email is missing @");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "lars@testesencom", "pass", Role.ROLE_USER);
            addError(prefix, "the email is missing a domain like .com");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "lars@testesen.com", "", Role.ROLE_USER);
            addError(prefix, "the password is empty");
        }catch (IllegalArgumentException exception){}
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "lars@testesen.com", null, Role.ROLE_USER);
            addError(prefix, "the password is null.");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Checks if the constructor works with valid input.
     */
    @Test
    @DisplayName("Checks if the constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        String prefix = "Expected a IllegalArgumentException since ";
        User user = null;
        try {
            user = new User("Lars", "Bjarnesen", new Address(), "lars@testesen.com", "pass", Role.ROLE_USER);
            assertNotNull(user);
        }catch (IllegalArgumentException exception){
            addError(prefix, "the format is valid.");
        }
        checkIfTestsFailedAndDisplayResult();
        assertNotNull(user);
    }

    /**
     * Tests if setEmail works with invalid input.
     */
    @Test
    @DisplayName("Tests if setEmail works with invalid input.")
    public void testIfSetEmailWorksWithInvalidInput(){
        String prefix = "Expected a IllegalArgumentException since ";
        try {
            user.setEmail("");
            addError(prefix, "email is empty");
        }catch (IllegalArgumentException exception){}
        try {
            user.setEmail(null);
            addError(prefix, "email is null");
        }catch (IllegalArgumentException exception){}
        try {
            user.setEmail("larstestesen.com");
            addError(prefix, "is missing @");
        }catch (IllegalArgumentException exception){}
        try {
            user.setEmail("lars@testesencom");
            addError(prefix, "is missing domain like .com");
        }catch (IllegalArgumentException illegalArgumentException){}
        try {
            user.setEmail("larstestesencom");
            addError(prefix, "is missing both @ and domain like .com");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setEmail works with valid input.
     */
    @Test
    @DisplayName("Tests if setEmail works with valid input.")
    public void testIfSetEmailWorksWithValidInput(){
        try {
            String newEmail = "bjarne@test.com";
            user.setEmail(newEmail);
            assertEquals(user.getEmail(), newEmail);
        }catch (IllegalArgumentException exception){
            addError("Expected the email to be set to a new value since the input is invalid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setLastName works with invalid input.
     */
    @Test
    @DisplayName("Tests if setLastName works with invalid input.")
    public void testIfSetLastNameWorksWithInvalidInput(){
        String prefix = "Expected a IllegalArgumentException since ";
        try {
            user.setLastName("");
            addError(prefix, "the last name is empty.");
        }catch (IllegalArgumentException exception){}
        try {
            user.setLastName(null);
            addError(prefix, "the last name is null");
        }catch (IllegalArgumentException exception){}
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setLastName works with valid input
     */
    @Test
    @DisplayName("Tests if setLastName works with valid input")
    public void testIfSetLastNameWorksWithValidInput(){
        try {
            String lastname = "Helle";
            user.setLastName(lastname);
            assertEquals(user.getLastName(), lastname);
        }catch (IllegalArgumentException exception){
            addError("Expected the last name to be set since the input is valid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if changePassword works with invalid input.
     */
    @Test
    @DisplayName("Tests if changePassword works with invalid input.")
    public void testIfChangePasswordWorksWithInvalidInput(){
        String prefix = "Expected a IllegalArgumentException since ";
        try {
            user.changePassword("", "hei");
            addError(prefix, "the password is empty");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotChangePasswordException exception){
            addError(prefix, "the password is empty");
        }
        try {
            user.changePassword(null, "hei");
            addError(prefix, "the password is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotChangePasswordException exception){
            addError(prefix, "the password is null");
        }
        try {
            user.changePassword("pass", "");
            addError(prefix, "the new password is empty");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotChangePasswordException exception){
            addError(prefix, "the new password is empty");
        }
        try {
            user.changePassword("pass", null);
            addError(prefix, "the new password is null");
        }catch (IllegalArgumentException exception){

        }catch (CouldNotChangePasswordException exception){
            addError(prefix, "the new password is null");
        }
        String changePasswordPrefix = "Expected to get a CouldNotChangePasswordException since ";
        try {
            user.changePassword("pas", "hei");
            addError(changePasswordPrefix, "the password is not matching the set password");
        }catch (IllegalArgumentException exception){
            addError(changePasswordPrefix, "the password is not matching the set password");
        }catch (CouldNotChangePasswordException exception){

        }
        try{
            user.changePassword("PASS", "hei");
            addError(changePasswordPrefix, "the passwords does not match. Input password is in caps.");
        }catch (IllegalArgumentException exception){
            addError(changePasswordPrefix, "the passwords does not match. Input password is in caps.");
        }catch (CouldNotChangePasswordException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if changePassword works with valid input.
     */
    @Test
    @DisplayName("Tests if changePassword works with valid input.")
    public void testIfChangePasswordWorksWithValidInput(){
        try {
            user.changePassword("pass", "password");

        }catch (IllegalArgumentException | CouldNotChangePasswordException exception){
            addError("Expected the password to be changed since the input is valid", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setAddress works with invalid input.
     */
    @Test
    @DisplayName("Tests if setAddress works with invalid input.")
    public void testIfSetAddressWorksWithInvalidInput(){
        try {
            user.setAddress(null);
            addError("Expected to get a IllegalArgumentException since the address is null", "");
        }catch (IllegalArgumentException exception){

        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if setAddress works with valid input.
     */
    @Test
    @DisplayName("Tests if setAddress works with valid input.")
    public void testIfSetAddressWorksWithValidInput(){
        try {
            Address address = new Address();
            user.setAddress(address);
            assertTrue(address == user.getAddress());
        }catch (IllegalArgumentException exception){
            addError("Expected the address to be set since the input is valid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

    /**
     * Tests if checkIfPasswordsMatch works with invalid input.
     */
    @Test
    @DisplayName("Tests if checkIfPasswordsMatch works with invalid input.")
    public void testIfCheckIfPasswordsMatchWorksWithInvalidInput(){
        String prefix = "Expected to get a IllegalArgumentException since ";
        try {
            user.checkIfPasswordsMatch("");
            addError(prefix, " the input is empty");
        }catch (IllegalArgumentException exception){}
        try {
            user.checkIfPasswordsMatch(null);
            addError(prefix, " the input is null.");
        }catch (IllegalArgumentException exception){}
    }

    /**
     * Tests if checkIfPasswordsMatch works with valid input
     */
    @Test
    @DisplayName("Tests if checkIfPasswordsMatch works with valid input")
    public void testIfCheckIfPasswordsMatchWorksWithValidInput(){
        try {
            assertFalse(user.checkIfPasswordsMatch("hei"));
        }catch (IllegalArgumentException exception){
            addError("Expected to get false since the passwords does not match and the format is valid.", "");
        }
        try {
            assertTrue(user.checkIfPasswordsMatch("pass"));
        }catch (IllegalArgumentException exception){
            addError("Expected to get true since the passwords does match and the input is valid.", "");
        }
        checkIfTestsFailedAndDisplayResult();
    }

}
