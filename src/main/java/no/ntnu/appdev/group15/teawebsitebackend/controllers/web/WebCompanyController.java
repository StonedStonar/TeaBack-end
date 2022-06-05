package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import javax.servlet.http.HttpSession;
import no.ntnu.appdev.group15.teawebsitebackend.model.Address;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.CompanyDetails;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetCompanyException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The controller that manages the companies pages.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebCompanyController extends WebController{

    private CompanyRegister companyRegister;

    /**
     * Makes an instance of the WebCompanyController class.
     * @param companyJPA the company JPA.
     */
    public WebCompanyController(CompanyJPA companyJPA) {
        super();
        this.companyRegister = companyJPA;
    }

    /**
     * Gets the overview of all the companies.
     * @param model the model.
     * @param authentication the authentication.
     * @return the HTML name of companies overview.
     */
    @GetMapping("/companyOverview")
    public String getCompanyOverview(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        List<Company> companyList = companyRegister.getAllCompanies();
        model.addAttribute("companies", companyList);
        return "companies";
    }

    /**
     * Get the editCompany page
     */
    @GetMapping("/editCompany")
    public String getEditCompany(Model model, Authentication authentication, @RequestParam ("companyID") long companyID, HttpSession httpSession)
        throws CouldNotGetCompanyException {
        addLoggedInAttributes(authentication, model);
        boolean isPreview = httpSession.getAttribute("isPreview") != null ? (boolean) httpSession.getAttribute("isPreview") : false;
        if (companyID == 0 && !isPreview) {
            model.addAttribute("companyID", companyID);
        }else if (!isPreview){
            Company company = companyRegister.getCompanyWithId(companyID);
            model.addAttribute("companyName", company.getCompanyName());
            model.addAttribute("companyID", company.getCompanyID());
            model.addAttribute("postalCode", company.getDetails().getAddress().getPostalCode());
            model.addAttribute("houseNumber", company.getDetails().getAddress().getHouseNumber());
            model.addAttribute("companyDescription", company.getDetails().getDescription());
            model.addAttribute("postalPlace", company.getDetails().getAddress().getPostalPlace());
            model.addAttribute("streetName", company.getDetails().getAddress().getStreetName());
            model.addAttribute("country", company.getDetails().getAddress().getCountry());
        }

        addAllAttributes(model, httpSession);
        return "editCompany";
    }

    @PutMapping("/editCompany")
    public RedirectView editCompany (@RequestParam ("companyID") long companyID, @RequestParam ("companyName") String companyName,
                                     @RequestParam ("companyDescription") String companyDescription, @RequestParam ("postalCode") String postalCodeAsString,
                                     @RequestParam ("postalPlace") String postalPlace, @RequestParam ("streetName") String streetName, @RequestParam ("houseNumber") String houseNumberAsString,
                                     @RequestParam ("country") String country, @RequestParam(value = "preview", required = false) String preview, HttpSession httpSession){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editCompany");
        parameterBuilder.addParameter("companyID", Long.toString(companyID));
        boolean isPreview = preview != null;
        httpSession.setAttribute("isPreview", isPreview);
        boolean invalidInput = false;
        int postalCode = 0;
        int houseNumber = 0;
        try {
            postalCode = Integer.parseInt(postalCodeAsString);
            houseNumber = Integer.parseInt(houseNumberAsString);
            if (companyID == 0 && preview == null){
                Address address = new Address(postalCode, postalPlace, streetName, houseNumber, country);
                CompanyDetails companyDetails = new CompanyDetails(companyDescription, address);
                Company company = new Company(companyName, companyDetails);
                companyRegister.addCompany(company);
                parameterBuilder.addParameter("addedCompany", "true");
            }else if (isPreview){
                parameterBuilder.addParameter("isPreview", "true");
            } else {
                Company company = companyRegister.getCompanyWithId(companyID);
                Address address = company.getDetails().getAddress();
                company.setCompanyName(companyName);
                company.getDetails().setDescription(companyDescription);
                address.setPostalCode(postalCode);
                address.setPostalPlace(postalPlace);
                address.setStreetName(streetName);
                address.setHouseNumber(houseNumber);
                address.setCountry(country);
                companyRegister.updateCompany(company);
                parameterBuilder.addParameter("updatedCompany", "true");
            }
        } catch (CouldNotGetCompanyException | CouldNotAddCompanyException exception) {
            parameterBuilder.addParameter("invalidCompanyID", "true");
            invalidInput = true;
        } catch (IllegalArgumentException exception) {
            parameterBuilder.addParameter("invalidFields", "true");
            invalidInput = true;
        }

        if (isPreview || invalidInput) {
            httpSession.setAttribute("companyName", companyName);
            httpSession.setAttribute("companyID", companyID);
            httpSession.setAttribute("postalCode", postalCode);
            httpSession.setAttribute("houseNumber", houseNumber);
            httpSession.setAttribute("companyDescription", companyDescription);
            httpSession.setAttribute("postalPlace", postalPlace);
            httpSession.setAttribute("streetName", streetName);
            httpSession.setAttribute("country", country);
        }


        return new RedirectView(parameterBuilder.buildString(), true);
    }

}
