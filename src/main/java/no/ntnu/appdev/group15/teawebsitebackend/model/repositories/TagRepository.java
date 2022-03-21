package no.ntnu.appdev.group15.teawebsitebackend.model.repositories;

import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface TagRepository extends CrudRepository<Tag, Long> {

    /**
     * Gets a tag with the same name as input.
     * @param tagName the tag to look for.
     * @return the optional that might contain the wanted tag.
     */
    @Query(value = "SELECT * FROM tag WHERE tagName = :tagName", nativeQuery = true)
    Optional<Tag> getTagWithTagName(@Param("tagName") String tagName);
}
