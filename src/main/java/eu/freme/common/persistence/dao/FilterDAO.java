package eu.freme.common.persistence.dao;

import eu.freme.common.persistence.model.Filter;
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

}
