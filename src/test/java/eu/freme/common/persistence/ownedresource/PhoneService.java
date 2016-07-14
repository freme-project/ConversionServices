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
