package model;

import java.util.Arrays;
import java.util.List;

public class Person {
	
	private String username, password;
	private String firstName, lastName;
	
	boolean isOnline;
	UserCalendar personalCalendar;
	
	public Person(	String username,
					String password,
					String firstName,
					String lastName){
		
		String[] args = {username, password, firstName, lastName};
		for (String s : args)
			if (s.isEmpty())
				throw new IllegalArgumentException();
		
		this.username = username.toLowerCase();
		this.password = password;
		
		this.firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
		this.lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	}
	
	public String toString() {
		String desc = username + ": " + lastName + ", " + firstName;
		return desc;
	}
}