package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCartProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveCartProductException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a cart with items.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartID")
    private long cartID;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cartProducts", joinColumns = @JoinColumn(name = "cartID"))
    @Column(name = "cartID")
    private List<CartProduct> cartProductList;

    /**
     * Makes an instance of the Cart class.
     */
    public Cart() {
        this.cartProductList = new ArrayList<>();
    }

    /**
     * Adds a product to the cart.
     * @param cartProduct the product to add in the cart.
     * @throws CouldNotAddCartProductException gets thrown if the cart product could not be added.
     */
    public void addCartProduct(CartProduct cartProduct) throws CouldNotAddCartProductException {
        checkIfObjectIsNull(cartProduct, "cart product");
        if (!checkIfProductIsInCart(cartProduct)){
            cartProductList.add(cartProduct);
        }else {
            throw new CouldNotAddCartProductException("The product is already in the cart.");
        }
    }

    /**
     * Removes a product from the cart.
     * @param product the product to remove.
     * @throws CouldNotRemoveCartProductException gets thrown if the product could not be removed from the cart.
     */
    public void removeCartProduct(Product product) throws CouldNotRemoveCartProductException {
        checkIfObjectIsNull(product, "product");
        try {
            CartProduct cartProduct = getCartProduct(product.getProductID());
            cartProductList.remove(cartProduct);
        }catch (CouldNotGetCartProductException exception){
            throw new CouldNotRemoveCartProductException(exception.getMessage());
        }
    }

    /**
     * Checks if the cart product is in the cart.
     * @param cartProduct the cart product.
     * @return <code>true</code> if the product is in the cart.
     *         <code>false</code> if the product is not in the cart.
     */
    private boolean checkIfProductIsInCart(CartProduct cartProduct){
        return cartProductList.stream().anyMatch(cartP -> cartP.getProduct().getProductID() == cartProduct.getProduct().getProductID());
    }

    /**
     * Gets the cart product.
     * @param productID the products id.
     * @return the cart product.
     * @throws CouldNotGetCartProductException gets thrown if the product could not be found in the cart.
     */
    public CartProduct getCartProduct(long productID) throws CouldNotGetCartProductException {
        checkIfNumberNotNegative(productID, "productID");
        Optional<CartProduct> optionalCartProduct = cartProductList.stream().filter(product -> product.getProduct().getProductID() == productID).findFirst();
        if (optionalCartProduct.isEmpty()){
            throw new CouldNotGetCartProductException("The product with id " + productID + " could not be found in this cart.");
        }
        return optionalCartProduct.get();
    }

    /**
     * Check to make sure that integer values cannot go bellow 0. (Negative value)
     * @param number the object to be checked.
     * @param errorMessage exception to be displayed.
     */
    public void checkIfNumberNotNegative(long number, String errorMessage) {
        if(number <= 0) {
            throw new IllegalArgumentException("The " + errorMessage + " cannot be of negative value.");
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
