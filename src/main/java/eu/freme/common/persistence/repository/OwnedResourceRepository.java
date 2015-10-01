package eu.freme.common.persistence.repository;

import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.Template;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by Arne on 18.09.2015.
 */

@NoRepositoryBean
public interface OwnedResourceRepository<T extends OwnedResource> extends CrudRepository<T, Long> {
    T findOneById(String name);
}
