package model;

public class User {
	
	private String username, password;
	
	public User(	String username,
					String password) {
		
		String[] args = {username, password};
		for (String s : args)
			if (s.isEmpty())
				throw new IllegalArgumentException();
		
		this.username = username.toLowerCase();
		this.password = password;
	}
	
	public String toString() {
		String desc = "User: " + username;
		return desc;
	}
	
	public boolean equals(User other) {
		return this.username.equals(other.username);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
