package eu.freme.common.persistence.dao;

import eu.freme.common.exception.OwnedResourceNotFoundException;
import eu.freme.common.persistence.model.Filter;
import eu.freme.common.persistence.repository.FilterRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by Arne on 11.12.2015.
 */

@Deprecated
@Component
public class FilterDAO extends OwnedResourceDAO<Filter> {

    @Override
    public String className() {
        return Filter.class.getSimpleName();
    }

    @Override
    public Filter findOneByIdentifierUnsecured(String identifier){
        return ((FilterRepository)repository).findOneByName(identifier);
    }

}
