package no.ntnu.no.appdev.group15.teawebsitebackend;

import no.ntnu.appdev.group15.teawebsitebackend.model.Company;
import no.ntnu.appdev.group15.teawebsitebackend.model.Details;
import no.ntnu.appdev.group15.teawebsitebackend.model.Product;
import no.ntnu.appdev.group15.teawebsitebackend.model.TeaDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Kenneth Misund
 * @version 0.1
 */
public class ProductTests {

    private Product product;

    //De var slik
    //private Details details = new TeaDetails();
    //private Company company = new Company();

    //Burde være slik
    private Details details;
    private Company company;

    //At feltet er lik noe setter vi heller inn i en metode som har annotasjonen @BeforeEach
    //Da vet vi at setupet er på et sted og trenger bare å se der om det kommer feil.
    //Samtidig kan vi håndtere feil som kan oppstå under testingen.
    //Before each gjør den metoden før alle testene og kan dermed fylle dine predefinerte felt.

    //Todo: For eksempler kan du se på linje 61 i UserTest klassen.

    /**
     * Sets up the basic test environment.
     */
    @BeforeEach
    public void setupTestProduct(){
        details = new TeaDetails();
        company = new Company();
        //Todo: Sett inn et produkt som du skal bruke under testingen. Så slipper du å ha "Product product = new Product(*alle inputter*)" overalt.
        try{
            //Todo: Konstruktøren med produktet. så "product = new Product(*all items*)"
        }catch (IllegalArgumentException exception){
            //Todo: Her kan vi legge til en feil. Se for deg at du er proff fotballspiller men før kampen brekker du foten.
            //Todo: Da burde du si ifra til laget (testeren) at det gikk feil ganske tidlig og ikke under kampen.
            //Todo: Eksempel er "fail("Excepted the football player to be made since the setup has valid input.")"
            //Se tag for eksempel
        }

    }


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
