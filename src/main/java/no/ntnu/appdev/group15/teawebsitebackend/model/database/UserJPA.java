package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotLoginToUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a connection to an Object-Relational to the database.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Service
public class UserJPA implements no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister {

    private UserRepository userRepository;

    /**
     * Makes an instance of the UserJPA class.
     * @param userRepository the user repository that is the connection to the DB.
     */
    public UserJPA(UserRepository userRepository) {
        checkIfObjectIsNull(userRepository, "user repository");
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) throws CouldNotAddUserException {
        checkIfUserIsValid(user);
        //Todo: Endre denne quickfixen senere. I JUST WANNA SLEEP SEND HELP
        Optional<User> opUs = userRepository.getUserByEmail(user.getEmail());
        if (!userRepository.existsById(user.getUserId()) && opUs.isEmpty()){
            userRepository.save(user);
        }else {
            throw new CouldNotAddUserException("The user id is already used.");
        }
    }

    @Override
    public void removeUser(User user) throws CouldNotRemoveUserException {
        checkIfUserIsValid(user);
        removeUserWithID(user.getUserId());
    }

    @Override
    public void removeUserWithID(long userID) throws CouldNotRemoveUserException {
        checkIfNumberIsAboveZero(userID, "user id");
        if(userRepository.existsById(userID)){
            userRepository.deleteById(userID);
        }else {
            throw new CouldNotRemoveUserException("The user with id " + userID + " is not in the system.");
        }

    }

    @Override
    public User loginToUser(String email, String password) throws CouldNotLoginToUserException, CouldNotGetUserException {
        checkString(email, "email");
        checkString(password, "password");
        User user = getUserWithEmail(email);
        if (!user.checkIfPasswordsMatch(password)){
            throw new CouldNotLoginToUserException("The email and password does not match any user in the system.");
        }
        return user;
    }

    @Override
    public User getUserWithUserID(long userID) throws CouldNotGetUserException {
        checkIfNumberIsAboveZero(userID, "userID");
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isEmpty()){
            throw new CouldNotGetUserException("The user with the id " + userID + " is not in the system.");
        }
        return optionalUser.get();
    }

    @Override
    public User getUserWithEmail(String email) throws CouldNotGetUserException {
        checkString(email, "email");
        Optional<User> optionalUser = userRepository.getUserByEmail(email);
        if (optionalUser.isEmpty()){
            throw new CouldNotGetUserException("The user with email " + email + " could not be located.");
        }
        return optionalUser.get();
    }

    @Override
    public void updateUser(User user) throws CouldNotGetUserException {
        checkIfUserIsValid(user);
        if (userRepository.existsById(user.getUserId())){
            userRepository.save(user);
        }else {
            throw new CouldNotGetUserException("The user with the id " + user.getUserId() + " is not in the system.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Iterable<User> it = userRepository.findAll();
        it.forEach(user -> users.add(user));
        return users;
    }

    /**
     * Checks if the user is not null.
     * @param user the user to check.
     * @throws IllegalArgumentException gets thrown when the user is null.
     */
    private void checkIfUserIsValid(User user){
        checkIfObjectIsNull(user, "user");
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
     * Checks if a string is of a valid format or not.
     *
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
