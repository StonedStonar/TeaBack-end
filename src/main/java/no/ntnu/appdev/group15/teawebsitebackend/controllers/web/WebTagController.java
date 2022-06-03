package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

/**
 * @author Kenneth Johansen Misund and Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebTagController {

    private TagsRegister tagsRegister;

    public WebTagController(TagJPA tagJPA) {
        this.tagsRegister = tagJPA;
    }

    @GetMapping("/edit-tag")
    public String editTag(Authentication authentication, Model model, @RequestParam("tagID") long id, HttpSession httpSession) {
        addLoggedInAttributes(authentication, model);
        Tag tag = (Tag) httpSession.getAttribute("tag");
        if (tag == null) {
            try {
                tag = tagsRegister.getTagWithID(id);


            } catch (CouldNotGetTagException e) {
                e.printStackTrace();
            }
        }else {
            httpSession.removeAttribute("tag");
        }
        model.addAttribute("tag", tag);
        return "editTag";
    }

    @PutMapping("/updateTag")
    public RedirectView updateTag(@RequestParam("tagName") String tagName,
                                  @RequestParam("tagDescription") String tagDescription, @RequestParam("isPreviewed") String preview, @RequestParam("tagID") long id, HttpSession httpSession) {

        ParameterBuilder parameterBuilder = new ParameterBuilder("edit-tag");
        parameterBuilder.addParameter("tagID", Long.toString(id));
        try {
            if (preview != null) {
                Tag tag = new Tag(tagName, tagDescription);
                httpSession.setAttribute("tag", tag);

            } else {
                Tag tag = tagsRegister.getTagWithID(id);
                tag.setTagName(tagName);
                tag.setTagDescription(tagDescription);
                tagsRegister.updateTag(tag);
            }

        } catch (CouldNotGetTagException e){
            e.printStackTrace();
        } catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }


        return new RedirectView(parameterBuilder.buildString(), true);
    }



    /**
     * Gets the access user that is using the page.
     * @param authentication the authentication object.
     * @return the access user of this session.
     */
    private AccessUser getAccessUser(Authentication authentication){
        return (AccessUser) authentication.getPrincipal();
    }

    /**
     * Adds a logged in attribute to the model.
     * @param authentication the authentication.
     * @param model the model.
     */
    private void addLoggedInAttributes(Authentication authentication, Model model){
        boolean loggedIn = authentication != null;
        boolean admin = false;
        model.addAttribute("loggedIn", loggedIn);
        if (loggedIn){
            admin = getAccessUser(authentication).getUser().getRole() == Role.ROLE_ADMIN;
        }
        model.addAttribute("isAdmin", admin);
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    /**
     * Check to make sure that integer values cannot be negative.
     * @param object the object to be checked.
     * @param error exception message to be displayed.
     */
    private void checkIfNumberNotNegative(long object, String error) {
        if (object <= 0) {
            throw new IllegalArgumentException("The " + error + " Cannot be negative values.");
        }
    }
}


