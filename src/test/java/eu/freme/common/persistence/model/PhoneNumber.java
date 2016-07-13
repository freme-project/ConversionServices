package eu.freme.common.persistence.model;

import javax.persistence.Entity;


@SuppressWarnings("serial")
@Entity
public class PhoneNumber extends OwnedResource{

	public PhoneNumber(){
		super(null);
	}
	
	public PhoneNumber(User owner){
		super(owner);
	}
	
	String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
