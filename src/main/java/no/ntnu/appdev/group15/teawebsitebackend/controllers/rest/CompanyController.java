package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import no.ntnu.appdev.group15.teawebsitebackend.model.database.CompanyJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.CompanyRegister;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents the CompanyController needed to connect Company to database.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */

@RestController
@RequestMapping("/Companies")
public class CompanyController {

    private CompanyRegister companyRegister;

    /**
     * Makes an instance of the CompanyRegister class.
     * @param companyJPA the Company repository to access the database.
     */
    public CompanyController(CompanyJPA companyJPA) {
        companyRegister = companyJPA;
    }
}
