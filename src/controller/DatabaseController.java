package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Application;
import application.ApplicationComponent;

import model.*;

public class DatabaseController extends ApplicationComponent {

	private Connection con;
	private Map<String,NotificationType> string2enum;
	private Map<Integer,ReplyStatus> int2enum;

	public DatabaseController(Application app) {
		super(app);
		buildString2enum();
		buildInt2enum();
	}
	

	private void buildInt2enum() {
		int2enum = new HashMap<Integer, ReplyStatus>();
		int2enum.put(-1, ReplyStatus.UBESVART);
		int2enum.put(0, ReplyStatus.NEI);
		int2enum.put(1,ReplyStatus.JA);
	}


	private void buildString2enum() {
		string2enum = new HashMap<String,NotificationType>();
		string2enum.put("Nytt", NotificationType.NYTT);
		string2enum.put("Oppdatert", NotificationType.OPPDATERT);
		string2enum.put("Slettet", NotificationType.SLETTET);
	}


	private void connect() {
		con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://mysql.stud.ntnu.no/magnurod_avtalekalender";
			String user = "magnurod_fellesp";
			String pw = "stabak";
			con = DriverManager.getConnection(url, user, pw);

		} catch (SQLException ex) {
			System.out.println("Tilkobling feilet: " + ex.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Feilet under driverlasting: " + e.getMessage());
			System.out.println(e);
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

	public boolean Save(Person user) {
		String username = incapsulate(user.getUsername());
		String pw = incapsulate(user.getPassword());
		String fname = incapsulate(user.getFirstName());
		String lname = incapsulate(user.getLastName());
		String email = incapsulate(user.getEmail());
		String sql = "INSERT INTO ANSATT VALUES ( " + username + ", " + pw
				+ ", " + fname + ", " + lname + ", " + email + ", 0)";
		int rowsAffected=-1;
		try {
			connect();
			Statement st = con.createStatement();
			rowsAffected = st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		disconnect();
		if(rowsAffected >= 1)
			return true;
		return false;
	}

	public boolean newAppointment(Appointment appointment) {
		String title = incapsulate(appointment.getTitle());
		java.util.Date date = appointment.getDate();
		java.sql.Date sqlDate = new java.sql.Date(date.getYear(),
				date.getMonth(), date.getDate());
		String owner = incapsulate(this.getApplication()
				.getCurrentlyLoggedInUser().getUsername());
		String place = incapsulate(appointment.getPlace());
		Time startTime = appointment.getStartTime();
		java.sql.Time sqlStartTime = new java.sql.Time(startTime.returnHours(),
				startTime.returnMinutes(), 00);
		Time duration = appointment.getAppLength();
		java.sql.Time sqlDuration = new java.sql.Time(duration.returnHours(),
				duration.returnMinutes(), 00);
		String desc = incapsulate(appointment.getDescription());
		String sql = "INSERT INTO AVTALE (Tittel, Dato, AvtaleEier, TYPE, "
				+ "Sted, Starttid, Varighet, Beskrivelse, ErAktiv )"
				+ "VALUES ( " + title + ", '" + sqlDate + "', " + owner + ", "
				+ "'Avtale', " + place + ", '" + sqlStartTime + "', '"
				+ sqlDuration + "', " + desc + ", 1)";
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

	public boolean authenticated(User user) {
		String username = user.getUsername();
		String pw = user.getPassword();
		String sql = "SELECT BrukerNavn, Passord FROM ANSATT WHERE BrukerNavn='"
				+ username + "'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String dbuser = "";
			String dbpw = "";
			while (rs.next()) {
				dbuser = rs.getString(1);
				dbpw = rs.getString(2);
			}
			dbuser = dbuser.toLowerCase();
			if (dbuser.equals(username) && dbpw.equals(pw)) {
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
		List<Appointment> appointments = new ArrayList<Appointment>();
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='" + username
				+ "' " + "AND ErAktiv=1 AND TYPE='Avtale'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Appointment a = new Appointment(rs.getInt(1), rs.getDate(3),
						rs.getTime(8), rs.getTime(9), rs.getString(2),
						rs.getString(10), rs.getString(7));
				appointments.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return appointments;
	}

	public void updateLoginStatus(User user, boolean isOnline) {
		String username = user.getUsername();
		String sql = "UPDATE ANSATT SET IsLoggedOn=" + isOnline
				+ " WHERE BrukerNavn='" + username + "'";
		try {
			connect();
			Statement st = con.createStatement();
			st.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	public boolean getOnlineStatus(String username) {
		boolean isOnline;
		String sql = "SELECT IsLoggedOn FROM ANSATT WHERE BrukerNavn='"
				+ username + "'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
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
	
	public List<String> getOnlineUsersNames() {
		String sql = "SELECT BrukerNavn FROM ANSATT WHERE IsLoggedOn=1";
		List<String> onlineUsers =  new ArrayList<String>();
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				onlineUsers.add(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return onlineUsers;
	}

	public Person retrieveUser(String username) {
		// TODO write
		String sql = "SELECT * FROM ANSATT WHERE BrukerNavn='" + username + "'";
		connect();
		Statement st;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			Person user;
			while (rs.next()) {
				user = new Person(rs.getString(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5));
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

	public List<String> retrieveUsernames() {
		String sql = "SELECT BrukerNavn FROM ANSATT";
		List<String> listOfUsernames = new ArrayList<String>();
		connect();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				listOfUsernames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return listOfUsernames;
	}

	public int[] deleteUser(String username) {
		String sql = "DELETE FROM ANSATT WHERE BrukerNavn='" + username + "'";
		String sql2 = "DELETE FROM PAMINNELSE WHERE SendtTil='" + username
				+ "'";
		int rowseffected[] = { -1, -1 };
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

	public boolean editAppointment(int ID, String kolonne, String updatedTo) {
		int rowsUpdated = -1;
		String sql = "UPDATE AVTALE SET " + kolonne + "="
				+ incapsulate(updatedTo) + " WHERE AvtaleID=" + ID;
		connect();
		try {
			Statement st = con.createStatement();
			rowsUpdated = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		if (rowsUpdated >= 1)
			return true;
		return false;

	}

	public boolean tryDeleteAppointment(int ID) {
		String sql = "UPDATE AVTALE SET ErAktiv=0 WHERE AvtaleID=" + ID +" AND AvtaleEier='"+
		this.getApplication().getCurrentlyLoggedInUser().getUsername()+"'";
		int res = -1;
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

	public List<Room> retrieveAllRooms() {
		String sql = "SELECT * FROM ROM";
		List<Room> allRooms = new ArrayList<Room>();
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet roomRowIterator = st.executeQuery(sql);

			while (roomRowIterator.next()) {
				Room a = new Room(roomRowIterator.getString(1),
						roomRowIterator.getInt(3), roomRowIterator.getString(2));
				allRooms.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return allRooms;
	}

	public boolean isRoomAvailable(String roomID, Date date, Time starttid,
			Time varighet) {
		boolean isAvailable = true;
		String sql = "SELECT dato, starttid, varighet FROM AVTALE WHERE avtalerom='"
				+ roomID + "'";
		date.setYear(date.getYear() - 1900);
		date.setMonth(date.getMonth() - 1);
		Date DBdate = null;
		Time DBstart = null;
		Time DBslutt = null;
		Time sluttid = null;
		boolean eq = false;
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				DBdate = rs.getDate(1);
				DBstart = new Time(rs.getTime(2).getHours(), rs.getTime(2)
						.getMinutes());
				DBslutt = new Time(rs.getTime(2).getHours()
						+ rs.getTime(3).getHours(), rs.getTime(2).getMinutes()
						+ rs.getTime(3).getMinutes());
				sluttid = new Time(starttid.returnHours()
						+ varighet.returnHours(), starttid.returnMinutes()
						+ varighet.returnMinutes());
				eq = starttid.between(DBstart, DBslutt);
				if (date.equals(DBdate)) {
					if (starttid.between(DBstart, DBslutt)
							|| sluttid.between(DBstart, DBslutt)) {
						isAvailable = false;
					} else if (DBstart.between(starttid, sluttid)
							|| DBslutt.between(starttid, sluttid)) {
						isAvailable = false;
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return isAvailable;
	}

	public List<Meeting> retrieveMeetings(User user) {

		String username = user.getUsername();
		List<Meeting> listOfMeetings = new ArrayList<Meeting>();
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='" + username
				+ "' " + "AND ErAktiv=1 AND TYPE='Møte'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				int ID = rs.getInt(1);
				String roomName = rs.getString(6);
				Date date = rs.getDate(3);
				java.sql.Time sTime = rs.getTime(8);
				java.sql.Time dur = rs.getTime(9);
				String title = rs.getString(2);
				String desc = rs.getString(10);
				String place = rs.getString(7);

				String sql2 = "SELECT SendtTil FROM PAMINNELSE WHERE AvtaleID="
						+ ID;
				Statement st2 = con.createStatement();
				ResultSet rs2 = st2.executeQuery(sql2);
				ArrayList<String> listOfParticipants = new ArrayList<String>();
				while (rs2.next()) {
					listOfParticipants.add(rs2.getString(1));
				}

				Room room = null;
				String sql3 = "SELECT * FROM ROM WHERE RomID='" + roomName
						+ "'";
				Statement st3 = con.createStatement();
				ResultSet rs3 = st3.executeQuery(sql3);
				while (rs3.next()) {
					room = new Room(rs3.getString(1), rs3.getInt(3),
							rs3.getString(2));
				}
				// System.out.println(ID+"+");
				Meeting m = new Meeting(ID, date, sTime, dur, title, desc,
						place, listOfParticipants, room);
				// System.out.println(m.getID()+"-");
				listOfMeetings.add(m);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return listOfMeetings;
	}

	public boolean summonToMeeting(String username, int appointmentID) {
		String sql = "INSERT INTO PAMINNELSE VALUES ('" + username + "', " + ""
				+ appointmentID + ", 'NULL', -1, 'Nytt')";
		int rowsAffected = -1;
		connect();
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


	public List<Notification> retrieveNotifications(User user) {
		String sql = "SELECT * FROM PAMINNELSE WHERE SendtTil = '"
				+ user.getUsername() + "' AND SkalDelta=-1";
		connect();
		List<Notification> notifications = new ArrayList<Notification>();
		Appointment appointment =  null;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String sql2 = "SELECT * FROM AVTALE WHERE AvtaleID="
						+ rs.getInt(2);
				Statement st2 = con.createStatement();
				ResultSet rs2 = st2.executeQuery(sql2);

				while (rs2.next()) {
					appointment = new Appointment(rs2.getInt(1),
							rs2.getDate(3), rs2.getTime(8), rs2.getTime(9),
							rs2.getString(2), rs2.getString(10),
							rs2.getString(7));
				}
				String s = rs.getString(5);
				int i = rs.getInt(4);
				

				 Notification n = new Notification(rs.getString(3),
						string2enum.get(s), int2enum.get(i), appointment, user);
				 notifications.add(n);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return notifications;
	}

	
	public List<Appointment> retrieveMeetingsAndAppointments(User user) {

		String username = user.getUsername();
		List<Appointment> listOfMeetingsAndAppointments = new ArrayList<Appointment>();
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='"+username+"' " +
				"AND ErAktiv=1";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				int ID = rs.getInt(1);
				String roomName = rs.getString(6);
				Date date = rs.getDate(3);
				java.sql.Time sTime = rs.getTime(8);
				java.sql.Time dur = rs.getTime(9);
				String title = rs.getString(2);
				String desc = rs.getString(10);
				String place = rs.getString(7);
				
				if (rs.getString(5).equals("Avtale")) {
					Appointment a = new Appointment(ID, date, sTime, dur,
							title, desc, place);
					listOfMeetingsAndAppointments.add(a);

				} else if (rs.getString(5).equals("Møte")) {
					String sql2 = "SELECT SendtTil FROM PAMINNELSE WHERE AvtaleID="
							+ ID;
					Statement st2 = con.createStatement();
					ResultSet rs2 = st2.executeQuery(sql2);
					ArrayList<String> listOfParticipants = new ArrayList<String>();
					while (rs2.next()) {
						listOfParticipants.add(rs2.getString(1));
					}

					Room room = null;
					String sql3 = "SELECT * FROM ROM WHERE RomID='" + roomName
							+ "'";
					Statement st3 = con.createStatement();
					ResultSet rs3 = st3.executeQuery(sql3);
					while (rs3.next()) {
						room = new Room(rs3.getString(1), rs3.getInt(3),
								rs3.getString(2));
					}
					Meeting m = new Meeting(ID, date, sTime, dur, title, desc,
							place, listOfParticipants, room);
					listOfMeetingsAndAppointments.add(m);
				}

			}
			String sql4 = "SELECT AvtaleID FROM PAMINNELSE WHERE SendtTil='"+
				this.getApplication().getCurrentlyLoggedInUser().getUsername()+
				"' AND SkalDelta=1";
			Statement st4 = con.createStatement();
			ResultSet rs4 = st4.executeQuery(sql4);
			while(rs4.next()){
				String sql5 = "SELECT * FROM AVTALE WHERE AvtaleID ="+rs4.getInt(1);
				Statement st5 = con.createStatement();
				ResultSet rs5 = st5.executeQuery(sql5);
				while(rs5.next()){
					Appointment a = new Appointment(rs5.getInt(1),
							rs5.getDate(3), rs5.getTime(8), rs5.getTime(9),
							rs5.getString(2), rs5.getString(10),
							rs5.getString(7));
					listOfMeetingsAndAppointments.add(a);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return listOfMeetingsAndAppointments;
	}
	public boolean unsummonToMeeting(String username, int appointmentID){
		String sql = "DELETE FROM PAMINNELSE WHERE AvtaleID='"+appointmentID+"' AND SendtTil='"+username+"'";
		int rowsAffected = -1;
		connect();
		try {
			Statement st = con.createStatement();
			rowsAffected = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		if (rowsAffected >=1)
			return true;
		return false;
	}
	
	public void replyToSummon(int ID, int yesORno, String reason){
		String sql = "UPDATE PAMINNELSE SET BegrunnetSvar='"+reason+"', SkalDelta="+yesORno+
			" WHERE AvtaleID="+ID+" AND SendtTil='"
			+this.getApplication().getCurrentlyLoggedInUser().getUsername()+"'";
		connect();
		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}
	
	public List<Notification> retrieveReplys(){
		String sql = "SELECT * FROM AVTALE WHERE AvtaleEier='"
			+this.getApplication().getCurrentlyLoggedInUser().getUsername()+
			"' AND ErAktiv=1 AND TYPE='Møte'";
		connect();
		List<Notification> notifications = new ArrayList<Notification>();
		try {
			Statement st =con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				Appointment a = new Appointment(rs.getInt(1),
						rs.getDate(3), rs.getTime(8), rs.getTime(9),
						rs.getString(2), rs.getString(10),
						rs.getString(7));
				String sql2 = "SELECT * FROM PAMINNELSE WHERE AvtaleID="
						+rs.getInt(1);
				Statement st2 = con.createStatement();
				ResultSet rs2 = st2.executeQuery(sql2);
				while(rs2.next()){
					String sql3 = "SELECT * FROM ANSATT WHERE BrukerNavn='"+rs2.getString(1)+"'";
					Statement st3 = con.createStatement();
					ResultSet rs3 = st3.executeQuery(sql3);
					Person user = null;
					while (rs3.next()){
						user = new Person(rs3.getString(1), rs3.getString(2),
								rs3.getString(3), rs3.getString(4), rs3.getString(5));	
					}
					Notification n = new Notification(rs2.getString(3),
							string2enum.get(rs2.getString(5)), int2enum.get(rs2.getInt(4)),
							a, user);
					notifications.add(n);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		return notifications;
		
	}
	
	public void updateSummon(int avtaleID, String updatedORdeleted){
		String sql = "";
		if(updatedORdeleted.equals("updated"))
			sql = "UPDATE PAMINNELSE SET SkalDelta=-1, TYPE='Oppdatert', " +
					"BegrunnetSvar='NULL' WHERE AvtaleID="+avtaleID;
		else if(updatedORdeleted.equals("deleted"))
			sql = "UPDATE PAMINNELSE SET SkalDelta=-1, TYPE='Slettet', " +
					"BegrunnetSvar='NULL' WHERE AvtaleID="+avtaleID;
		connect();
		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	public int retrieveNumOfParticipants(int avtaleID){
		int output = -1;
		String sql = "SELECT COUNT(*) FROM PAMINNELSE WHERE AvtaleID='"+avtaleID+"'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				output = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		disconnect();
		return output;
	}

	private String incapsulate(String input) {
		return "'" + input + "'";
	}
	
	public void reserveRoomWithID(int avtaleID, String roomID){
		String sql = "UPDATE AVTALE SET AvtaleRom='"+roomID+"' WHERE AvtaleID='"+avtaleID+"'";
		try {
			connect();
			Statement st = con.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public Appointment retrieveAnAppointment(int avtaleID){
		Appointment output = null;
		String sql = "SELECT * FROM AVTALE WHERE AvtaleID = '"+avtaleID+"'";
		try {
			connect();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				output = new Appointment(avtaleID, rs.getDate(3), rs.getTime(8), rs.getTime(9), rs.getString(2), rs.getString(10), rs.getString(7));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}

	
}
