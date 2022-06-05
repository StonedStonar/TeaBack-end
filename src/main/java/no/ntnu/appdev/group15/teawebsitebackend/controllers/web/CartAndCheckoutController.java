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
 * @author Steinar Hjelle Midthus & Trine Merete Staverløkk
 * @version 0.1
 */
@Controller
public class CartAndCheckoutController extends WebController{

    private OrderRegister orderRegister;

    private ProductRegister productRegister;

    private UserRegister userRegister;

    /**
     * Makes an instance of the CartAndCheckoutController class.
     */
    public CartAndCheckoutController(OrderJPA orderJPA, ProductJPA productJPA, UserJPA userJPA) {
        super();
        this.orderRegister = orderJPA;
        this.productRegister = productJPA;
        this.userRegister = userJPA;
    }

    /**
     * Adds a product to the cart.
     * @param productID the product ID.
     * @param amount the amount.
     * @param authentication the authentication of the user.
     * @return a redirect to the cart page.
     */
    @PutMapping("/addToCart")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public RedirectView addToCart(@RequestParam("productID") long productID, @RequestParam(value = "amountOfProduct", required = false) Integer amount,
                                  Authentication authentication){
        ParameterBuilder parameterBuilder = new ParameterBuilder("/cart");
        AccessUser accessUser = getAccessUser(authentication);
        try {
            User user = userRegister.getUserWithUserID(accessUser.getUser().getUserId());
            Product product = productRegister.getProduct(productID);
            Cart cart = user.getCart();
            if (amount == null || amount == 0){
                amount = 1;
            }
            if (!cart.checkIfProductIsInCart(product)){
                cart.addCartProduct(new CartProduct(product, amount, cart));
                parameterBuilder.addParameter("productAdded", "true");
            }else {
                CartProduct cartProduct = cart.getCartProduct(productID);
                cartProduct.setAmount(amount);
                parameterBuilder.addParameter("amountUpdated", "true");
            }
            userRegister.updateUser(user);
            accessUser.setUser(user);
        } catch (CouldNotGetProductException | CouldNotAddCartProductException | CouldNotGetCartProductException | CouldNotGetUserException e) {
            parameterBuilder.addParameter("invalidProductID", "true");
        }

        return new RedirectView(parameterBuilder.buildString(), true);
    }



    //TODO "my info" stemmer ikke.


    /**
     * Confirms order and redirects to thankYouPage.
     * @param authentication
     * @param deliveryValue
     * @param paymentValue
     * @return
     */
    @PostMapping("/doPayment")
    public RedirectView confirmOrder(Authentication authentication, @RequestParam(value = "deliveryMethod", required = false)Integer deliveryValue, @RequestParam(value = "paymentMethod", required = false)Integer paymentValue){
        ParameterBuilder parameterBuilder = new ParameterBuilder("thankYouPage");
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        Cart cart = user.getCart();
        List<OrderedProduct> orderedProductList = new ArrayList<>();
        //List<OrderedProduct> orderedProducts = null; //WOOPSIES
        boolean invalidInput = false;

        try {
            //Not optimal, but in our case the system would be locked if we are going to process a cart.
            synchronized (CartAndCheckoutController.class) {
                orderedProductList = getOrderedProducts(cart);
            }
        } catch (CouldNotGetProductException | IllegalArgumentException e) {
            parameterBuilder.addParameter("invalidProductAmount", "true");
            invalidInput = true;
        }

        if (deliveryValue != null && paymentValue != null) {
            try {
                Order order = new Order(Long.MAX_VALUE, user, orderedProductList, OrderState.ORDERED, user.getAddress(), getDeliveryMethod(deliveryValue), LocalDate.now(),getPaymentMethod(paymentValue), false);
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

    /**
     * Gets payment method
     * @param paymentValue represents the payment method
     * @return the payment method as a value
     */
    private String getPaymentMethod(int paymentValue){
        return switch (paymentValue){
            case 1 -> "Klarna";
            case 2 -> "Vipps";
            case 3 -> "Your mom";
            default -> throw new IllegalArgumentException("Invalid paymentMethod");
        };
    }

    /**
     * Gets delivery method
     * @param deliveryValue represents the delivery method
     * @return the delivery method as a value
     */
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

    /**
     * Gets the cart of the user.
     * @param model the model object.
     * @param authentication the authentication.
     * @return the html name of the page.
     */
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
    public String getConfirmPage(Authentication authentication, Model model) throws CouldNotGetUserException {
        addLoggedInAttributes(authentication, model);
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        int totalPrice = user.getCart().getAllProducts().stream().mapToInt(product -> product.getProduct().getPrice() * product.getAmount()).sum();
        model.addAttribute("price", totalPrice);
        return "confirmPage";
    }
}
