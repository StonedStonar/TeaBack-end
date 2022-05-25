package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.ProductDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetProductException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.ProductRegister;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebProductController {

    private ProductRegister productRegister;

    private TagsRegister tagsRegister;

    private CompanyRegister companyRegister;

    /**
     * Makes an instance of the ProductController class.
     * @param companyJPA the company JPA
     * @param productJPA the product JPA.
     * @param tagJPA the tag JPA.
     */
    public WebProductController(ProductJPA productJPA, TagJPA tagJPA, CompanyJPA companyJPA) {
        this.productRegister = productJPA;
        this.tagsRegister = tagJPA;
        this.companyRegister = companyJPA;
    }

    @GetMapping("/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String getEditProduct(Authentication authentication, Model model, HttpSession httpSession,
                                 @RequestParam("productID") long productID){
        addLoggedInAttributes(authentication, model);
        Boolean isPreview = (Boolean) httpSession.getAttribute("isPreview");
        httpSession.removeAttribute("isPreview");
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProduct");
        if (isPreview != null && isPreview){
            addAllAttributes(model, httpSession);
        }else {
            try {
                Product product = productRegister.getProduct(productID);
                model.addAttribute("productID", product.getProductID());
                model.addAttribute("productName", product.getProductName());
                model.addAttribute("productAmount", product.getAmountOfProduct());
                model.addAttribute("price", product.getPrice());
                model.addAttribute("companyID", product.getCompany().getCompanyID());
                model.addAttribute("ingredients", product.getProductDetails().getIngredients());
                model.addAttribute("description", product.getProductDetails().getDescription());
                model.addAttribute("tags", convertListToValues(product.getProductDetails().getTagList()));

            }catch (CouldNotGetProductException exception){
                parameterBuilder.addParameter("invalidProductID", "true");
            }
        }

        String url = parameterBuilder.buildString();

        return url;
    }

    @PutMapping("/editProduct")
    public RedirectView editProduct(@RequestParam("productID") long productID, @RequestParam("productName") String productName, @RequestParam("productAmount") int productAmount,
                                    @RequestParam("productPrice") int productPrice, @RequestParam("companyID") int companyID,
                                    @RequestParam("ingredients") String ingredients, @RequestParam("tags") List<Long> tags, @RequestParam("description") String description
                                    ,@RequestParam(value = "previewProduct", required = false) Boolean preview,  HttpSession httpSession){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProduct");
        parameterBuilder.addParameter("productID", String.valueOf(productID));
        boolean invalidInput = false;
        try {
            checkIfNumberNotNegative(productAmount, "product amount");
            checkString(productName, "product name");
            checkString(description, "description");
            checkIfNumberNotNegative(productPrice, "product price");
            checkIfNumberNotNegative(companyID, "companyID");
            checkIfNumberNotNegative(productAmount, "product amount");
            Product product = productRegister.getProduct(productID);
            if (preview != null && preview){
                parameterBuilder.addParameter("tagPreview", "true");
                httpSession.setAttribute("isPreview", true);
            }else {
                product.setProductName(productName);
                product.setPrice(productPrice);
                int totalProduct = productAmount - product.getAmountOfProduct();
                if (totalProduct > 0){
                    product.addAmountOfProduct(totalProduct);
                }else if (totalProduct < 0){
                    product.removeAmountOfProduct(totalProduct);
                }
                ProductDetails productDetails = product.getProductDetails();
                addAndRemoveTags(productDetails, tags);
                parameterBuilder.addParameter("tagUpdated", "true");
                productRegister.updateProduct(product);
            }
        }catch (IllegalArgumentException  exception){
            parameterBuilder.addParameter("invalidFieldInput", "true");
            invalidInput = true;
        }catch (CouldNotGetProductException exception){
            parameterBuilder.addParameter("invalidProductID", "true");
            invalidInput = true;
        } catch (CouldNotGetTagException exception) {
            parameterBuilder.addParameter("invalidTagId", "true");
            invalidInput = true;
        } catch (CouldNotAddTagException exception) {
            parameterBuilder.addParameter("duplicateTag", "true");
            invalidInput = true;
        } catch (CouldNotRemoveTagException exception) {
            parameterBuilder.addParameter("tagNotFound", "true");
            invalidInput = true;
        }
        if (invalidInput || (preview != null && preview)){
            httpSession.setAttribute("productID", productID);
            httpSession.setAttribute("productName", productName);
            httpSession.setAttribute("productAmount", productAmount);
            httpSession.setAttribute("price", productPrice);
            httpSession.setAttribute("companyID", companyID);
            httpSession.setAttribute("ingredientsInput", ingredients);
            httpSession.setAttribute("tags", tags);
            httpSession.setAttribute("description", description);
        }
        return new RedirectView(parameterBuilder.buildString(), true);
    }

    /**
     * Adds and removes tags form a product based on if it's already in.
     * @param productDetails the product details.
     * @param tags the tags id's
     * @throws CouldNotGetTagException gets thrown if the input tags could not be found.
     * @throws CouldNotRemoveTagException gets thrown if the tag could not be removed.
     * @throws CouldNotAddTagException gets thrown if the tag could not be added.
     */
    private void addAndRemoveTags(ProductDetails productDetails, List<Long> tags) throws CouldNotRemoveTagException, CouldNotAddTagException, CouldNotGetTagException {
        List<Tag> tagList = getAllTagsWithIDs(tags);
        List<Tag> productTagList = productDetails.getTagList();
        List<Tag> tagsToRemove = productTagList.stream().filter(tag -> !tagList.contains(tag)).toList();
        List<Tag> tagsToAdd = tagList.stream().filter(tag -> !productTagList.contains(tag)).toList();
        for (Tag tag : tagsToRemove) {
            productDetails.removeTag(tag);
        }
        for (Tag tag : tagsToAdd) {
            productDetails.addTag(tag);
        }
    }

    /**
     * Gets the list of the specified tags.
     * @param tagIds the tags id.
     * @return a list with the tags.
     * @throws CouldNotGetTagException gets thrown if a tag could not be found.
     */
    private List<Tag> getAllTagsWithIDs(List<Long> tagIds) throws CouldNotGetTagException {
        List<Tag> tagList = new ArrayList<>();
        for (long tagId : tagIds){
            tagList.add(tagsRegister.getTagWithID(tagId));
        }
        return tagList;
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
            Object object = httpSession.getAttribute(attributeName);
            if (object instanceof List list){
                object = convertListToValues(list);
            }
            model.addAttribute(attributeName, object);
            httpSession.removeAttribute(attributeName);
        }
    }

    private String convertListToValues(List list){
        StringBuilder stringBuilder = new StringBuilder();
        if (!list.isEmpty() && list.get(0) instanceof Long){
            List<Long> longs = list;
            for(long number: longs){
                if (!stringBuilder.isEmpty()){
                    stringBuilder.append(",");
                }
                stringBuilder.append(number);

            }
        }
        return stringBuilder.toString();
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
