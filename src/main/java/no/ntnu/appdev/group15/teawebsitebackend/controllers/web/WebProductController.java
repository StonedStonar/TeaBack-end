package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.*;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.ProductJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.*;
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
import java.util.*;

import static java.lang.Long.parseLong;


/**
 * @author Kenneth Johansen Misund and Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebProductController {

    private ProductRegister productRegister;

    private CompanyRegister companyRegister;

    private TagsRegister tagRegister;

    /**
     * Gets the product into page.
     * @param authentication the authentication
     * @param model the model
     * @return the product info page.
     */
    @GetMapping("/product-info")
    public String getProductInfo(Authentication authentication, Model model, @RequestParam("id") long id) {
        addLoggedInAttributes(authentication, model);
        List<Product> productList = productRegister.getAllProducts();
        Product product = null;
        model.addAttribute("sale", false);
        try {
            product = productRegister.getProduct(id);
            model.addAttribute("mainProduct", product);
            model.addAttribute("relatedProduct", productList);
            model.addAttribute("productDetail", product.getProductDetails());
        } catch (CouldNotGetProductException e) {
            e.printStackTrace();
        }
        return "product-info";
    }

    /**
     * Gets the products page.
     * @param authentication the authentication.
     * @param model the model
     * @return the products page.
     */
    @GetMapping("/productsOverview")
    public String getProductsPage(Authentication authentication, Model model, @RequestParam Map<String, String> parameters) throws CouldNotGetTagException, CouldNotGetCompanyException {
        addLoggedInAttributes(authentication, model);
        List<Product> productList = productRegister.getAllProducts();
        if (parameters.size() > 0) {
            List<Tag> listOfTags = getTagsFromMap(parameters);
            if(!listOfTags.isEmpty()){
                productList = productList.stream().filter(product ->
                        product.getProductDetails().checkIfTagsAreFound(listOfTags)).toList();
            }
            parameters.keySet().forEach(tagIdentifier -> model.addAttribute(tagIdentifier, tagIdentifier));
            List<Company> companies = getCompaniesFromMap(parameters);
            if(!companies.isEmpty()){
                productList = productList.stream().filter(product -> companies.stream().anyMatch(company ->
                        company.getCompanyID() == product.getCompany().getCompanyID())).toList();
            }
        }
        productList = checkIfSearch(parameters, productList, model);
        String sale = parameters.get("sale");
        boolean isSale = sale != null;
        if (isSale){
            productList = productList.stream().filter(Product::isOnSale).toList();
            if (productList.isEmpty()){
                model.addAttribute("sale", sale);
            }
        }
        List<Company> companies = productList.stream().map(product -> product.getCompany()).distinct().toList();
        List<Tag> tags = getTagsFromProduct(productList);
        model.addAttribute("relatedProduct", productList);
        //Todo: Istedet for at vi tar alle tagsene som er i systemet burde vi heller ta alle tagsene som er med disse produktene og vise dem.
        model.addAttribute("relatedTags", tags);
        model.addAttribute("companies" ,companies);
        return "products";
    }

    private List<Company> getCompaniesFromMap(Map<String, String> parameters) throws CouldNotGetCompanyException {
        Set<String> keySetForMap = parameters.keySet();
        List<Company> companies = new ArrayList<>();
        Iterator<String> it = keySetForMap.iterator();
        while (it.hasNext()) {
            String stringToTakeOut = it.next();
            String[] takeOutAsArray = stringToTakeOut.split("-");
            if (takeOutAsArray[0].equals("c")) {
                long companyID = Long.parseLong(takeOutAsArray[1]);
                companies.add(companyRegister.getCompanyWithId(companyID));
            }
        }
        return companies;
    }

    /**
     * Get the tags from a product.
     * @param products the products to get the tags from.
     * @return the tags.
     */
    private List<Tag> getTagsFromProduct(List<Product> products) {
        List<Tag> tags = new ArrayList<>();
        products.forEach(product -> {
            List<Tag> tagsToGet = product.getProductDetails().getTagList();
            tagsToGet.forEach(tag -> {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            });
        });
        return tags;
    }

    /**
     * Gets the tag from map so that we can use it for the sort methode later.
     * @param parameters the return map value.
     * @return the tag.
     * @throws CouldNotGetTagException is thrown if we could not get the tag from map.
     */
    private List<Tag> getTagsFromMap(Map<String, String> parameters) throws CouldNotGetTagException {
        Set<String> keySetForMap = parameters.keySet();
        List<Tag> tags = new ArrayList<>();
        Iterator<String> it = keySetForMap.iterator();
        while (it.hasNext()) {
            String stringToTakeOut = it.next();
            String[] takeOutAsArray = stringToTakeOut.split("-");
            if (takeOutAsArray[0].equals("t")) {
                long tagID = Long.parseLong(takeOutAsArray[1]);
                tags.add(tagRegister.getTagWithID(tagID));
            }
        }
        return tags;
    }

    /**
     * Checks if the input values is a search.
     * @param stringMap the string map.
     * @param productList the product list.
     * @param model the model.
     * @return a list with all the products that has that searchword in it.
     */
    private List<Product> checkIfSearch(Map<String, String> stringMap, List<Product> productList, Model model){
        String searchWord = stringMap.get("productSearch");
        List<Product> products = productList;
        if (searchWord != null && !searchWord.isBlank()) {
            products = products.stream().filter(product -> product.getProductName().length() >= searchWord.length())
                    .filter(product -> product.getProductName().toLowerCase().contains(searchWord.toLowerCase())).toList();
            if (productList.isEmpty()){
                model.addAttribute("searchWord", searchWord);
            }
        }
        return products;
    }


    /**
     * Makes an instance of the ProductController class.
     * @param companyJPA the company JPA
     * @param productJPA the product JPA.
     * @param tagJPA the tag JPA.
     */
    public WebProductController(ProductJPA productJPA, TagJPA tagJPA, CompanyJPA companyJPA) {
        this.productRegister = productJPA;
        this.tagRegister = tagJPA;
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
        model.addAttribute("isOnSale", false);

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
                ProductDetails productDetails = product.getProductDetails();
                addAndRemoveTags(productDetails, tags);
                Company company = companyRegister.getCompanyWithId(companyID);
                product.setCompany(company);
                product.setProductName(productName);
                product.setPrice(productPrice);
                int totalProduct = productAmount - product.getAmountOfProduct();
                if (totalProduct > 0){
                    product.addAmountOfProduct(totalProduct);
                }else if (totalProduct < 0){
                    product.removeAmountOfProduct(totalProduct);
                }

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
        } catch (CouldNotGetCompanyException e) {
            e.printStackTrace();
        }
        if (invalidInput || (preview != null && preview)){
            httpSession.setAttribute("productID", productID);
            httpSession.setAttribute("productName", productName);
            httpSession.setAttribute("productAmount", productAmount);
            httpSession.setAttribute("price", productPrice);
            httpSession.setAttribute("companyID", companyID);
            httpSession.setAttribute("ingredients", ingredients);
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
            tagList.add(tagRegister.getTagWithID(tagId));
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
        boolean admin = false;
        model.addAttribute("loggedIn", loggedIn);
        if (loggedIn){
            admin = getAccessUser(authentication).getUser().getRole() == Role.ROLE_ADMIN;
        }
        model.addAttribute("isAdmin", admin);
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
