package model;

public class Person extends User {
	
	private String firstName, lastName, email;
	
	private boolean isOnline;
	private UserCalendar personalCalendar;
	
	public Person(	String username,
					String password,
					String firstName,
					String lastName,
					String email) {
		
		super(username, password);
		
		String[] args = {firstName, lastName};
		for (String s : args)
			if (s.isEmpty())
				throw new IllegalArgumentException();
		
		this.firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
		this.lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
	}
	
	public Person(	String username,
			String password,
			String firstName,
			String lastName,
			String email,
			boolean isOnline,
			UserCalendar calendar) {
		this(username, password, firstName, lastName, email);
		
		this.isOnline = isOnline;
		this.personalCalendar = calendar;
	}
	
	public String toString() {
		String desc = super.toString() + ", name: " + lastName + ", " + firstName;
		return desc;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	
	
	public String getEmail() {
		return email;
	}

	public UserCalendar getPersonalCalendar() {
		return personalCalendar;
	}

	public String getLastName(){
		return lastName;
	}
}