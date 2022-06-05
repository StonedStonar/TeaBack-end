package no.ntnu.appdev.group15.teawebsitebackend.security;

import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A class used to get users for security.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Service
public class AccessUserJPA implements UserDetailsService {

    private UserJPA userJPA;

    /**
     * Makes an instance of the AccessUserJPA class.
     */
    public AccessUserJPA(UserJPA userJPA) {
        checkIfObjectIsNull(userJPA, "userJPA");
        this.userJPA = userJPA;
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new AccessUser(userJPA.getUserWithEmail(username));
        } catch (CouldNotGetUserException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
