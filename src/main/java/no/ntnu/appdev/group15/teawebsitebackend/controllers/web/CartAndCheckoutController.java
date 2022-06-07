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
     * @param orderJPA the order JPA.
     * @param productJPA the product JPA.
     * @param userJPA the user JPA.
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
                cart.addCartProduct(new CartProduct(product, amount));
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




    /**
     * Confirms order and redirects to thankYouPage.
     * @param authentication the authentication.
     * @param deliveryValue the delivery value.
     * @param paymentValue the payment value.
     * @return a redirect to the "thank you for your purchase" screen.
     */
    @PostMapping("/doPayment")
    public RedirectView confirmOrder(Authentication authentication, @RequestParam(value = "deliveryMethod", required = false)Integer deliveryValue, @RequestParam(value = "paymentMethod", required = false)Integer paymentValue){
        ParameterBuilder parameterBuilder = new ParameterBuilder("thankYouPage");
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        Cart cart = user.getCart();
        List<OrderedProduct> orderedProductList = new ArrayList<>();
        boolean invalidInput = false;

        try {
            synchronized (CartAndCheckoutController.class) {
                orderedProductList = getOrderedProducts(cart);
            }
        } catch (CouldNotGetProductException | IllegalArgumentException e) {
            parameterBuilder.addParameter("invalidProductAmount", "true");
            invalidInput = true;
        }

        if (deliveryValue != null && paymentValue != null) {
            try {
                Order order = new Order(Long.MAX_VALUE, user, orderedProductList, OrderState.ORDERED, user.getAddress(), getDeliveryMethod(deliveryValue), LocalDate.now(),getPaymentMethod(paymentValue));
                orderRegister.addOrder(order);
                userRegister.updateUser(user);
            } catch (IllegalArgumentException e) {
                invalidInput = true;
                parameterBuilder.addParameter("invalidPaymentOrDelivery", "true");
            } catch (CouldNotAddOrderException | CouldNotGetUserException e) {
                invalidInput = true;
                parameterBuilder.addParameter("criticalError", "true");
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
        String payment = "";
        switch (paymentValue){
            case 1:
                payment = "Klarna";
                break;
            case 2:
                payment = "Vipps";
                break;
            case 3:
                payment = "Your mom";
                break;
            default:
                throw new IllegalArgumentException("Invalid paymentMethod");
        }
        return payment;
    }

    /**
     * Gets delivery method
     * @param deliveryValue represents the delivery method
     * @return the delivery method as a value
     */
    private String getDeliveryMethod(int deliveryValue){
        String delivery = "";
        switch (deliveryValue){
            case 1:
                delivery = "Postnord";
                break;
            case 2:
                delivery = "Posten";
                break;
            case 3:
                delivery = "Brevdue";
                break;
            default:
                throw new IllegalArgumentException("Invalid deliveryMethod");
        }
        return delivery;
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
    @PreAuthorize("hasRole('USER')")
    public String getCart(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        User user = getAccessUser(authentication).getUser();
        model.addAttribute("cart", user.getCart());
        return "cart";
    }

    @GetMapping("/thankYouPage")
    @PreAuthorize("hasRole('USER')")
    public String getThankYouPage(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);

        return "thankYouPage";
    }

    @GetMapping("/confirmPage")
    @PreAuthorize("hasRole('USER')")
    public String getConfirmPage(Authentication authentication, Model model) throws CouldNotGetUserException {
        addLoggedInAttributes(authentication, model);
        AccessUser accessUser = getAccessUser(authentication);
        User user = accessUser.getUser();
        int totalPrice = user.getCart().getAllProducts().stream().mapToInt(product -> product.getProduct().getPrice() * product.getAmount()).sum();
        model.addAttribute("price", totalPrice);
        return "confirmPage";
    }
}
