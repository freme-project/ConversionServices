package eu.freme.common.persistence.repository;

import java.util.List;

import eu.freme.common.persistence.model.DatasetSimple;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 15.10.2015.
 */

public interface DatasetSimpleRepository extends CrudRepository<DatasetSimple, Long> {

    DatasetSimple findOneById(long id);
    DatasetSimple findOneByName(String name);
    List<DatasetSimple> findAll();

}
