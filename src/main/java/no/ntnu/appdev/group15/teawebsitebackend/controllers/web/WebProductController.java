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
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static java.lang.Long.sum;


/**
 * @author Kenneth Johansen Misund and Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebProductController extends WebController{

    private ProductRegister productRegister;

    private CompanyRegister companyRegister;

    private TagsRegister tagRegister;

    /**
     * Makes an instance of the ProductController class.
     * @param companyJPA the company JPA
     * @param productJPA the product JPA.
     * @param tagJPA the tag JPA.
     */
    public WebProductController(ProductJPA productJPA, TagJPA tagJPA, CompanyJPA companyJPA) {
        super();
        this.productRegister = productJPA;
        this.tagRegister = tagJPA;
        this.companyRegister = companyJPA;
    }

    /**
     * Gets the product into page.
     * @param authentication the authentication
     * @param model the model
     * @return the product info page.
     */
    @GetMapping("/product-info")
    public String getProductInfo(Authentication authentication, Model model, @RequestParam("id") long id)
            throws CouldNotGetProductException {
        addLoggedInAttributes(authentication, model);
        List<Product> productList = productRegister.getAllProducts();
        Product product = null;
        model.addAttribute("sale", false);
        product = productRegister.getProduct(id);
        model.addAttribute("mainProduct", product);
        model.addAttribute("relatedProduct", productList);
        model.addAttribute("productDetail", product.getProductDetails());
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
                        product.getProductDetails().checkIfTagsAreFound(listOfTags)).collect(Collectors.toList());
            }
            parameters.keySet().forEach(tagIdentifier -> model.addAttribute(tagIdentifier, tagIdentifier));
            List<Company> companies = getCompaniesFromMap(parameters);
            if(!companies.isEmpty()){
                productList = productList.stream().filter(product -> companies.stream().anyMatch(company ->
                        company.getCompanyID() == product.getCompany().getCompanyID())).collect(Collectors.toList());
            }
        }
        productList = checkIfSearch(parameters, productList, model);
        String sale = parameters.get("sale");
        boolean isSale = sale != null;
        if (isSale){
            productList = productList.stream().filter(Product::isOnSale).collect(Collectors.toList());
            if (productList.isEmpty()){
                model.addAttribute("sale", sale);
            }
        }
        List<Company> companies = productList.stream().map(Product::getCompany).distinct().collect(Collectors.toList());;
        List<Tag> tags = getTagsFromProduct(productList);
        model.addAttribute("relatedProduct", productList);
        model.addAttribute("relatedTags", tags);
        model.addAttribute("companies" ,companies);
        return "products";
    }

    /**
     * Gets the companies from the parameter map.
     * @param parameters the map.
     * @return the companies matching the set id's.
     * @throws CouldNotGetCompanyException gets thrown if the company could not be found in the DB.
     */
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
                    .filter(product -> product.getProductName().toLowerCase().contains(searchWord.toLowerCase())).collect(Collectors.toList());
            if (productList.isEmpty()){
                model.addAttribute("searchWord", searchWord);
            }
        }
        return products;
    }

    /**
     * Gets the edit product page.
     * @param authentication the authentication.
     * @param model the model.
     * @param httpSession the http session.
     * @param productID the product ID.
     * @return the url to the editProdut html.
     */
    @GetMapping("/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String getEditProduct(Authentication authentication, Model model, HttpSession httpSession,
                                 @RequestParam("productID") long productID){
        addLoggedInAttributes(authentication, model);
        Boolean isPreview = (Boolean) httpSession.getAttribute("isPreview");
        httpSession.removeAttribute("isPreview");
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProduct");
        if (isPreview != null && isPreview || httpSession.getAttribute("productID") != null){
            addAllAttributes(model, httpSession);
        }else {
            Product product = null;
            if (productID > 0){
                try {
                    product = productRegister.getProduct(productID);
                }catch (CouldNotGetProductException exception){
                    parameterBuilder.addParameter("invalidProductID", "true");
                }
            }
            model.addAttribute("productID", product != null ? product.getProductID() : 0);
            model.addAttribute("productName", product != null ? product.getProductName(): "");
            model.addAttribute("productAmount", product != null ? product.getAmountOfProduct(): 0);
            model.addAttribute("price", product != null ? product.getPrice():0);
            model.addAttribute("companyID", product != null ? product.getCompany().getCompanyID(): 0);
            model.addAttribute("ingredients", product != null ? product.getProductDetails().getIngredients(): "");
            model.addAttribute("description", product != null ? product.getProductDetails().getDescription(): "");
            model.addAttribute("tags", product != null ? convertListToValues(product.getProductDetails().getTagList()) : convertListToValues(new ArrayList<>()));
            model.addAttribute("shortDescription", product != null ? product.getProductDetails().getShortDescription() : "");
        }
        model.addAttribute("isOnSale", false);

        String url = parameterBuilder.buildString();

        return url;
    }

    /**
     * Updates the wanted product with its new details.
     * @param productID the product id.
     * @param productName the product name.
     * @param productAmount the product amount.
     * @param productPrice the product price.
     * @param companyID the company ID.
     * @param ingredients the ingredeints.
     * @param tags the tags.
     * @param description the description.
     * @param shortDescription the short description.
     * @param preview is here when its a preview.
     * @param httpSession the storage medium between this method and editProduct metohd.
     * @return a redirect to the editProduct page.
     */
    @PutMapping("/editProduct")
    public RedirectView editProduct(@RequestParam("productID") long productID, @RequestParam("productName") String productName, @RequestParam("productAmount") int productAmount,
                                    @RequestParam("productPrice") int productPrice, @RequestParam("companyID") int companyID,
                                    @RequestParam("ingredients") String ingredients, @RequestParam("tags") List<Long> tags, @RequestParam("description") String description, @RequestParam("shortDescription") String shortDescription
                                    ,@RequestParam(value = "previewProduct", required = false) Boolean preview,  HttpSession httpSession){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editProduct");
        parameterBuilder.addParameter("productID", String.valueOf(productID));
        boolean invalidInput = false;
        try {
            Product product = null;
            if (productID == 0){
                ProductDetails productDetails = new ProductDetails(description, ingredients, shortDescription);
                Company company = companyRegister.getCompanyWithId(companyID);
                product = new Product(productName, productPrice, productAmount, productDetails, company);
                productRegister.addProduct(product);
                parameterBuilder.addParameter("productAdded", "true");
            }else {
                product = productRegister.getProduct(productID);
                if (preview != null && preview){
                    parameterBuilder.addParameter("tagPreview", "true");
                    httpSession.setAttribute("isPreview", true);
                }else {
                    ProductDetails productDetails = product.getProductDetails();
                    addAndRemoveTags(productDetails, tags);
                    productDetails.setShortDescription(shortDescription);
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
                    productRegister.updateProduct(product);
                    parameterBuilder.addParameter("productUpdated", "true");
                }
            }
        }catch (IllegalArgumentException  exception){
            parameterBuilder.addParameter("invalidFieldInput", "true");
            invalidInput = true;
        }catch (CouldNotGetProductException | CouldNotAddProductException exception){
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
            parameterBuilder.addParameter("invalidCompanyID", "true");
            invalidInput = true;
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
            httpSession.setAttribute("shortDescription", shortDescription);
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
        List<Tag> tagsToRemove = productTagList.stream().filter(tag -> !tagList.contains(tag)).collect(Collectors.toList());
        List<Tag> tagsToAdd = tagList.stream().filter(tag -> !productTagList.contains(tag)).collect(Collectors.toList());
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
}
