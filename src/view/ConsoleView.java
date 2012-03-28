package view;

import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.Appointment;
import model.Meeting;
import model.Notification;
import model.Person;
import model.Time;
import model.User;

import application.Application;
import application.ApplicationComponent;

public class ConsoleView extends ApplicationComponent {

	private PrintStream output;
	private String testOutput;
	private String outString;

	public ConsoleView(Application app, PrintStream output) {
		super(app);
		this.output = output;
	}

	public String getOutString() {
		return this.outString;
	}

	// For testing
	public ConsoleView(Application app, String testOutput) {
		super(app);
		this.testOutput = testOutput;
	}

	// End for testing

	public void println(String message) {
		output.println(message);
	}

	public void welcome() {
		output.println("  Calendar Console, alpha 1.3.49");
		output.println("-----------------------------");
	}

	// public void showCommands(Iterable<String> commands) {
	// output.println("+---------------------------+");
	// output.println("|  Available commands are:  |");
	// output.println("+---------------------------+");
	//
	// for (String command : commands){
	// String line = String.format("| %-25s |", command);
	// output.println(line);
	// }
	//
	// output.println("+---------------------------+");
	// }

	public void showCommands(Iterable<String> commands) {
		LinkedList<Object> objects = new LinkedList<Object>();
		for (String command : commands)
			objects.add(command);
		showTitledList("Commands", objects);
	}

	public void showTitledList(String title, Iterable<Object> contents) {
		int longestStringLength = 0;
		for (Object o : contents) {
			String s = o.toString();
			if (s.length() > longestStringLength)
				longestStringLength = s.length();
		}

		showTitledList(title, contents, longestStringLength);
	}

	public void showTitledList(String title, Iterable<Object> contents,
			int textWidth) {
		output.println(separator(textWidth));

		// Header
		String titleFormat = "| " + "%-" + textWidth + "s" + " |";
		output.println(String.format(titleFormat, title));

		output.println(separator(textWidth));

		// Content
		for (Object o : contents) {
			String format = "| %-" + textWidth + "s |";
			String line = String.format(format, o.toString());
			output.println(line);
		}

		output.println(separator(textWidth));
	}

	private String separator(int textWidth) {
		StringBuilder ret = new StringBuilder();
		ret.append("+-");
		ret.append(repeated("-", textWidth));
		ret.append("-+");
		return ret.toString();
	}

