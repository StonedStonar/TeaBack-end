package no.ntnu.appdev.group15.teawebsitebackend.controllers.web;

import no.ntnu.appdev.group15.teawebsitebackend.model.Role;
import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.TagJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotGetTagException;
import no.ntnu.appdev.group15.teawebsitebackend.model.registers.TagsRegister;
import no.ntnu.appdev.group15.teawebsitebackend.security.AccessUser;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Kenneth Johansen Misund and Steinar Hjelle Midthus
 * @version 0.1
 */
@Controller
public class WebTagController extends WebController{

    private TagsRegister tagsRegister;

    /**
     * Makes a instance of the tag controller.
     * @param tagJPA the tagJPA.
     */
    public WebTagController(TagJPA tagJPA) {
        super();
        this.tagsRegister = tagJPA;
    }

    /**
     * Gets the overview of all the tags.
     * @param model the model.
     * @param authentication the authentication.
     * @return the HTML file name of tags overview.
     */
    @GetMapping("/tagsOverview")
    public String getTagsOverview(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        List<Tag> tagList = tagsRegister.getAllTags();
        model.addAttribute("tags", tagList);
        return "tags";
    }

    /**
     * Gets the edit tag page.
     * @param authentication the authentication.
     * @param model the model.
     * @param id the ID of the tag.
     * @param httpSession the HTTP session for data storage.
     * @return the HTML file name of edit tag.
     */
    @GetMapping("/edit-tag")
    @PreAuthorize("hasRole('ADMIN')")
    public String editTag(Authentication authentication, Model model, @RequestParam("tagID") long id, HttpSession httpSession) {
        addLoggedInAttributes(authentication, model);
        Tag tag = (Tag) httpSession.getAttribute("tag");
        if (tag == null && id > 0) {
            try {
                tag = tagsRegister.getTagWithID(id);
            } catch (CouldNotGetTagException e) {
                e.printStackTrace();
            }
        }else {
            httpSession.removeAttribute("tag");
        }
        if (tag != null){
            model.addAttribute("tagName", tag.getTagName());
            model.addAttribute("tagDescription", tag.getTagDescription());
        }
        model.addAttribute("tagID", id);
        return "editTag";
    }

    /**
     * Updates a tag's details. Makes a new tag if the id is 0.
     * @param tagName the tag name.
     * @param tagDescription the tag description.
     * @param preview is present if it's supposed to be a preview of the tag.
     * @param id the ID of the tag.
     * @param httpSession the HTTP session to store data in.
     * @return a redirect to the edit tag window.
     */
    @PutMapping("/updateTag")
    @PreAuthorize("hasRole('ADMIN')")
    public RedirectView updateTag(@RequestParam("tagName") String tagName,
                                  @RequestParam("tagDescription") String tagDescription, @RequestParam(value = "isPreviewed", required = false) String preview, @RequestParam("tagID") long id, HttpSession httpSession) {
        ParameterBuilder parameterBuilder = new ParameterBuilder("edit-tag");
        parameterBuilder.addParameter("tagID", Long.toString(id));
        try {
            if (preview != null) {
                Tag tag = new Tag(tagName, tagDescription);
                httpSession.setAttribute("tag", tag);
                parameterBuilder.addParameter("isPreviewed", "true");
            } else if (id == 0){
                Tag tag = new Tag(tagName, tagDescription);
                tagsRegister.addTag(tag);
                parameterBuilder.addParameter("tagAdded", "true");
            }else {
                Tag tag = tagsRegister.getTagWithID(id);
                tag.setTagName(tagName);
                tag.setTagDescription(tagDescription);
                tagsRegister.updateTag(tag);
                parameterBuilder.addParameter("tagIsUpdated", "true");
            }
        } catch (CouldNotGetTagException e){
            parameterBuilder.addParameter("invalidTagID", "true");
        } catch (IllegalArgumentException ex){
            parameterBuilder.addParameter("invalidInput", "true");
        } catch (CouldNotAddTagException exception) {
            parameterBuilder.addParameter("takenTagID", "true");
        }

        return new RedirectView(parameterBuilder.buildString(), true);
    }
}


