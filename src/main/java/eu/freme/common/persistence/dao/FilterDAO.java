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
@Component
public class FilterDAO extends OwnedResourceDAO<Filter> {

    @Override
    public String className() {
        return Filter.class.getSimpleName();
    }

    /*public Filter findOneByName(String name){
        Filter result = ((FilterRepository)repository).findOneByName(name);
        if(result==null)
            throw new OwnedResourceNotFoundException("Could not find filter with name='"+name+"'");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, result, accessLevelHelper.readAccess());
        result.constructQuery();
        return result;
    }

    @Override
    public Filter findOneById(long id){
        Filter filter = super.findOneById(id);
        filter.constructQuery();
        return filter;
    }*/

    @Override
    public Filter findOneByIdentifierUnsecured(String identifier){
        return ((FilterRepository)repository).findOneByName(identifier);
    }

}
