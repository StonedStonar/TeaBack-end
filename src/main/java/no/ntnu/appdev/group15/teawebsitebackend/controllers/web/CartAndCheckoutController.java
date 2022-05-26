package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.OrderRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * Makes an instance of the CartAndCheckoutController class.
     */
    public CartAndCheckoutController(OrderJPA orderJPA, ProductJPA productJPA) {
        this.orderRegister = orderJPA;
        this.productRegister = productJPA;
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
        model.addAttribute("loggedIn", loggedIn);
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
