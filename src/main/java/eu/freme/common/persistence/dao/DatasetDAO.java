package eu.freme.common.persistence.dao;

import eu.freme.common.persistence.model.Dataset;
import org.springframework.stereotype.Component;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Component
public class DatasetDAO extends OwnedResourceDAO<Dataset> {
    @Override
    public String className() {
        return Dataset.class.getSimpleName();
    }
}
