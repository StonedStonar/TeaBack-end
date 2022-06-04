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

    public WebTagController(TagJPA tagJPA) {
        super();
        this.tagsRegister = tagJPA;
    }

    @GetMapping("/tagsOverview")
    public String getTagsOverview(Model model, Authentication authentication){
        addLoggedInAttributes(authentication, model);
        List<Tag> tagList = tagsRegister.getAllTags();
        model.addAttribute("tags", tagList);
        return "tags";
    }

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


