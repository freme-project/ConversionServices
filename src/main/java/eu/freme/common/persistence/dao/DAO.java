package eu.freme.common.persistence.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;

/**
 * Created by Arne on 18.09.2015.
 */
public class DAO<Repository  extends CrudRepository<Entity, Long>, Entity> {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    Repository repository;

    Logger logger = Logger.getLogger(DAO.class);


    public void delete(Entity entity){
        repository.delete(entity);
        try {
            entityManager.flush();
            entityManager.clear();
        }catch(TransactionRequiredException e){
            logger.warn("Tried to flush and clear the entity manager, but didn't work! ("+e.getMessage()+")");
        }
    }


    public void save(Entity entity){
        repository.save(entity);
        //entityManager.flush();
        //entityManager.clear();
    }

    public long count(){
        return repository.count();
    }

    public Iterable<Entity> findAll(){
        return repository.findAll();
    }

    public Repository getRepository(){
        return repository;
    }


}
