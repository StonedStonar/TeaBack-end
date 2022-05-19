package no.ntnu.appdev.group15.teawebsitebackend.security;

import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A class holding the details of the authenticated user.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class AccessUser implements UserDetails {

    private User user;

    private final List<GrantedAuthority> authorities = new LinkedList<>();

    /**
     * Makes an authenticated user.
     * @param user the user.
     */
    public AccessUser(User user) {
        checkIfObjectIsNull(user, "user");
        this.user = user;
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    /**
     * Sets the user.
     * @param user the user.
     */
    public void setUser(User user){
        checkIfObjectIsNull(user, "user");
        this.user = user;
        authorities.clear();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isActive();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Gets the user of this access user object.
     * @return the user.
     */
    public User getUser(){
        return user;
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
