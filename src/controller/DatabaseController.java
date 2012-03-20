package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import model.Person;
import model.User;

public class DatabaseController {
	
	private static Connection con;
	
	private static void connect() throws InstantiationException, IllegalAccessException {
		con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://mysql.stud.ntnu.no/magnurod_avtalekalender";
			String user = "magnurod_fellesp";
			String pw = "stabak";
			con = DriverManager.getConnection(url, user, pw);
			
			if (con != null)
				System.out.println("nice");

		} catch (SQLException ex) {
			System.out.println("Tilkobling feilet: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out
					.println("Feilet under driverlasting: " + ex.getMessage());
			System.out.println(ex);
		}
	}
	
	private static void disconnect() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException ex) {
			System.out.println("Epic fail: " + ex.getMessage());
		}
	}
	
	public static void Save(Person user) {
		String username = "'"+user.getUsername()+"'";
		String pw = "'"+user.getPassword()+"'";
		String fname = "'"+user.getFirstName()+"'";
		String lname = "'"+user.getLastName()+"'";
		
		try {
			connect();
			Statement st = con.createStatement();
			int res = -1;
			res = st.executeUpdate("INSERT INTO ANSATT VALUES ( "
					+ username + ", " + pw + ", " + fname + ", "+ lname + ", NULL, 0)" );
			if (res != -1)
				System.out.println("wee");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
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
	
//	public static void main(String[] args) {
//		Person person = new Person("testEn", "pappa", "mag", "test");
//		Save(person);
//				
//	}
}


