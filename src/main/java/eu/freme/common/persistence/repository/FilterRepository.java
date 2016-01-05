package eu.freme.common.persistence.repository;

import eu.freme.common.persistence.model.Filter;

/**
 * Created by Arne on 11.12.2015.
 */
public interface FilterRepository extends OwnedResourceRepository<Filter> {
    Filter findOneByName(String name);
}
