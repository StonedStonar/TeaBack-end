package no.ntnu.appdev.group15.teawebsitebackend.model.registers;

import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveProductException;

import java.util.List;

/**
 * Represents what basic methods a product register should have.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface ProductRegister {

    /**
     * Adds a new product to the register.
     * @param product the new product to add.
     * @throws CouldNotAddProductException gets thrown if the product could not be added to the register.
     */
    void addProduct(Product product) throws CouldNotAddProductException;

    /**
     * Removes a product from the register.
     * @param product the product to remove.
     * @throws CouldNotRemoveProductException gets thrown if the product could not be located.
     */
    void removeProduct(Product product) throws CouldNotRemoveProductException;

    /**
     * Removes a product with a given product id.
     * @param productId the id of the product to remove.
     * @throws CouldNotRemoveProductException gets thrown if the product could not be removed.
     */
    void removeProductWithProductID(long productId) throws CouldNotRemoveProductException;

    /**
     * Gets a product that matches the given product id.
     * @param productID the id of the product to find.
     * @return the product that matches that product id.
     * @throws CouldNotGetProductException gets thrown if the product could not be located.
     */
    Product getProduct(long productID) throws CouldNotGetProductException;

    /**
     * Updates a product and its details. The product id must be in the register.
     * @param product the product to update fields of.
     * @throws CouldNotGetProductException gets thrown if the product could not be located.
     */
    void updateProduct(Product product) throws CouldNotGetProductException;

    /**
     * Gets a list of all the products in the register.
     * @return the list with all the products.
     */
    List<Product> getAllProducts();
}
