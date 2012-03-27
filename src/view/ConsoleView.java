package view;

import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import model.Appointment;
import model.Notification;
import model.Person;
import model.Time;
import model.User;

import application.Application;
import application.ApplicationComponent;

public class ConsoleView extends ApplicationComponent{

	private PrintStream output;
	
	public ConsoleView(Application app, PrintStream output) {
		super(app);
		this.output = output;
	}
	
	public void println(String message){
		output.println(message);
	}
	
	public void welcome(){
		output.println("  Calendar Console, version 0.1");
		output.println("---------------------------------");
	}

	public void showCommands(Iterable<String> commands) {
		output.println("+---------------------------+");
		output.println("|  Available commands are:  |");
		output.println("+---------------------------+");
		
		for (String command : commands){
			String line = String.format("| %-25s |", command);
			output.println(line);
		}
		
		output.println("+---------------------------+");
	}

	public void showSucessfulLoginMessage(Person owner) {
		output.println("Sucessfully logged in as " + owner.getUsername());
	}

	public void showFailedLoginMessage(User user) {
		output.println("Login as " + user.getUsername() + " failed");
	}

	public void showNotLoggedIn() {
		output.println("You are not logged in!");
	}
	
	public void showRegError(String errorType){
		if(errorType.equals("noInput")) 
			output.println("Du må ha med register -u <username> -p <password> -fn <firstName> -ln <lastName> -email <email>");
		else
			output.println("Feil input. Skriv inn riktig " + errorType);
	}

	public void showAppontmentInputError() {
		output.println("Du må ha med -title <title>, -date date, -s <starttid>, -d <varighet>, med dato på formen YYYY-MM-DD og tid på formen HH:MM.");
	}

	public void showAppontmentDetails(String title, String date,
			String startTime, String appLength, String desc, String place) {
		output.println("Tittel: "+title+" Date: "+ date +" Start: "+ startTime +" Duration: "+ appLength +" Desc: "+ desc+" Place: "+ place);
	}

	public void showApplicationDeleted() {
		output.println("The appointment has been deleted");
	}

	public void showApplicationDoesNotExistError() {
		output.println("This is not a valid appointment ID");
		output.println("Valid appontments: ");
		List<Appointment> appointments = getApplication().getDatabaseController().retrieveAppointments(this.getApplication().getCurrentlyLoggedInUser());
		for(int i = 0; i < appointments.size();i++){
			Appointment a = appointments.get(i);
			output.println("ID: "+a.getID()+" Tittle: "+a.getTitle());
		}
	}

	public void showUser(String username) {
		Person a;
		a = getApplication().getDatabaseController().retriveUser(username);
		output.println("Brukernavn: "+a.getUsername());
		output.println("Fornavn: "+a.getFirstName());
		output.println("Etternavn: "+a.getLastName());
		output.println("E-mail: "+a.getEmail());
	}

	public void showAllUsers() {
		List<String> usernames = getApplication().getDatabaseController().retriveUsernames();
		for(int i = 0; i < usernames.size(); i++){
			String name = usernames.get(i);
			System.out.println(name);
		}
	}

	public void showAppointments(List<Appointment> appointments) {
		for(int i = 0; i < appointments.size(); i++){
			Appointment a = appointments.get(i);
			output.println("ID: "+a.getID()+" Tittel: "+a.getTitle());   
		}
	}

	public void showAppointmentTitleChange(String title) {
		output.println("Tittel ble endret til "+ title);
	}

	public void showAppointmentDateChange(Date date) {
		output.println("Dato ble endret til: "+ date);
	}

	public void showAppointmentStartTimeChange(Time startTime) {
		output.println("StartTid ble endret til: "+ startTime);
	}

	public void showAppointmentLengthChange(Time appLength) {
		output.println("Varighet ble endret til: "+ appLength);
	}

	public void showAppointmentDescriptionChange(String description) {
		output.println("Beskrivelse ble endret til: "+ description);
	}

	public void showAppointmentPlaceChange(String place) {
		output.println("Sted ble endret til: "+ place);
	}

	public void showLoginOptions() {
		output.println("For å logge på, skriv login -u <username> -p <password>");
	}

	public void showNotifications(List<Notification> notificationList) {
		output.println("Antall meldinger: " + notificationList.size());
		for (int i = 0; i < notificationList.size(); i++) {
			Notification n = notificationList.get(i);
			output.println("Melding nr " + i+1 + ": " + n.getSender().getTitle() + ". MøteID: " + n.getSender().getID());
		}
	}

	public void showNotificationInvalidReplyError() {
		output.println("Ikke et gyldig svar. Gyldige svar er y/n");
	}

	public void showNotificationNoReplyError() {
		output.println("Ugyldig input. For å se og svare på meldinger, skriv notification -reply <velg melding. 0-indeksert> -y/n");
	}
	
	public void showAppointmentAddedPerson(String name){
		output.println("Brukeren "+name+" er lagt til i Møte!");
	}
	
	public void showAppointmentAlreadyContains(String name){
		output.println( name+" er allerede lagt til!");
	}
	
	public void showUserDoesNotExist(String name){
		output.println(name+" finnes ikke i lista over brukere!");
	}
	
	public void showNoMeetings(){
		output.println("Du har ingen Møter");
	}
	
	
}
