package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
}
