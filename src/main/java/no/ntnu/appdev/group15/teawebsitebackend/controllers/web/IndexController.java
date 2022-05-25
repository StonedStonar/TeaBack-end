package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



/**
 * Represents the controller for the index page.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class IndexController {

    private ProductRegister productRegister;

    /**
     * Makes an instance of the IndexController class.
     */
    public IndexController(ProductJPA productJPA) {
        this.productRegister = productJPA;
    }

    @GetMapping("/index")
    public String getIndex(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);

        model.addAttribute("listOfProductsOne", getSpesificNumberOfElements());
        return "index";
    }

    @GetMapping("/aboutUs")
    public String getAboutUs(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);

        return "aboutUs";
    }

    @GetMapping("/contactUs")
    public String getContactUs(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);

        return "contactUs";
    }

    @GetMapping("/product-info")
    public String getProductInfo(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);

        return "product-info";
    }

    private List<Product> getSpesificNumberOfElements() {
        List<Product> products = productRegister.getAllProducts();
        List<Product> wantedProduct = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wantedProduct.add(products.get(i));
        }
        return wantedProduct;
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
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
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
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
