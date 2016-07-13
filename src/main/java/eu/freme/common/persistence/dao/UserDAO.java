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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.repository.OwnedResourceRepository;
import eu.freme.common.persistence.repository.UserRepository;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Component
public class UserDAO extends DAO<UserRepository, User> {

	@SuppressWarnings("rawtypes")
	@Autowired
	List<OwnedResourceRepository> repositories;
	
    @PersistenceContext
    EntityManager entityManager;
	
    @SuppressWarnings("unchecked")
	@Override
	public void delete(User entity){
		User user = repository.findOneByName(entity.getName());

		for( OwnedResourceRepository<OwnedResource> repository : repositories){
			List<OwnedResource> list = repository.findAllByOwner(user);
			for( OwnedResource or : list ){
				entityManager.remove(or);
			}
		}
		entityManager.flush();
		
        super.delete(user);
    }
}