	private String repeated(String src, int times) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) {
			sb.append(src);
		}
		return sb.toString();
	}

	public LinkedList<Object> toIterableOfObject(Iterable<String> contents) {
		LinkedList<Object> ls = new LinkedList<Object>();
		for (String s : contents)
			ls.add((Object) s);
		return ls;
	}

	public void showSucessfulLoginMessage(Person owner) {
		outString = "Sucessfully logged in as " + owner.getUsername();
		output.println(outString);
	}

	public void showFailedLoginMessage(User user) {
		outString = "Login as " + user.getUsername() + " failed";
		output.println(outString);
	}

	public void showNotLoggedIn() {
		outString = "You are not logged in!";
		output.println(outString);
	}

	public void showRegError(String errorType) {
		if (errorType.equals("noInput")) {
			outString = "Du må ha med register -u <username> -p <password> -fn <firstName> -ln <lastName> -email <email>";
			output.println(outString);
		} else {
			outString = "Feil input. Skriv inn riktig " + errorType;
			output.println(outString);
		}
	}

	public void showAppontmentInputError() {
		outString = "Du må ha med -title <title>, -date date, -s <starttid>, -d <varighet>, med dato på formen YYYY-MM-DD og tid på formen HH:MM.";
		output.println(outString);
	}

	public void showAppontmentDetails(String title, String date,
			String startTime, String appLength, String desc, String place) {
		outString = "Tittel: " + title + " Date: " + date + " Start: "
				+ startTime + " Duration: " + appLength + " Desc: " + desc
				+ " Place: " + place;
		output.println(outString);
	}

	public void showApplicationDeleted() {
		outString = "The appointment has been deleted";
		output.println(outString);
	}

	public void showApplicationDoesNotExistError() {
		outString = "This is not a valid appointment ID";
		output.println(outString);
		output.println("Valid appontments: ");
		List<Appointment> appointments = getApplication()
				.getDatabaseController().retrieveMeetingsAndAppointments(
						this.getApplication().getCurrentlyLoggedInUser());
		for (int i = 0; i < appointments.size(); i++) {
			Appointment a = appointments.get(i);
			output.println("ID: " + a.getID() + " Tittle: " + a.getTitle());
		}
	}

	public void showUser(String username) {
		Person a;
		a = getApplication().getDatabaseController().retrieveUser(username);
		output.println("Brukernavn: " + a.getUsername());
		output.println("Fornavn: " + a.getFirstName());
		output.println("Etternavn: " + a.getLastName());
		output.println("E-mail: " + a.getEmail());
	}

	public void showAllUsers() {
		outString = "Users: ";
		output.println(outString);
		List<String> usernames = getApplication().getDatabaseController()
				.retrieveUsernames();
		for (int i = 0; i < usernames.size(); i++) {
			String outString = usernames.get(i);
			output.println(outString);
		}
	}

	public void showAppointments(List<Appointment> appointments) {
		outString = "Appointments: ";
		output.println(outString);
		for (int i = 0; i < appointments.size(); i++) {
			Appointment a = appointments.get(i);
			output.println("ID: " + a.getID() + " Tittel: " + a.getTitle());
		}
	}

	public void showAppointmentTitleChange(String title) {
		outString = "Tittel ble endret til " + title;
		output.println(outString);
	}

	public void showAppointmentDateChange(Date date) {
		outString = "Dato ble endret til: " + date;
		output.println(outString);
	}

	public void showAppointmentStartTimeChange(Time startTime) {
		outString = "StartTid ble endret til: " + startTime;
		output.println(outString);
	}

	public void showAppointmentLengthChange(Time appLength) {
		outString = "Varighet ble endret til: " + appLength;
		output.println(outString);
	}

	public void showAppointmentDescriptionChange(String description) {
		outString = "Beskrivelse ble endret til: " + description;
		output.println(outString);
	}

	public void showAppointmentPlaceChange(String place) {
		outString = "Sted ble endret til: " + place;
		output.println(outString);
	}

	public void showLoginOptions() {
		outString = "For å logge på, skriv login -u <username> -p <password>";
		output.println(outString);
	}

	public void showNotifications(List<Notification> notificationList) {
		outString = "Antall meldinger: " + notificationList.size();
		output.println(outString);
		for (int i = 0; i < notificationList.size(); i++) {
			Notification n = notificationList.get(i);
			output.println("Melding nr " + i + 1 + ": "
					+ n.getSender().getTitle() + ". MøteID: "
					+ n.getSender().getID());
		}
	}

	public void showNotificationsReplies(List<Notification> notificationList) {
		for (Notification n : notificationList) {
			output.println("MøteID: " + n.getSender().getID() + ":: "
					+ n.getReceivers().getUsername() + " har svart "
					+ n.getReply());
		}
	}

	public void showNotificationInvalidReplyError() {
		outString = "Ikke et gyldig svar. Gyldige svar er y/n";
		output.println(outString);
	}

	public void showNotificationNoReplyError() {
		outString = "Ugyldig input. For å se og svare på meldinger, skriv notification -reply <velg melding. 0-indeksert> -y/n";
		output.println(outString);
	}

	public void showAppointmentAddedPerson(String name) {
		outString = "Brukeren " + name + " er lagt til i Møte!";
		output.println(outString);
	}

	public void showAppointmentAlreadyContains(String name) {
		outString = name + " er allerede lagt til!";
		output.println(outString);
	}

	public void showUserDoesNotExist(String name) {
		outString = name + " finnes ikke i lista over brukere!";
		output.println(outString);
	}

	public void showNoMeetings() {
		outString = "Du har ingen Møter";
		output.println(outString);
	}

	public void showEditInputError() {
		outString = "For å endre, skriv edit [-title <tittel> -date <date> -s <StartTime> -d <Duration> -desc <description> -place <Place> ]";
		output.println(outString);
	}

	public void showNoReplyArgumentsError() {
		outString = "For å svare, skriv reply -id <møte id> <y/n> -reason <grunn for svar>";
		output.println(outString);
	}

	public void showAppointmentDoesNotContain(String name) {
		outString = name + " er ikke medlem av møte!";
		output.println(outString);
	}

	public void showAppointmentRemovedPerson(String name) {
		outString = name + " er fjernet fra møte";
		output.println(outString);
	}

	public void showAppointmentChangeToApp() {
		outString = "Ingen fler deltagere, møte forandret til avtale";
		output.println(outString);
	}

	public void showCompletedLogout() {
		outString = "Du har blitt logget ut.";
		output.println(outString);
	}

	public void showOnlineUsers() {
		List<String> usernames = getApplication().getDatabaseController()
				.getOnlineUsersNames();
		for (int i = 0; i < usernames.size(); i++) {
			String name = usernames.get(i);
			output.println(name);
		}
	}

	public void showRegSuccess() {
		outString = "Brukeren er blitt lagt til.";
		output.println(outString);
	}

	public void showSummonHelp() {
		outString = "summon <id> <username1> [ <username2> <username3> ... ]";
		output.println(outString);
	}

	public void showIllegalIntSummon() {
		output.println("Dette er ikke en gyldig ID!");
	}

	public void showAllMeetingsAndApps(List<Appointment> allAppointmentsAndMeetings) {
		for (Appointment a : allAppointmentsAndMeetings) {
			String type = "Avtale";
			if(a instanceof Meeting){
				type = "Møte";
				Meeting m = (Meeting) a;
				output.println("ID:" + m.getID() + ", Tittel: " + m.getTitle()
						+ ", Dato: " + m.getDate() + ", Start: " + m.getStartTime()
						+ ", Varighet: " + m.getAppLength() + ", Sted: "
						+ m.getPlace() + ", Beskrivelse: " + m.getDescription()+", Type: "+type+", Deltakere: "+m.getParticipants()+", Rom: "+m.getRoom());
			} else {
				output.println("ID:" + a.getID() + ", Tittel: " + a.getTitle()
						+ ", Dato: " + a.getDate() + ", Start: " + a.getStartTime()
						+ ", Varighet: " + a.getAppLength() + ", Sted: "
						+ a.getPlace() + ", Beskrivelse: " + a.getDescription()+", Type: "+type);
				
			}
		}
	}
	public void showAppointmentRoomDoesNotExist(String RoomID) {
		outString = RoomID + " er ikke et rom.";
		output.println(outString);
	}
	public void showAppointmentRoomReserved(String RoomID) {
		outString = RoomID + " er blitt reservert for avtalen.";
		output.println(outString);
	}
	public void showAppointmentRoomAlreadyReserved(String RoomID) {
		outString = RoomID + " er allerede reservert for dette tidspunktet.";
		output.println(outString);
	}
	public void showAppointmentNoRoomsWCapAvail() {
		outString = "Ingen rom med nok kapasitet er ledige";
		output.println(outString);
	}
	public void showAppointmentNoRoomsWCap() {
		outString = "Ingen rom har nok kapasitet";
		output.println(outString);		
	}
	public void showAppointmentNoSuchApp() {
		outString = "Du har ikke en slik avtale/møte";
		output.println(outString);
	}

}
