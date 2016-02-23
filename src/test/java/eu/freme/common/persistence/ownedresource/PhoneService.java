package eu.freme.common.persistence.ownedresource;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.User;

@Component
public class PhoneService {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	PhoneNumberDAO phoneNumberDAO;
	
	@Transactional
	public void createEntities(){	
		User user = new User("henry", "password", User.roleUser);
		userDAO.save(user);
		
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setNumber("12345");
		phoneNumber.setUser(user);
		phoneNumberDAO.save(phoneNumber);
	}
	
	public long countPhoneNumbers(){
		return phoneNumberDAO.count();
	}
	
	public long countUsers(){
		return userDAO.count();
	}
	
	@Transactional
	public void deleteUser(){
		User user = userDAO.findAll().iterator().next();
		userDAO.delete(user);
	}
	
	@Transactional
	public void deletePhone(){
		PhoneNumber phone = phoneNumberDAO.findAll().iterator().next();
		phoneNumberDAO.delete(phone);
	}
}
