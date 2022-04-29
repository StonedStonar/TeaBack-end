package no.ntnu.appdev.group15.teawebsitebackend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.*;
import java.util.Random;

import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.springframework.stereotype.Component;

/**
 * A class that holds test data for the database.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Component
public class RegisterTestData {

    /**
     * Makes an instance of the RegisterTestData class.
     * @param tagJPA the tagJPA.
     * @param userJPA the userJPA.
     * @param productJPA the productJPA.
     * @param orderJPA the orderJPA.
     */
    public RegisterTestData(TagJPA tagJPA, UserJPA userJPA, ProductJPA productJPA, OrderJPA orderJPA, CompanyJPA companyJPA){
        try{
            addTestTags(tagJPA);
            addTestCompanies(companyJPA);
            addTestUsers(userJPA);
            addTestProducts(productJPA, userJPA);
            addTestOrder(orderJPA, userJPA.getAllUsers(), productJPA.getAllProducts());
        } catch (CouldNotAddOrderException | CouldNotAddProductException | CouldNotAddReviewException | CouldNotAddTagException | CouldNotAddUserException | CouldNotAddCompanyException e) {
            System.err.println("Test data could not be added but got an " + e.getClass().getSimpleName() + ".");
        }
    }

    /**
     * Adds test tags to a tags register.
     * @param tagsRegister the tags register to add tags too.
     * @throws CouldNotAddTagException gets thrown if one of the tags could not be added.
     */
    public void addTestTags(TagsRegister tagsRegister) throws CouldNotAddTagException {
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
    public void addTestUsers(UserRegister userRegister) throws CouldNotAddUserException {
        checkIfObjectIsNull(userRegister, "user register");
        if (userRegister.getAllUsers().isEmpty()){
            Address address = new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge");
            Address address1 = new Address(2900, "Fagernes", "Fagernesvegen", 33, "Norge");
            Address address2 = new Address(6015, "Ålesund", "Fagerlia", 15, "Noreg");
            Address address3 = new Address(6390, "Vestnes", "Kaigata", 15, "Noreg");
            userRegister.addUser(new User("Bjarne", "lastName", address, "bjarne@bjarnesen.com", "pass",12345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Prell", "Prellesen", new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge"), "prell@pirk.com", "pass",82345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Arne", "Arnesen", address1, "arne@gmail.com", "password", 23456781 ,Role.ROLE_USER));
            userRegister.addUser(new User("Lise", "Fjell", address2, "lise@gmail.com", "passwoord", 34567812, Role.ROLE_USER));
            userRegister.addUser(new User("Fjell", "Bekken", address3, "fjell@lisemom.com", "123spel", 95852210 , Role.ROLE_USER));
        }
    }

     /** Adds test products to the classes.
     * @param productRegister the product register.
     * @param userRegister the user register.
     * @throws CouldNotAddProductException gets thrown if the product could not be added.
     * @throws CouldNotAddReviewException gets thrown if the review could not be added.
     */
    public void addTestProducts(ProductRegister productRegister, UserRegister userRegister) throws CouldNotAddProductException, CouldNotAddReviewException {
        checkIfObjectIsNull(productRegister, "product register");
        List<Product> products = new ArrayList<>();
        //Todo: Vi må legge til mer reivews.
        if (productRegister.getAllProducts().isEmpty()) {
            //Todo: Gjorde bare om på denne så vi kunne generere random reviews til databasen.
            products.add(new Product( "Green Leaf Tea", 11.99f, 7, new TeaDetails(), new Company()));
            products.add(new Product( "Black water Leaf Tea", 9.99f, 3, new TeaDetails(), new Company()));
            products.add(new Product( "Mushroom High Tea", 6.99f, 2, new TeaDetails(), new Company()));
            products.add(new Product( "Blue Herb Tea", 5.99f, 1, new TeaDetails(), new Company()));
            products.add(new Product( "Strawberry Tea", 8.99f, 9, new TeaDetails(), new Company()));
            products.add(new Product( "Hause Norsk Tea", 7.99f, 5, new TeaDetails(), new Company()));
            products.add(new Product( "Stor Troll Herb Tea", 13.99f, 11, new TeaDetails(), new Company()));
            addRandomProductReviews(userRegister.getAllUsers(), products);
            for (Product product : products){
                productRegister.addProduct(product);
            }
        }
    }

    /**
     * Makes a list with test companies.
     * @return the list with the companies.
     */
    private List<Company> makeCompanies(){
        List<Company> companies = new ArrayList<>();
        companies.add(new Company("PepeINC", new CompanyDetails("pepe", new Address(1, 6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        companies.add(new Company("LuliClapAS", new CompanyDetails("Luli", new Address(1, 6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        companies.add(new Company("CompanyINC", new CompanyDetails("Nja", new Address(1, 6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
        return companies;
    }

    /**
     * Make sa random product review to each product.
     * @param users the users in the DB.
     * @param products the products to be added in DB.
     * @throws CouldNotAddReviewException gets thrown if the review could not be added.
     */
    private void addRandomProductReviews(List<User> users, List<Product> products) throws CouldNotAddReviewException {
        Random random = new Random();
        int amountOfUsers = users.size();
        int maxAmountOfRandomReviews = 5;
        for (Product product : products) {
            int amountOfReveiws = random.nextInt(maxAmountOfRandomReviews);
            for (int i = 0; i < amountOfReveiws; i++){
                User randomUser = users.get(random.nextInt(users.size()));
                product.addReview(new Review("Luli " + i, randomUser.getFirstName(), randomUser, "Pogchamp " + (i *amountOfReveiws), LocalDate.now().minusDays(random.nextInt(14)), random.nextInt(6)));
            }
        }
    }

    public void addTestCompanies(CompanyRegister companyRegister) throws CouldNotAddCompanyException {
        checkIfObjectIsNull(companyRegister, "companyRegister");
        List<Company> companies = new ArrayList<>();
        if (companyRegister.getAllCompanies().isEmpty()) {
            companies.add(new Company("Yogi Tea", new CompanyDetails("Best i Verden", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            companies.add(new Company("Ahmad Tea", new CompanyDetails("Best i Norge", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            companies.add(new Company("Clipper Tea", new CompanyDetails("Tea of great quality", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            companies.add(new Company("Pukka Tea", new CompanyDetails("Veldig Salt Tea", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            companies.add(new Company("Fair Trade", new CompanyDetails("Vi er et firma som opprett holder orden, slik at tea blir solgt og opprettholdt inn for rettighetene.", new Address(6015, "Ålesund", "Ålesundvegen", 123, "Norway"))));
            for (Company company : companies)
            companyRegister.addCompany(company);
        }
    }


    /**
     * Makes an ordered list of products.
     * @return the list
     */
    private List<OrderedProduct> makeListWithOrderedProducts(Product product) {
        List<OrderedProduct> orderedProductList = new ArrayList<>();
        orderedProductList.add(new OrderedProduct(product, 2));
        return orderedProductList;
    }

    /**
     * Adds test orders to the database.
     * @param orderRegister the order register to add orders to
     * @param users the users in the database.
     * @param products the products in this database.
     * @throws CouldNotAddOrderException gets thrown if order could not be added.
     */
    public void addTestOrder(OrderRegister orderRegister, List<User> users, List<Product> products) throws CouldNotAddOrderException {
        checkIfObjectIsNull(orderRegister, "order Register");
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);
        User user4 = users.get(3);
        if (orderRegister.getAllOrders().isEmpty()){
            Order order = new Order(123L, user1, makeListWithOrderedProducts(products.get(0)), OrderState.ORDERED,
                    user1.getAddress(), "Posten", LocalDate.now().minusDays(1), "Klarna", false);
            Order order1 = new Order(124L, user2, makeListWithOrderedProducts(products.get(1)), OrderState.ORDERED,
                    user2.getAddress(), "Posten", LocalDate.now().minusDays(1), "Visa", false);
            Order order2 = new Order(125L, user3, makeListWithOrderedProducts(products.get(2)), OrderState.ORDERED,
                    user3.getAddress(), "Posten", LocalDate.now().minusDays(1), "Mastercard", false);
            Order order3 = new Order(126L, user4, makeListWithOrderedProducts(products.get(0)), OrderState.ORDERED,
                    user4.getAddress(), "Posten", LocalDate.now().minusDays(1), "nudes", false);
            Order order4 = new Order(126L, user1, makeListWithOrderedProducts(products.get(2)), OrderState.ORDERED,
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
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
