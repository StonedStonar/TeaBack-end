package no.ntnu.appdev.group15.teawebsitebackend.model.database;

import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a register that has the database as its implementation.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
@Service
public class ProductJPA implements ProductRegister {

    private ProductRepository productRepository;

    /**
     * Makes an instance of the ProductJPA class.
     * @param productRepository the product repository that works as connection to the database.
     */

    public ProductJPA(ProductRepository productRepository) {
        checkIfObjectIsNull (productRepository, "product repository");
        this.productRepository = productRepository;
    }


    @Override
    public void addProduct(Product product) throws CouldNotAddProductException {
        checkIfProductIsValid(product);
        if(!productRepository.existsById(product.getProductID())) {
            productRepository.save(product);
        } else {
            throw new CouldNotAddProductException("The productID is already in use.");
        }

    }

    @Override
    public void removeProduct(Product product) throws CouldNotRemoveProductException {
        checkIfProductIsValid(product);
        removeProductWithProductID(product.getProductID());
    }

    @Override
    public void removeProductWithProductID(long productId) throws CouldNotRemoveProductException {
        checkIfNumberIsAboveZero(productId, "productID");
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new CouldNotRemoveProductException("The product with this id " + productId + " is not found in the system.");
        }
    }

    @Override
    public Product getProduct(long productID) throws CouldNotGetProductException {
        checkIfNumberIsAboveZero(productID, "productID");
        Optional<Product> optionalProduct = productRepository.findById(productID);
        if (optionalProduct.isEmpty()) {
            throw new CouldNotGetProductException("The product with this id " + productID + " is not found in the system.");
        }
        return optionalProduct.get();
    }

    @Override
    public void updateProduct(Product product) throws CouldNotGetProductException {
       checkIfProductIsValid(product);
       if (productRepository.existsById(product.getProductID())) {
           productRepository.save(product);
       } else {
          throw new CouldNotGetProductException("The product with the id " + product.getProductID() + " is not found in the system.");
       }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Iterable<Product> it = productRepository.findAll();
        it.forEach(product -> products.add(product));
        return products;
    }

    /**
     * Checks if the product is not null, easier to use everywhere.
     * @param product the product we want to check.
     */
    private void checkIfProductIsValid(Product product) {
        checkIfObjectIsNull (product, "product expected");
    }

    /**
     * Checks if the input number is above zero.
     * @param numberToCheck the number to check.
     */
    private void checkIfNumberIsAboveZero(long numberToCheck, String prefix){
        if (numberToCheck <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be larger than zero.");
        }
    }

    /**
     * Checks if an object is null.
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
