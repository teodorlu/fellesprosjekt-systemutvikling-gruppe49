package model;

public class Person {
	
	private String username, password;
	private String firstName, lastName;
	
	boolean isOnline;
	UserCalendar personalCalendar;
	
	public Person(	String username,
					String password,
					String firstName,
					String lastName)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
