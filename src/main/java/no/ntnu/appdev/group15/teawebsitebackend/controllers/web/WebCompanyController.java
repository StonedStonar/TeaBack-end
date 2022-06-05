package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import javax.servlet.http.HttpSession;
import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
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
    public String getEditCompany(Model model, Authentication authentication, @RequestParam ("companyID") long companyID) {
        addLoggedInAttributes(authentication, model);
        try {
            if (companyID == 0) {

            }else {
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
        } catch (CouldNotGetCompanyException exception) {
            //TODO
        }
        return "editCompany";
    }

    @PutMapping("/editCompany")
    public RedirectView editCompany (@RequestParam ("companyID") long companyID, @RequestParam ("companyName") String companyName,
                                     @RequestParam ("companyDescription") String companyDescription, @RequestParam ("postalCode") int postalCode,
                                     @RequestParam ("postalPlace") String postalPlace, @RequestParam ("streetName") String streetName, @RequestParam ("houseNumber") int houseNumber,
                                     @RequestParam ("country") String country, @RequestParam(value = "preview", required = false) String preview, HttpSession httpSession){
        ParameterBuilder parameterBuilder = new ParameterBuilder("editCompany");
        parameterBuilder.addParameter("companyID", Long.toString(companyID));
        try {
            if (companyID == 0 && preview == null){

            }else if (preview != null){

            } else {
                Company company = companyRegister.getCompanyWithId(companyID);
                company.setCompanyName(companyName);
                company.getDetails().setDescription(companyDescription);
                company.getDetails().getAddress();

            }
        } catch (CouldNotGetCompanyException exception) {

        }


        return new RedirectView(parameterBuilder.buildString(), true);
    }

}
