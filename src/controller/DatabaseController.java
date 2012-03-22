package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import application.Application;
import application.ApplicationComponent;

import model.Appointment;
import model.Person;
import model.User;
import model.Time;

public class DatabaseController extends ApplicationComponent {
	
	private Connection con;
	
	public DatabaseController(Application app){
		super(app);
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
		String username = incapsulate(user.getUsername());
		String pw = incapsulate(user.getPassword());
		String fname = incapsulate(user.getFirstName());
		String lname = incapsulate(user.getLastName());
		String email = incapsulate(user.getEmail());
		String sql = "INSERT INTO ANSATT VALUES ( "
					+ username + ", " + pw + ", " + fname + ", "+ lname + ", "+ email +", 0)";
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

	public List<Appointment> retrieveAppointments(User user) {
		// TODO generate Person object from database corresponding to user.username
		// This includes the Person's appointments: person.personalCalendar.appointments
		String username = user.getUsername();
		List<Appointment> listOfApp = new ArrayList<Appointment>();
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='"+username+"' AND ErAktiv=1";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				Appointment a = new Appointment(rs.getDate(3), rs.getTime(8), 
						rs.getTime(9), rs.getString(2), rs.getString(10), rs.getString(6));
				listOfApp.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		System.out.println(listOfApp);
		return listOfApp;
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
	
	public Person retriveUser(String username){
		// TODO write
		String sql = "SELECT * FROM ANSATT WHERE BrukerNavn='"+username+"'";
		connect();
		Statement st;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			Person user;
			while(rs.next()){
				user = new Person(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
				disconnect();
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return null;
		
	}
	
	public int[] deleteUser(String username){
		String sql = "DELETE FROM ANSATT WHERE BrukerNavn='"+username+"'";
		String sql2 = "DELETE FROM PAMINNELSE WHERE Paminner='"+username+"'";
		int rowseffected[] = {-1,-1};
		connect();
		try {
			Statement st = con.createStatement();
			st.addBatch(sql);
			st.addBatch(sql2);
			rowseffected = st.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return rowseffected;
	}
	
	public boolean tryDeleteAppointment(int ID){
		String sql = "UPDATE AVTALE SET ErAktiv=0 WHERE AvtaleID="+ID;
		int res=-1;
		connect();
		try {
			Statement st = con.createStatement();
			res = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		if (res >= 1)
			return true;
		return false;
	}
	
	
	
	private String incapsulate(String input){
		return "'" + input + "'";
	}
	
	private String sqlInsert(String tabellnavn, Collection<String> properties){
		// TODO: write
		return null;
	}
	
//	public static void main(String[] args) {
//		DatabaseController dbc = new DatabaseController(null);
//		User u = new User("MAGRODAHL", "123");
//		dbc.retrieveAppointments(u);
//
//		
//	
//	}
}



