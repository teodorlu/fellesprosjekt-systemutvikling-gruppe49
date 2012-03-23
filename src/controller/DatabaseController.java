package controller;

import java.sql.Connection;
import java.sql.Date;
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
import model.Room;
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
	
	public boolean newAppointment(Appointment appointment){
		String title = incapsulate(appointment.getTitle());
		java.util.Date date = appointment.getDate();
		java.sql.Date sqlDate = new java.sql.Date(date.getYear()-1900,date.getMonth()-1,date.getDate());
		//String owner = incapsulate(this.getApplication().getCurrentlyLoggedInUser().getUsername());
		String place = incapsulate(appointment.getPlace());
		Time startTime = appointment.getStartTime();
		java.sql.Time sqlStartTime = new java.sql.Time(startTime.returnHours(),startTime.returnMinutes(),00);
		Time duration = appointment.getAppLength();
		java.sql.Time sqlDuration = new java.sql.Time(duration.returnHours(),duration.returnMinutes(),00);
		String desc = incapsulate(appointment.getDescription());
		String sql = "INSERT INTO AVTALE (Tittel, Dato, AvtaleEier, TYPE, " +
				"Sted, Starttid, Varighet, Beskrivelse, ErAktiv )" +
				"VALUES ( " + title + ", '"+sqlDate+"', 'MAGRODAHL', " +
				"'Avtale', "+ place + ", '"+sqlStartTime+"', '"+sqlDuration+"', " + desc +", 1)";
		connect();
		int rowsAffected = -1;
		try {
			Statement st = con.createStatement();
			rowsAffected = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		if (rowsAffected >= 1)
			return true;
		return false;
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

		String username = user.getUsername();
		List<Appointment> listOfApp = new ArrayList<Appointment>();
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='"+username+"' " +
				"AND ErAktiv=1 AND TYPE='Avtale'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				Appointment a = new Appointment( rs.getInt(1), rs.getDate(3), rs.getTime(8), 
						rs.getTime(9), rs.getString(2), rs.getString(10), rs.getString(6));
				listOfApp.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
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
	
	public List<String> retriveUsernames(){
		String sql = "SELECT BrukerNavn FROM ANSATT";
		List<String> listOfUsernames = new ArrayList<String>();
		connect();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				listOfUsernames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return listOfUsernames;
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
	
	
	public boolean editAppointment(int ID, String kolonne, String updatedTo){
		int rowsUpdated = -1;
		String sql = "UPDATE AVTALE SET "+ kolonne + "=" + incapsulate(updatedTo) + " WHERE AvtaleID="+ID;
		connect();
		try {
			Statement st = con.createStatement();
			rowsUpdated = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		if(rowsUpdated >= 1)
			return true;
		return false;
		
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
	
	public List<Room> retrieveAllRooms(){
		String sql = "SELECT * FROM ROM";
		List<Room> allRooms = new ArrayList<Room>();
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				Room a = new Room(rs.getInt(1), rs.getInt(3), rs.getString(2));
				allRooms.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return allRooms;	
	}
	
	public boolean isRoomAvailable(String roomID, Date date, Time starttid, Time varighet){
		boolean isAvailable = true;
		String sql = "SELECT dato, starttid, varighet FROM AVTALE WHERE avtalerom='"+roomID+"'";
		date.setYear(date.getYear()-1900);
		date.setMonth(date.getMonth()-1);
		Date DBdate = null;
		Time DBstart = null;
		Time DBslutt = null;
		Time sluttid = null;
		boolean eq = false;
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				DBdate = rs.getDate(1);
				DBstart = new Time( rs.getTime(2).getHours(), rs.getTime(2).getMinutes());
				DBslutt = new Time ( rs.getTime(2).getHours() + rs.getTime(3).getHours(), rs.getTime(2).getMinutes() + rs.getTime(3).getMinutes());
				sluttid = new Time(starttid.returnHours()+varighet.returnHours(), starttid.returnMinutes()+varighet.returnMinutes());
				eq = starttid.between(DBstart, DBslutt);
				if(date.equals(DBdate)){
					if( starttid.between(DBstart, DBslutt) || sluttid.between(DBstart, DBslutt)  ){
						isAvailable = false;
					}else if (DBstart.between(starttid, sluttid) || DBslutt.between(starttid, sluttid)){
						isAvailable =false;
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		
		
		return isAvailable ;
	}
	
	
	private String incapsulate(String input){
		return "'" + input + "'";
	}
	
	private String sqlInsert(String tabellnavn, Collection<String> properties){
		// TODO: write
		return null;
	}
	
//	public static void main(String[] args) {
//	DatabaseController dbc = new DatabaseController(null);
//	Date date = new Date(2012,03,23);
//	Time start = new Time(15,15);
//	Time dur = new Time(00,45);
//	Appointment a = new Appointment(date, start, dur, "test6", "description som er fin", "hjemme");
//	if(dbc.newAppointment(a))
//		System.out.println("weeeee");
//	
//	
//	
//	}
}

