package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseController {
	
	private void connect() throws InstantiationException, IllegalAccessException{
		
	Connection con = null;
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
	} finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException ex) {
			System.out.println("Epic fail: " + ex.getMessage());
		}
	}
	}
}


