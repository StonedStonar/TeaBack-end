package no.ntnu.appdev.group15.teawebsitebackend.model.registers;

import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotLoginToUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveUserException;

import java.util.List;

/**
 * Represents a basic set of methods a user register should have.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface UserRegister {

    /**
     * Adds a user to the register.
     * @param user the user to add.
     * @throws CouldNotAddUserException gets thrown if the user could not be added.
     */
    void addUser(User user) throws CouldNotAddUserException;

    /**
     * Removes a user from the register.
     * @param user the user to remove.
     * @throws CouldNotRemoveUserException gets thrown if the user could not be located.
     */
    void removeUser(User user) throws CouldNotRemoveUserException;

    /**
     * Removes a user with the user id.
     * @param user the user to remove.
     * @throws CouldNotRemoveUserException gets thrown if the user could not be located.
     */
    void removeUserWithID(long user) throws CouldNotRemoveUserException;

    /**
     * Logs into a user that has that email and password.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return the user that matches the mail and password.
     * @throws CouldNotLoginToUserException gets thrown if the email or password does not match anyone in the register or the set details.
     * @throws CouldNotGetUserException gets thrown if there is no user with that email in the system.
     */
    User loginToUser(String email, String password) throws CouldNotLoginToUserException, CouldNotGetUserException;

    /**
     * Gets a user with its user id.
     * @param userID the id of the user.
     * @return the user that has this id.
     * @throws CouldNotGetUserException gets thrown if the user could not be located.
     */
    User getUserWithUserID(long userID) throws CouldNotGetUserException;

    /**
     * Gets the user that matches the input email.
     * @param email the email to look for.
     * @return a user that has this email.
     * @throws CouldNotGetUserException gets thrown if there is no user with this email in the system.
     */
    User getUserWithEmail(String email) throws CouldNotGetUserException;

    /**
     * Updates a users fields. Only the ID cannot be altered.
     * @param user the user to update.
     * @throws CouldNotGetUserException gets thrown if the user could not be located.
     */
    void updateUser(User user) throws CouldNotGetUserException;

    /**
     * Gets all the users in the register.
     * @return a list with all the users.
     */
    List<User> getAllUsers();
}
