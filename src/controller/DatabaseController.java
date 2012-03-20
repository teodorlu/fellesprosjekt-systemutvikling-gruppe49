package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.Person;
import model.User;

public class DatabaseController {
	
	private Connection con;
	
	private void connect() throws InstantiationException, IllegalAccessException {
		con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://mysql.stud.ntnu.no/magnurod_avtalekalender";
			String user = "magnurod_fellesp";
			String pw = "stabak";
			con = DriverManager.getConnection(url, user, pw);

		} catch (SQLException ex) {
			System.out.println("Tilkobling feilet: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out
					.println("Feilet under driverlasting: " + ex.getMessage());
			System.out.println(ex);
		}
	}
	
	private void disconnect() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException ex) {
			System.out.println("Epic fail: " + ex.getMessage());
		}
	}
	
	public void Save(Person user) {
		// TODO complete
	}
	
	public boolean authenticated(User user){
		// TODO check username and password in database
		return true;
	}

	public Person retrieve(User user) {
		// TODO generate Person object from database corresponding to user.username
		// This includes the Person's appointments: person.personalCalendar.appointments
		return null;
	}
}


