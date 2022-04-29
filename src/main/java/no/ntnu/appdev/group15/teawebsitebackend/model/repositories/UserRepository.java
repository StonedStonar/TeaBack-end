package no.ntnu.appdev.group15.teawebsitebackend.model.repositories;

import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Represents the connection to the database.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface UserRepository  extends JpaRepository<User, Long> {

    /**
     * Gets a user that matches the email and password.
     * @param email the email of the user.
     * @param password the password.
     * @return an optional that might contain the user matching the parameters.
     */
    @Query(value = "SELECT * FROM `users` WHERE email = :email AND password = :password", nativeQuery = true)
    Optional<User> loginToUser(@Param("email") String email, @Param("password") String password);

    /**
     * Gets a user that has this email.
     * @param email the email to look for.
     * @return the user that matched this email.
     */
    @Query(value = "SELECT * FROM `users` WHERE email = :email", nativeQuery = true)
    Optional<User> getUserByEmail(@Param("email") String email);
}
