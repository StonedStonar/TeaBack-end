package no.ntnu.appdev.group15.teawebsitebackend;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Order;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderState;
import no.ntnu.appdev.group15.teawebsitebackend.model.OrderedProduct;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.User;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
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
            userRegister.addUser(new User("Bjarne", "Bjarnesen", address, "bjarne@bjarnesen.com", "pass",12345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Arne", "Arnesen", address1, "arne@gmail.com", "password", 23456781 ,Role.ROLE_USER));
            userRegister.addUser(new User("Lise", "Fjell", address2, "lise@gmail.com", "passwoord", 34567812, Role.ROLE_USER));
            userRegister.addUser(new User("Fjell", "Bekken", address3, "fjell@lisemom.com", "123spel", 95852210 , Role.ROLE_USER));
        }
    }

    /**
     * Makes an ordered list of products.
     * @return the list
     */
    private static List<OrderedProduct> makeListWithOrderedProducts() {
        List<OrderedProduct> orderedProductList = new ArrayList<>();
        orderedProductList.add(new OrderedProduct(new Product(1, "Fjell laks Te", 399.99f, 100, new TeaDetails(), new Company()), 2));
        return orderedProductList;
    }

    /**
     * Adds test orders to the database.
     * @param orderRegister the order register to add orders to
     * @param userRegister the user register to add users to
     * @throws CouldNotAddOrderException gets thrown if order could not be added.
     * @throws CouldNotAddUserException gets thrown if user could not be added.
     */
    public static void addTestOrder(OrderRegister orderRegister, UserRegister userRegister)
        throws CouldNotAddOrderException, CouldNotAddUserException {
        checkIfObjectIsNull(orderRegister, "order Register");
        addTestUsers(userRegister);
        List<User> users = userRegister.getAllUsers();
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        if (orderRegister.getAllOrders().isEmpty()){
            Order order = new Order(123L, user1, makeListWithOrderedProducts(), OrderState.ORDERED,
                user1.getAddress(), "Posten", LocalDate.now().minusDays(1), "Klarna", false);
            Order order1 = new Order(124L, user2, makeListWithOrderedProducts(), OrderState.ORDERED,
                user2.getAddress(), "Posten", LocalDate.now().minusDays(1), "Visa", false);
            Order order2 = new Order(125L, user3, makeListWithOrderedProducts(), OrderState.ORDERED,
                user3.getAddress(), "Posten", LocalDate.now().minusDays(1), "Mastercard", false);
            Order order3 = new Order(126L, user4, makeListWithOrderedProducts(), OrderState.ORDERED,
                user4.getAddress(), "Posten", LocalDate.now().minusDays(1), "nudes", false);
            Order order4 = new Order(126L, user1, makeListWithOrderedProducts(), OrderState.ORDERED,
                user4.getAddress(), "Posten", LocalDate.now().minusDays(1), "nudes", false);
            orderRegister.addOrder(order);
            orderRegister.addOrder(order1);
            orderRegister.addOrder(order2);
            orderRegister.addOrder(order3);
            orderRegister.addOrder(order4);
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
