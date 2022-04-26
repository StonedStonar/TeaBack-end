package no.ntnu.appdev.group15.teawebsitebackend;


import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class RegisterTestData {

    /**
     * Adds test tags to a tags register.
     * @param tagsRegister the tags register to add tags too.
     * @throws CouldNotAddTagException gets thrown if one of the tags could not be added.
     */
    public static void addTestTags(TagsRegister tagsRegister) throws CouldNotAddTagException {
        checkIfObjectIsNull(tagsRegister, "tags register");
        if (tagsRegister.getAllTags().isEmpty()){
            tagsRegister.addTagWithDetails("Food", "Consumable that will keep ya alive.");
            tagsRegister.addTagWithDetails("Tools", "Things used to fix stuff");
            tagsRegister.addTagWithDetails("Tea", "A herb that is crushed and used to make water taste something more than minerals.");
            tagsRegister.addTagWithDetails("Crockery", "Stuff you use to eat on and drink from.");
            tagsRegister.addTagWithDetails("Environmentally Friendly", "A product made in a sustainable way for the planets sake.");
        }
    }

    /**
     * Adds test users to the database.
     * @param userRegister the user register to add users too.
     * @throws CouldNotAddUserException gets thrown if a user could not be added to the register.
     */
    public static void addTestUsers(UserRegister userRegister) throws CouldNotAddUserException {
        checkIfObjectIsNull(userRegister, "user register");
        if (userRegister.getAllUsers().isEmpty()){
            Address address = new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge");
            Address address1 = new Address(2900, "Fagernes", "Fagernesvegen", 33, "Norge");
            Address address2 = new Address(6015, "Ålesund", "Fagerlia", 15, "Noreg");
            Address address3 = new Address(6390, "Vestnes", "Kaigata", 15, "Noreg");
            userRegister.addUser(new User("Bjarne", lastName, address, "bjarne@bjarnesen.com", "pass",12345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Arne", "Arnesen", address1, "arne@gmail.com", "password", 23456781 ,Role.ROLE_USER));
            userRegister.addUser(new User("Lise", "Fjell", address2, "lise@gmail.com", "passwoord", 34567812, Role.ROLE_USER));
            userRegister.addUser(new User("Fjell", "Bekken", address3, "fjell@lisemom.com", "123spel", 95852210 , Role.ROLE_USER));
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private static void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
