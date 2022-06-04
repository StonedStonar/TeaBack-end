package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
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
public class IndexController extends WebController{

    private ProductRegister productRegister;

    /**
     * Makes an instance of the IndexController class.
     */
    public IndexController(ProductJPA productJPA) {
        super();
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

    private List<Product> getSpesificNumberOfElements() {
        List<Product> products = productRegister.getAllProducts();
        List<Product> wantedProduct = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wantedProduct.add(products.get(i));
        }
        return wantedProduct;
    }
}
