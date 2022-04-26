package no.ntnu.appdev.group15.teawebsitebackend;


import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;

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
            userRegister.addUser(new User("Bjarne", "Bjarnesen", address, "bjarne@bjarnesen.com", "pass",12345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Arne", "Arnesen", address1, "arne@gmail.com", "password", 23456781 ,Role.ROLE_USER));
            userRegister.addUser(new User("Lise", "Fjell", address2, "lise@gmail.com", "passwoord", 34567812, Role.ROLE_USER));
            userRegister.addUser(new User("Fjell", "Bekken", address3, "fjell@lisemom.com", "123spel", 95852210 , Role.ROLE_USER));
        }
    }

    public static void addTestProducts(ProductRegister productRegister) throws CouldNotAddProductException {
        checkIfObjectIsNull(productRegister, "product register");
        if (productRegister.getAllProducts().isEmpty()) {
            Product product = new Product( "Green Leaf Tea", 11.99f, 7, new TeaDetails(), new Company());
            Product product1 = new Product( "Black water Leaf Tea", 9.99f, 3, new TeaDetails(), new Company());
            Product product2 = new Product( "Mushroom High Tea", 6.99f, 2, new TeaDetails(), new Company());
            Product product3 = new Product( "Blue Herb Tea", 5.99f, 1, new TeaDetails(), new Company());
            Product product4 = new Product( "Strawberry Tea", 8.99f, 9, new TeaDetails(), new Company());
            Product product5 = new Product( "Hause Norsk Tea", 7.99f, 5, new TeaDetails(), new Company());
            Product product6 = new Product( "Stor Troll Herb Tea", 13.99f, 11, new TeaDetails(), new Company());
            productRegister.addProduct(product);
            productRegister.addProduct(product1);
            productRegister.addProduct(product2);
            productRegister.addProduct(product3);
            productRegister.addProduct(product4);
            productRegister.addProduct(product5);
            productRegister.addProduct(product6);
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
