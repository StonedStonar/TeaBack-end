package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

public class TeaDetails implements Details {


    public TeaDetails() {

    }


    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void addTag(Tag tag) throws CouldNotAddTagException {

    }

    @Override
    public void removeTag(Tag tag) throws CouldNotRemoveTagException {

    }

    @Override
    public boolean checkIfTagIsPartOfDetails(Tag tag) {
        return false;
    }
}
