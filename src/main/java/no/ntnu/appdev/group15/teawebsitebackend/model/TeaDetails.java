package no.ntnu.appdev.group15.teawebsitebackend.model;

import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotRemoveTagException;

public class TeaDetails implements Details{

    public TeaDetails(){

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
