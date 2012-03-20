package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Person;
import model.User;

public class DatabaseController {
	
	private Connection con;
	
	public DatabaseController(){
		
	}
	
	private void connect() {
		con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://mysql.stud.ntnu.no/magnurod_avtalekalender";
			String user = "magnurod_fellesp";
			String pw = "stabak";
			con = DriverManager.getConnection(url, user, pw);
			
//			if (con != null)
//				System.out.println("nice");

		} catch (SQLException ex) {
			System.out.println("Tilkobling feilet: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out
					.println("Feilet under driverlasting: " + ex.getMessage());
			System.out.println(ex);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String username = "'"+user.getUsername()+"'";
		String pw = "'"+user.getPassword()+"'";
		String fname = "'"+user.getFirstName()+"'";
		String lname = "'"+user.getLastName()+"'";
		String sql = "INSERT INTO ANSATT VALUES ( "
					+ username + ", " + pw + ", " + fname + ", "+ lname + ", NULL, 0)";
		try {
			connect();
			Statement st = con.createStatement();
			int res = st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}
	
	public boolean authenticated(User user){
		String username = user.getUsername();
		String pw = user.getPassword();
		String sql = "SELECT BrukerNavn, Passord FROM ANSATT WHERE BrukerNavn='"+username+"'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String dbuser = "";
			String dbpw = "";
			while(rs.next()){
				dbuser=rs.getString(1);
				dbpw=rs.getString(2);
			}
			dbuser = dbuser.toLowerCase();
			if (dbuser.equals(username) && dbpw.equals(pw)){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return false;
	}

	public Person retrieve(User user) {
		// TODO generate Person object from database corresponding to user.username
		// This includes the Person's appointments: person.personalCalendar.appointments
		String username = user.getUsername();
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='"+username+"'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				//TODO hente ut fra rs og legge inn i et avtaleobjekt
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return null;
	}
	
	public void updateLoginStatus(User user, boolean isOnline){
		String username = user.getUsername();
		String sql = "UPDATE ANSATT SET IsLoggedOn="+isOnline+" WHERE BrukerNavn='"+username+"'";
		try {
			connect();
			Statement st = con.createStatement();
			st.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}
	
	public boolean getOnlineStatus(String username){
		boolean isOnline;
		String sql = "SELECT IsLoggedOn FROM ANSATT WHERE BrukerNavn='"+username+"'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				isOnline = rs.getBoolean(1);
				disconnect();
				return isOnline;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return false;
	}
	
	public static Boolean deleteID(String ID){
		return true;
		//må fikse at den sjekker ID opp mot database
	}
	
}


