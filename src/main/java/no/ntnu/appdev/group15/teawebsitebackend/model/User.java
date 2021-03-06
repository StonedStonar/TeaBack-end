package no.ntnu.appdev.group15.teawebsitebackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotChangePasswordException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.regex.Pattern;

/**
 * Represents a user of the website.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "userID")
    private long userId;

    private String firstName;

    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Address.class)
    @JoinColumn(name = "addressID")
    private Address address;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private long phoneNumber;

    private boolean active;

    @Enumerated
    @Column(nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Cart.class)
    @JoinColumn(name = "cartID")
    private Cart cart;


    /**
     * Makes an instance of the User class.
     */
    public User() {
        cart = new Cart();
    }

    /**
     * Makes an instance of the user object.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param address the address of the user
     * @param email the email of the user.
     * @param phoneNumber the phone number of the user.
     * @param password the password of the user.
     * @param role the role of the user.
     * @throws IllegalArgumentException gets thrown if the input is invalid format.
     */
    //Todo: Skriv tester for Role parameteren.
    public User(String firstName, String lastName, Address address, String email, String password,long phoneNumber, Role role){
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);
        setEmail(email);
        checkIfObjectIsNull(role, "role");
        setPassword(password);
        checkIfLongIsAboveZero(phoneNumber, "phonenumber");
        this.userId = 0;
        this.active = true;
        this.role = role;
        this.phoneNumber = phoneNumber;
        cart = new Cart();
    }

    /**
     * Makes an instance of the user object.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param address the address of the user
     * @param email the email of the user.
     * @param phoneNumber the phone number of the user.
     * @param password the password of the user.
     * @throws IllegalArgumentException gets thrown if the input is invalid format.
     */
    @JsonCreator
    public User(@JsonProperty("userID") long userID, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                @JsonProperty("address") Address address, @JsonProperty("email") String email,@JsonProperty("phoneNumber") long phoneNumber , @JsonProperty("password") String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAddress(address);
        setPassword(password);
        checkIfLongIsAboveZero(phoneNumber, "phone number");
        this.phoneNumber = phoneNumber;
        role = Role.ROLE_USER;
        this.active = true;
        cart = new Cart();
    }


    /**
     * Makes an instance of the user object.
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param email the email of the user.
     * @param phoneNumber the phone number of the user.
     * @param password the password of the user.
     * @throws IllegalArgumentException gets thrown if the input is invalid format.
     */
    public User(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                @JsonProperty("email") String email,@JsonProperty("phoneNumber") long phoneNumber ,
                @JsonProperty("password") String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        checkIfLongIsAboveZero(phoneNumber, "phone number");
        this.phoneNumber = phoneNumber;
        role = Role.ROLE_USER;
        this.active = true;
        cart = new Cart();
    }

    /**
     * Sets the password of the user.
     * @param newPassword the new password.
     */
    private void setPassword(String newPassword){
        checkString(newPassword, "password");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(newPassword);
    }

    /**
     * Gets the phone number.
     * @return the phone number.
     */
    public long getPhoneNumber(){
        return phoneNumber;
    }

    /**
     * Gets the cart of this user.
     * @return the cart.
     */
    public Cart getCart(){
        return cart;
    }

    /**
     * Gets the role of the user.
     * @return the role of the user.
     */
    public Role getRole(){
        return role;
    }

    /**
     * Gets if the user is active.
     * @return <code>true</code> if the user is active.
     *         <code>false</code> if the user is not active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the password.
     * @return the password.
     */
    public String getPassword(){
        return password;
    }

    /**
     * Changes the password of the user to a new one.
     * @param oldPassword the old password.
     * @param newPassword the new password.
     * @throws CouldNotChangePasswordException gets thrown when the old password does not match the set password.
     * @throws IllegalArgumentException gets throw if one of the input passwords are empty or null.
     */
    public void changePassword(String oldPassword, String newPassword) throws CouldNotChangePasswordException {
        checkIfPasswordIsNotNullOrEmpty(newPassword);
        checkIfPasswordIsNotNullOrEmpty(oldPassword);
        if (checkIfPasswordsMatch(oldPassword)){
            setPassword(newPassword);
        }else {
            throw new CouldNotChangePasswordException("The old password does not match the set password.");
        }
    }

    /**
     * Checks if the input password matches the set password. This is case-sensitive.
     * @param passwordToCheck the password to check.
     * @return <code>true</code> if the input password matches the users set password.
     *         <code>false</code> if the input password does not match the users set password.
     * @throws IllegalArgumentException gets thrown if the input password is empty or null.
     */
    public boolean checkIfPasswordsMatch(String passwordToCheck){
        checkIfPasswordIsNotNullOrEmpty(passwordToCheck);
        return BCrypt.checkpw(passwordToCheck, password);
    }

    /**
     * Gets the user's id.
     * @return the user id.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Gets the first name.
     * @return the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName the new first name.
     */
    public void setFirstName(String firstName) {
        checkString(firstName, "first name");
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     * @return the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name to a new value.
     * @param lastName the new last name.
     * @throws IllegalArgumentException gets thrown when the input is empty or null.
     */
    public void setLastName(String lastName) {
        checkString(lastName, "last name");
        this.lastName = lastName;
    }

    /**
     * Gets the address object.
     * @return the address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the address to a new value.
     * @param address the new address.
     * @throws IllegalArgumentException gets thrown if the address is null.
     */
    public void setAddress(Address address) {
        checkIfObjectIsNull(address, "address");
        this.address = address;
    }

    /**
     * Gets the email of the user.
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email to a new value.
     * @param email the new email.
     * @throws IllegalArgumentException gets thrown if the email is empty or null.
     */
    public void setEmail(String email) {
        checkIfEmailIsNotInvalid(email);
        this.email = email;
    }

    /**
     * Sets the phone number.
     * @param phoneNumber the new phone number.
     */
    //Todo: Lag tester for denne
    public void setPhoneNumber(long phoneNumber){
        checkIfLongIsAboveZero(phoneNumber, "phone number");
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the full name of the user.
     * @return the full name.
     */
    public String getFullName(){
        return firstName + " " + lastName;
    }

    /**
     * Checks if the email is not an invalid value. Also checks that the email contains "@" and "."
     * @param email the email to check.
     * @throws IllegalArgumentException gets thrown if the email is invalid format.
     */
    private void checkIfEmailIsNotInvalid(String email){
        checkString(email, "email");
        String reg = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(reg);
        if (!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("The email must be the correct format.");
        }
    }

    /**
     * Checks if the password is not null or empty.
     * @param password the password to check.
     * @throws IllegalArgumentException gets thrown if the password is empty or null.
     */
    private void checkIfPasswordIsNotNullOrEmpty(String password){
        checkString(password, "password");
    }

    /**
     * Checks if the input long is above zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     * @throws IllegalArgumentException gets thrown if the number is below or equal to zero.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be above zero.");
        }
    }


    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
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
