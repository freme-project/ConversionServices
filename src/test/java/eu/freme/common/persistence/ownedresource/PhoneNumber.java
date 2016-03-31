package eu.freme.common.persistence.ownedresource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import eu.freme.common.persistence.model.User;


@Entity
public class PhoneNumber {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;

	String number;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
