package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.Review;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Kenneth Johansen Misund and Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebProductController {

    private ProductRegister productRegister;

    private TagsRegister tagRegister;

    @GetMapping("/product-info")
    public String getProductInfo(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);
        List<Product> productList = productRegister.getAllProducts();
        Product product = productList.get(6);
        model.addAttribute("mainProduct", product);
        model.addAttribute("relatedProduct", productList);
        model.addAttribute("productDetail", product.getProductDetails());
        return "product-info";
    }

    @GetMapping("/productsOverview")
    public String getProductsPage(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);
        List<Product> productList = productRegister.getAllProducts();
        model.addAttribute("relatedProduct", productList);
        model.addAttribute("relatedTags", tagRegister.getAllTags());
        return "products";
    }

    /**
     * Makes an instance of the ProductController class.
     */
    public WebProductController(ProductJPA productJPA, TagJPA tagJPA) {
        this.productRegister = productJPA;
        this.tagRegister = tagJPA;
    }

    @GetMapping("/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String getEditProduct(Authentication authentication, Model model, HttpSession httpSession){
        addLoggedInAttributes(authentication, model);
        httpSession.getAttributeNames();
        try {
            Product product = productRegister.getProduct(31);
            model.addAttribute("product", product);
            model.addAttribute("productID", product.getProductID());
            model.addAttribute("productName", product.getProductName());

        }catch (CouldNotGetProductException exception){
            //Todo: something
        }
        addAllAttributes(model, httpSession);

        return "editProduct";
    }

    @PutMapping("/editProduct")
    public RedirectView editProduct(@RequestParam("productID") long productID, @RequestParam("productName") String productName, @RequestParam("productAmount") int productAmount,
                                    @RequestParam("productPrice") int productPrice, @RequestParam("companyID") int companyID,
                                    @RequestParam("ingredients") String ingredients, HttpSession httpSession){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProduct");
        try {
            checkIfNumberNotNegative(productAmount, "product amount");
            checkString(productName, "product name");
            checkString(ingredients, "ingredients");
            checkIfNumberNotNegative(productPrice, "product price");
            checkIfNumberNotNegative(companyID, "companyID");
            checkIfNumberNotNegative(productAmount, "product amount");
            Product product = productRegister.getProduct(productID);
            product.setProductName(productName);
            product.setPrice(productPrice);
            int totalProduct = productAmount - product.getAmountOfProduct();
            product.addAmountOfProduct(totalProduct);


        }catch (IllegalArgumentException  exception){
            parameterBuilder.addParameter("invalidFieldInput", "true");
            httpSession.setAttribute("productName", productName);
            httpSession.setAttribute("productAmount", productAmount);
            httpSession.setAttribute("productPrice", productPrice);
            httpSession.setAttribute("companyID", companyID);
            httpSession.setAttribute("ingredientsInput", ingredients);
        }catch (CouldNotGetProductException exception){
            parameterBuilder.addParameter("invalidProductID", "true");
        }
        return new RedirectView(parameterBuilder.buildString(), true);
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
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Adds all the attributes to the model.
     * @param model the model.
     * @param httpSession the http session.
     */
    private void addAllAttributes(Model model, HttpSession httpSession){
        Iterator<String> it = httpSession.getAttributeNames().asIterator();
        while (it.hasNext()){
            String attributeName = it.next();
            model.addAttribute(attributeName, httpSession.getAttribute(attributeName));
            httpSession.removeAttribute(attributeName);
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

    /**
     * Check to make sure that integer values cannot be negative.
     * @param object the object to be checked.
     * @param error exception message to be displayed.
     */
    private void checkIfNumberNotNegative(long object, String error) {
        if (object <= 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative values.");
        }
    }
}
