package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.bytebuddy.pool.TypePool;
import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetUserException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.UserRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.lang.reflect.Parameter;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class CartAndCheckoutController {

    private OrderRegister orderRegister;

    private ProductRegister productRegister;

    private UserRegister userRegister;

    /**
     * Makes an instance of the CartAndCheckoutController class.
     */
    public CartAndCheckoutController(OrderJPA orderJPA, ProductJPA productJPA, UserJPA userJPA) {
        this.orderRegister = orderJPA;
        this.productRegister = productJPA;
        this.userRegister = userJPA;
    }


    @PutMapping("/addToCart")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public RedirectView addToCart(@RequestParam("productID") long productID, @RequestParam(value = "amount", required = false) Integer amount,
                                  Authentication authentication){
        ParameterBuilder parameterBuilder = new ParameterBuilder("/cart");
        AccessUser accessUser = getAccessUser(authentication);
        //Todo: Fix the rest tomorrow
        try {
            Product product = productRegister.getProduct(productID);
            Cart cart = accessUser.getUser().getCart();
            if (amount == null || amount == 0){
                amount = 1;
            }
            if (!cart.checkIfProductIsInCart(product)){
                cart.addCartProduct(new CartProduct(product, amount, cart));
            }else {
                CartProduct cartProduct = cart.getCartProduct(productID);
                cartProduct.setAmount(amount);
            }
        } catch (CouldNotGetProductException e) {

        } catch (CouldNotAddCartProductException e) {

        } catch (CouldNotGetCartProductException e) {

        }

        return new RedirectView(parameterBuilder.buildString(), true);
    }

    @PostMapping("/doPayment") //TODO FIX logic (errors after tried to not select a method)
    public RedirectView confirmOrderAndDoPayment(Authentication authentication, @RequestParam(value = "deliveryMethod", required = false)Integer deliveryValue, @RequestParam(value = "paymentMethod", required = false)Integer paymentValue){
        ParameterBuilder parameterBuilder = new ParameterBuilder("thankYouPage");
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        Cart cart = user.getCart();
        List<OrderedProduct> orderedProducts = null;
        boolean invalidInput = false;

        try {
            //Not optimal, but in our case the system would be locked if we are going to process a cart.
            synchronized (CartAndCheckoutController.class) {
                orderedProducts = getOrderedProducts(cart);
            }
        } catch (CouldNotGetProductException | IllegalArgumentException e) {
            parameterBuilder.addParameter("invalidProductAmount", "true");
            invalidInput = true;
        }
        if (deliveryValue != null && paymentValue != null) {
            try {
                Order order = new Order(Long.MAX_VALUE, user, orderedProducts, OrderState.ORDERED, user.getAddress(), getDeliveryMethod(deliveryValue), LocalDate.now(),getPaymentMethod(paymentValue), false);
                orderRegister.addOrder(order);
                //cart.clearAllProducts();
                userRegister.updateUser(user);
            } catch (IllegalArgumentException e) {
                invalidInput = true;
                parameterBuilder.addParameter("invalidPaymentOrDelivery", "true");
                //her funker ikke delivery eller payment
            } catch (CouldNotAddOrderException | CouldNotGetUserException e) {
                invalidInput = true;
                parameterBuilder.addParameter("criticalError", "true");
                // her har det skjedd noe critical
            }
        } else {
            invalidInput = true;
            parameterBuilder.addParameter("invalidPaymentOrDelivery", "true");
        }

        if (invalidInput) {
            parameterBuilder.setPageName("confirmPage");
        }


        return new RedirectView(parameterBuilder.buildString(), true);
    }

    private String getPaymentMethod(int paymentValue){
        return switch (paymentValue){
            case 1 -> "Klarna";
            case 2 -> "Vipps";
            case 3 -> "Your mom";
            default -> throw new IllegalArgumentException("Invalid paymentMethod");
        };
    }

    private String getDeliveryMethod(int deliveryValue){
        return switch (deliveryValue){
            case 1 -> "Postnord";
            case 2 -> "Posten";
            case 3 -> "Brevdue";
            default -> throw new IllegalArgumentException("Invalid deliveryMethod");
        };
    }


    /**
     * Gets ordered products from cart.
     * @param cart the cart with products to get.
     * @return a list of ordered products.
     * @throws CouldNotGetProductException this gets thrown when
     */
    private List<OrderedProduct> getOrderedProducts(Cart cart) throws CouldNotGetProductException {
        List<CartProduct> cartProductsList = cart.getAllProducts();
        List<OrderedProduct> orderedProductList = new ArrayList<>();
        try {
            for(CartProduct cartProduct:cartProductsList){
                long productID = cartProduct.getProduct().getProductID();
                Product product = productRegister.getProduct(productID);
                OrderedProduct orderedProduct = new OrderedProduct(product, cartProduct.getAmount());
                product.removeAmountOfProduct(orderedProduct.getAmountOfProduct());

                orderedProductList.add(orderedProduct);
            }
        }catch (CouldNotGetProductException | IllegalArgumentException e) {
            //TODO logic
            for(OrderedProduct orderedProduct:orderedProductList) {
                Product product = orderedProduct.getProduct();
                int amount = orderedProduct.getAmountOfProduct();
                product.addAmountOfProduct(amount);
            }
            throw e;
        }finally {
            for(OrderedProduct orderedProduct : orderedProductList){
                productRegister.updateProduct(orderedProduct.getProduct());
            }
        }

        return orderedProductList;
    }

    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getCart(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        model.addAttribute("cart", user.getCart());
        return "cart";
    }

    @GetMapping("/thankYouPage")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getThankYouPage(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);

        return "thankYouPage";
    }

    @GetMapping("/confirmPage")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getConfirmPage(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);

        return "confirmPage";
    }

    /**
     * Gets the access user that is using the page.
     * @param authentication the authentication object.
     * @return the access user of this session.
     */
    private AccessUser getAccessUser(Authentication authentication){
        return (AccessUser) authentication.getPrincipal();
    }

    /**
     * Adds a logged in attribute to the model.
     * @param authentication the authentication.
     * @param model the model.
     */
    private void addLoggedInAttributes(Authentication authentication, Model model){
        boolean loggedIn = authentication != null;
        boolean admin = false;
        model.addAttribute("loggedIn", loggedIn);
        if (loggedIn){
            admin = getAccessUser(authentication).getUser().getRole() == Role.ROLE_ADMIN;
        }
        model.addAttribute("isAdmin", admin);
    }

    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
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
}
