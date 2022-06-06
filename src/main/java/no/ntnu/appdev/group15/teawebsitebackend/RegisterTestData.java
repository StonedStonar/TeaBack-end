package no.ntnu.appdev.group15.teawebsitebackend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.*;
import java.util.Random;

import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * A class that holds test data for the database.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Component
@Profile("!test")
public class RegisterTestData {


    /**
     * Makes an instance of the RegisterTestData class.
     * @param tagJPA the tagJPA.
     * @param userJPA the userJPA.
     * @param productJPA the productJPA.
     * @param orderJPA the orderJPA.
     * @param companyJPA the company JPA.
     */
    public RegisterTestData(TagJPA tagJPA, UserJPA userJPA, ProductJPA productJPA, OrderJPA orderJPA, CompanyJPA companyJPA) {
        try{
            addTestTags(tagJPA);
            addTestCompanies(companyJPA);
            addTestUsers(userJPA);
            addTestProducts(productJPA, userJPA, companyJPA, tagJPA);
            addTestOrder(orderJPA, userJPA.getAllUsers(), productJPA.getAllProducts());
            addTestCarts(userJPA, productJPA);
        } catch (CouldNotAddOrderException | CouldNotAddProductException | CouldNotAddReviewException | CouldNotAddTagException | CouldNotAddUserException | CouldNotAddCompanyException | CouldNotAddCartProductException | CouldNotGetUserException e) {
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
            tagsRegister.addTagWithDetails("Green tea", "A green tea that is green");
            tagsRegister.addTagWithDetails("Black tea", "A black tea that is black");
            tagsRegister.addTagWithDetails("White tea", "A white tea that is white");
            tagsRegister.addTagWithDetails("Yellow tea", "A yellow tea that is yellow");
            tagsRegister.addTagWithDetails("Oolong tea", "Oolong tea that is chinese");
            tagsRegister.addTagWithDetails("Small cup", "Small cup that is small");
            tagsRegister.addTagWithDetails("Cup", "Cup that is a cup");
            tagsRegister.addTagWithDetails("Big cup", "Big cup that is big");
            tagsRegister.addTagWithDetails("Tea pot", "Tea pot that tea pots");
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
            userRegister.addUser(new User("Bjarne", "Bjarnesen", address, "bjarne@bjarnesen.com", "pass",12345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Prell", "Prellesen", new Address(2910, "Aurdal", "Raskebakkin", 9, "Norge"), "prell@pirk.com", "pass",82345678, Role.ROLE_ADMIN));
            userRegister.addUser(new User("Arne", "Arnesen", address1, "arne@gmail.com", "password", 23456781 ,Role.ROLE_USER));
            userRegister.addUser(new User("Lise", "Fjell", address2, "lise@gmail.com", "passwoord", 34567812, Role.ROLE_USER));
            userRegister.addUser(new User("Fjell", "Bekken", address3, "fjell@lisemom.com", "123spel", 95852210 , Role.ROLE_USER));
        }
    }

     /** Adds test products to the classes.
     * @param productRegister the product register.
     * @param userRegister the user register.
      * @param companyRegister the company register
      * @param tagsRegister the tags register.
     * @throws CouldNotAddProductException gets thrown if the product could not be added.
     * @throws CouldNotAddReviewException gets thrown if the review could not be added.
     */
    public void addTestProducts(ProductRegister productRegister, UserRegister userRegister, CompanyRegister companyRegister, TagsRegister tagsRegister)
        throws CouldNotAddProductException, CouldNotAddReviewException, CouldNotAddTagException {
        checkIfObjectIsNull(productRegister, "product register");
        Lorem lorem = LoremIpsum.getInstance();
        List<Tag> tags = tagsRegister.getAllTags();
        List<Product> products = new ArrayList<>();
        List<Company> companies = companyRegister.getAllCompanies();
        if (productRegister.getAllProducts().isEmpty()) {
            products.add(new Product( "Green Leaf Tea", 109, 7, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(0)));
            products.add(new Product( "Black water Leaf Tea", 509, 3, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(1)));
            products.add(new Product( "Mushroom High Tea", 559, 20, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(2)));
            products.add(new Product( "Blue Herb Tea", 89, 1, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(3)));
            products.add(new Product( "Strawberry Tea", 99, 9, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(1)));
            products.add(new Product( "Hause Norsk Tea", 69, 5, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(1)));
            products.add(new Product( "Stor Troll Herb Tea", 150, 11, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(0)));
            products.add(new Product("White cup", 200, 200, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(4)));
            products.add(new Product("Black cup", 200, 200, new ProductDetails(lorem.getParagraphs(1,3), lorem.getWords(1, 8), lorem.getWords(2, 6)), companies.get(4)));
            products.get(0).getProductDetails().addTag(tags.get(5));
            products.get(1).getProductDetails().addTag(tags.get(6));
            products.get(2).getProductDetails().addTag(tags.get(9));
            products.get(3).getProductDetails().addTag(tags.get(9));
            products.get(4).getProductDetails().addTag(tags.get(7));
            products.get(5).getProductDetails().addTag(tags.get(7));
            products.get(7).getProductDetails().addTag(tags.get(12));
            products.get(7).getProductDetails().addTag(tags.get(11));
            products.get(8).getProductDetails().addTag(tags.get(12));
            products.get(8).getProductDetails().addTag(tags.get(11));
            addRandomProductReviews(userRegister.getAllUsers(), products);
            for (Product product : products){
                productRegister.addProduct(product);
            }
        }
    }

    /**
     * Adds cart products to a user.
     * @param userRegister the user register
     * @param productRegister the product register.
     * @throws CouldNotAddCartProductException gets thrown if the ordered product could not be added.
     * @throws CouldNotGetUserException gets thrown if the user is not found in the register.
     */
    public void addTestCarts(UserRegister userRegister, ProductRegister productRegister) throws CouldNotAddCartProductException, CouldNotGetUserException {
        List<User> users = userRegister.getAllUsers();
        Cart cart = users.get(0).getCart();
        List<Product> products = productRegister.getAllProducts();
        cart.addCartProduct(new CartProduct(products.get(0), 1));
        cart.addCartProduct(new CartProduct(products.get(1), 3));
        users.get(2).getCart().addCartProduct(new CartProduct(products.get(2), 3));
        users.get(1).getCart().addCartProduct(new CartProduct(products.get(3), 5));
        users.get(1).getCart().addCartProduct(new CartProduct(products.get(0), 2));
        for (User user : users){
            userRegister.updateUser(user);
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
        int maxAmountOfRandomReviews = 5;
        Lorem lorem = LoremIpsum.getInstance();
        for (Product product : products) {
            int amountOfReveiws = random.nextInt(maxAmountOfRandomReviews);
            for (int i = 0; i < amountOfReveiws; i++){
                User randomUser = users.get(random.nextInt(users.size()));
                product.addReview(new Review(lorem.getTitle(2), lorem.getName(), randomUser, lorem.getWords(2, 20), LocalDate.now().minusDays(random.nextInt(14)), random.nextInt(6)));
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
            for (Company company : companies){
                companyRegister.addCompany(company);
            }
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
            List<OrderedProduct> orders = makeListWithOrderedProducts(products.get(0));
            orders.add(new OrderedProduct(products.get(3), 10));
            Order order = new Order(123L, user1, orders, OrderState.ORDERED,
                    user1.getAddress(), "Posten", LocalDate.now().minusDays(1), "Klarna");
            Order order1 = new Order(124L, user2, makeListWithOrderedProducts(products.get(1)), OrderState.ORDERED,
                    user2.getAddress(), "Posten", LocalDate.now().minusDays(1), "Visa");
            Order order2 = new Order(125L, user3, makeListWithOrderedProducts(products.get(2)), OrderState.ORDERED,
                    user3.getAddress(), "Posten", LocalDate.now().minusDays(1), "Mastercard");
            Order order3 = new Order(126L, user4, makeListWithOrderedProducts(products.get(0)), OrderState.ORDERED,
                    user4.getAddress(), "Posten", LocalDate.now().minusDays(1), "Nudes");
            Order order4 = new Order(126L, user1, makeListWithOrderedProducts(products.get(2)), OrderState.ORDERED,
                    user4.getAddress(), "Posten", LocalDate.now().minusDays(1), "Nudes");
            Order order5 = new Order(123L, user1, makeListWithOrderedProducts(products.get(4)), OrderState.ORDERED,
                    user1.getAddress(), "Post-Nord", LocalDate.now().minusDays(0), "Klarna");
            orderRegister.addOrder(order);
            orderRegister.addOrder(order1);
            orderRegister.addOrder(order2);
            orderRegister.addOrder(order3);
            orderRegister.addOrder(order4);
            orderRegister.addOrder(order5);
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
