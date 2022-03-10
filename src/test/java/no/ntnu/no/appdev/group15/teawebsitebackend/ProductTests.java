package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Details;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ProductTests {

    private Product product;

    Details details = new TeaDetails();
    Company company = new Company();


    @Test
    public void testIfConstructorWorkWithValidInput() {
        try {
            Product product = new Product(1, "Mushroom Tea", 32, 2, details, company);
            assertTrue(true);
        } catch (IllegalArgumentException exception) {
            fail("Expected a product to be made with valid input.");
        }
    }
}
