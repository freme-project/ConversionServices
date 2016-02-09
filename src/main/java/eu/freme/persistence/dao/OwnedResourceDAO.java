/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.persistence.dao;


import eu.freme.common.exception.OwnedResourceNotFoundException;
import eu.freme.persistence.model.OwnedResource;
import eu.freme.persistence.model.User;
import eu.freme.persistence.repository.OwnedResourceRepository;
import eu.freme.persistence.tools.AccessLevelHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arne on 18.09.2015.
 */
public abstract class OwnedResourceDAO<Entity extends OwnedResource>  extends DAO<OwnedResourceRepository<Entity>, Entity>{

    @Autowired
    AbstractAccessDecisionManager decisionManager;

    @Autowired
    AccessLevelHelper accessLevelHelper;

    public abstract String className();

    @Override
	public void delete(Entity entity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, entity, accessLevelHelper.writeAccess());
        super.delete(entity);
    }

    @Override
	public Entity save(Entity entity){
        if(entity.getOwner() == null) {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            if(authentication instanceof AnonymousAuthenticationToken)
                throw new AccessDeniedException("Could not set current user as owner of created resource: The anonymous user can not own any resource. You have to be logged in to create a resource.");
            entity.setOwner((User) authentication.getPrincipal());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, entity, accessLevelHelper.writeAccess());
        return super.save(entity);
    }

    /**
     * @Depricated use findOneByIdentifier instead and override findOneByIdentifierUnsecured in
     * the DAO class of the entity you want to use if an identifier other then id is prefered/needed.
     * @param id
     * @return
     */
    @Deprecated
    public Entity findOneById(long id){
        Entity result = repository.findOneById(id);
        if(result==null)
            throw new OwnedResourceNotFoundException("Could not find resource with id='"+id+"'");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, result, accessLevelHelper.readAccess());
        return result;
    }

    public Entity findOneByIdentifier(String identifier){
        Entity result = findOneByIdentifierUnsecured(identifier);
        if(result==null)
            throw new OwnedResourceNotFoundException("Could not find resource with identifier='"+identifier+"'");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, result, accessLevelHelper.readAccess());
        return result;
    }

    public Entity findOneByIdentifierUnsecured(String identifier){
        return repository.findOneById(Integer.parseInt(identifier));
    }

    public Entity updateOwner(Entity entity, User newOwner){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, entity, accessLevelHelper.writeAccess());
        entity.setOwner(newOwner);
        return super.save(entity);
    }

    @SuppressWarnings("unchecked")
	public List<Entity> findAllReadAccessible(){
        if(repository.count()==0)
            return new ArrayList<>(0);

        String entityName = className();
        String entity = entityName.toLowerCase();
        String queryString;
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) {
            logger.debug("Find owned resources as ANONYMOUS USER");
            queryString = "select " + entity + " from " + entityName + " " + entity + " where " + entity + ".visibility = " + OwnedResource.Visibility.PUBLIC.ordinal()+" order by id"; //
        }else {
            User authUser = (User) authentication.getPrincipal();
            if(authUser.getRole().equals(User.roleAdmin)) {
                queryString = "select " + entity + " from " + entityName + " " + entity + " order by id";
            }else {
                queryString = "select " + entity + " from " + entityName + " " + entity + " where " + entity + ".owner.name = '" + authUser.getName() + "' or " + entity + ".visibility = " + OwnedResource.Visibility.PUBLIC.ordinal() + " order by id"; //
            }
        }
        return entityManager.createQuery(queryString).getResultList();
    }

}
