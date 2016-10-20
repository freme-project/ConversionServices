/**
 * Copyright © 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.persistence.ownedresource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.freme.common.persistence.dao.PhoneNumberDAO;
import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.PhoneNumber;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.repository.UserRepository;

/**
 * Helper for OwnedResourceTest
 * 
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@Component
public class PhoneService {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	PhoneNumberDAO phoneNumberDAO;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	UserRepository userRepository;
	
	final String userName = "henriette";
	
	@Transactional
	public void createEntities(){	
		User user = new User(userName, "password", User.roleUser);
		userDAO.save(user);
		
		PhoneNumber phoneNumber = new PhoneNumber(user);
		phoneNumber.setNumber("12345");
		phoneNumberDAO.save(phoneNumber);		
		
		entityManager.flush();
		entityManager.clear();
	}
	
	@Transactional
	public long countPhoneNumbers(){
		return phoneNumberDAO.count();
	}
	
	public long countUsers(){
		return userDAO.count();
	}
	
	@Transactional
	public void deleteUser(){
		User user = userRepository.findOneByName(userName);
		userDAO.delete(user);
		entityManager.flush();
		entityManager.clear();
	}
	
	@Transactional
	public void deletePhone(){
		PhoneNumber phone = phoneNumberDAO.findAll().iterator().next();
		phoneNumberDAO.delete(phone);
	}
}
