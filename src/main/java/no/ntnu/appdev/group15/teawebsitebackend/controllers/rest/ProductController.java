package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;


import no.ntnu.appdev.group15.teawebsitebackend.RegisterTestData;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRegister productRegister;

    /**
     * Makes an instance of the ProductRegister class.
     * @param productJPA the product repository to access the database.
     */
    public ProductController(ProductJPA productJPA) {
        productRegister = productJPA;

    }

}
