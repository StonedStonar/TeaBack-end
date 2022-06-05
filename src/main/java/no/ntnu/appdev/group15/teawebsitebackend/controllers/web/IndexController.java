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
 * @author Trine Merete Staverløkk
 * @version 0.1
 */
@Controller
public class IndexController extends WebController{

    private ProductRegister productRegister;

    /**
     * Makes an instance of the IndexController class.
     */
    public IndexController(ProductJPA productJPA) {
        super();
        this.productRegister = productJPA;
    }

    /**
     * Gets the index page.
     * @param authentication the authentication.
     * @param model the model.
     * @return the HTML file name of index
     */
    @GetMapping("/index")
    public String getIndex(Authentication authentication, Model model){
        addLoggedInAttributes(authentication, model);

        model.addAttribute("listOfProductsOne", getSpesificNumberOfElements());
        return "index";
    }

    /**
     * Gets the about us page.
     * @param authentication the authentication.
     * @param model the model.
     * @return HTML file name of aboutUs
     */
    @GetMapping("/aboutUs")
    public String getAboutUs(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);

        return "aboutUs";
    }

    /**
     * Gets the contact us page.
     * @param authentication the authentication.
     * @param model the model.
     * @return HTML file name of contactUs.
     */
    @GetMapping("/contactUs")
    public String getContactUs(Authentication authentication, Model model) {
        addLoggedInAttributes(authentication, model);

        return "contactUs";
    }

    /**
     * Gets specific number of products.
     * @return the wanted product.
     */
    private List<Product> getSpesificNumberOfElements() {
        List<Product> products = productRegister.getAllProducts();
        List<Product> wantedProduct = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wantedProduct.add(products.get(i));
        }
        return wantedProduct;
    }
}
