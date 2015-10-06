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
package eu.freme.common.persistence.dao;


import eu.freme.common.exception.OwnedResourceNotFoundException;
import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.repository.OwnedResourceRepository;
import eu.freme.common.persistence.tools.AccessLevelHelper;
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

    public void delete(Entity entity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // DEBUG!!
        //decisionManager.decide(authentication, entity, accessLevelHelper.writeAccess());
        super.delete(entity);
    }


    public void save(Entity entity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        decisionManager.decide(authentication, entity, accessLevelHelper.writeAccess());
        super.save(entity);
    }

    public Entity findOneById(String id){
        Entity result = repository.findOneById(id);
        if(result==null)
            throw new OwnedResourceNotFoundException("Could not find resource with id='"+id+"'");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // DEBUG!!
        //decisionManager.decide(authentication, result, accessLevelHelper.readAccess());
        return result;
    }

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
            queryString = "select " + entity + " from " + entityName + " " + entity + " where " + entity + ".visibility = " + OwnedResource.Visibility.PUBLIC.ordinal(); //
        }else {
            User authUser = (User) authentication.getPrincipal();
            queryString = "select " + entity + " from " + entityName + " " + entity + " where " + entity + ".owner.name = '" + authUser.getName() + "' or " + entity + ".visibility = " + OwnedResource.Visibility.PUBLIC.ordinal(); //
        }
        return (List<Entity>)entityManager.createQuery(queryString).getResultList();
    }

}
