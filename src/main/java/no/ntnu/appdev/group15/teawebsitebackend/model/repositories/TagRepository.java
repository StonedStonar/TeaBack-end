package no.ntnu.appdev.group15.teawebsitebackend.model.repositories;

import no.ntnu.appdev.group15.teawebsitebackend.model.Tag;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
}
