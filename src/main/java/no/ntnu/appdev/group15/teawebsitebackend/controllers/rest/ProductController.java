package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * Represents the ProductController needed to connect Product to database.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    private ProductRegister productRegister;

    /**
     * Makes an instance of the ProductRegister class.
     * @param productJPA the product repository to access the database.
     */
    public ProductController(ProductJPA productJPA) {
        checkIfObjectIsNull(productJPA, "productJPA");
        productRegister = productJPA;
    }

    /**
     * Gets all the product in the system.
     * @return a list with all the product in the system.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productRegister.getAllProducts();
    }

    /**
     * Gets a product with a specified ID.
     * @param id the id of the product.
     * @return the product with this id.
     * @throws CouldNotGetProductException gets thrown if the product cannot be found.
     */
    @GetMapping("/{id}")
    public Product getProductWithProductID(@PathVariable Long id) throws CouldNotGetProductException {
        return productRegister.getProduct(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addProduct(@RequestBody String body) throws JsonProcessingException, CouldNotAddProductException {
        productRegister.addProduct(getProduct(body));
    }

    /**
     * Deletes a product from the list.
     * @param id the id of the product.
     * @throws CouldNotRemoveProductException gets thrown if the product could not be removed.
     */
    public void deleteProduct(@PathVariable Long id) throws CouldNotRemoveProductException {
        productRegister.removeProductWithProductID(id);
    }

    /**
     * Makes it possible to update the product in the system if you are a ADMIN user.
     * @param body the body of the request.
     * @throws JsonProcessingException gets thrown if the format is invalid.
     * @throws CouldNotGetProductException gets thrown if the product could not be found in the system.
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void updateProduct(@RequestBody String body) throws JsonProcessingException, CouldNotGetProductException {
        productRegister.updateProduct(getProduct(body));
    }

    /**
     * Gets the product from the body.
     * @param body the json object.
     * @return the product form the json object.
     * @throws JsonProcessingException Should be cast if the json does not contain right format.
     */
    private Product getProduct(@RequestBody String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, Product.class);
    }

    @ExceptionHandler(CouldNotRemoveProductException.class)
    private ResponseEntity<String> handleCouldNotRemoveProductException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CouldNotAddProductException.class)
    private ResponseEntity<String> handleCouldNotAddProductException(Exception ex) {
        return ResponseEntity.status(HttpStatus.IM_USED).body(ex.getMessage());
    }

    @ExceptionHandler(CouldNotGetProductException.class)
    private ResponseEntity<String> handleCouldNotGetProductException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
    }


    /**
     * Check if an object is null.
     * @param object the object you want to do a check on.
     * @param error the error message the exception should have.
     */
    private void checkIfObjectIsNull (Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    /**
     * Checks if a string is of a valid format or not.
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

}
