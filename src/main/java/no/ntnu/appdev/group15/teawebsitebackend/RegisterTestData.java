package no.ntnu.appdev.group15.teawebsitebackend;


import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class RegisterTestData {

    /**
     * Adds test tags to a tags register.
     * @param tagsRegister the tags register to add tags too.
     * @throws CouldNotAddTagException gets thrown if one of the tags could not be added.
     */
    public static void addTestTags(TagsRegister tagsRegister) throws CouldNotAddTagException {
        checkIfObjectIsNull(tagsRegister, "tags register");
        if (tagsRegister.getAllTags().isEmpty()){
            tagsRegister.addTagWithDetails("Food", "Consumable that will keep ya alive.");
            tagsRegister.addTagWithDetails("Tools", "Things used to fix stuff");
            tagsRegister.addTagWithDetails("Tea", "A herb that is crushed and used to make water taste something more than minerals.");
            tagsRegister.addTagWithDetails("Crockery", "Stuff you use to eat on and drink from.");
            tagsRegister.addTagWithDetails("Environmentally Friendly", "A product made in a sustainable way for the planets sake.");
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private static void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
