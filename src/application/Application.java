package application;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import model.*;
import view.*;
import controller.*;
public class Application {

	private Person currentlyLoggedInUser = null;
	private Set<Person> otherCalendarOwners;

	private ConsoleController consoleController;
	private DatabaseController databaseController;

	private ConsoleView consoleView;

	private boolean loggedIn;


	public Person getCurrentlyLoggedInUser() {
		return currentlyLoggedInUser;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentlyLoggedInUser = currentUser;
	}

	public Set<Person> getOtherCalendarOwners() {
		return new HashSet<Person>(otherCalendarOwners);
	}

	public void addOtherCalendarOwners(Collection<Person> others){
		otherCalendarOwners.addAll(others);
	}

	public void removeOtherCalendarOwners(Collection<Person> others){
		otherCalendarOwners.removeAll(others);
	}

	public ConsoleController getConsoleController() {
		return consoleController;
	}

	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public ConsoleView getConsoleView() {
		return consoleView;
	}

	private Application(){
		// Assign settings here.
		InputStream input = System.in;
		PrintStream output = (PrintStream) System.out;

		consoleController = new ConsoleController(this, input);
		consoleView = new ConsoleView(this, output);
		databaseController = new DatabaseController(this);

		run();
	}
	
//	TESTER
	public Application(String arg, String command){
		
		String input = arg;
		PrintStream output = (PrintStream) System.out;
		
		databaseController = new DatabaseController(this);
		consoleView = new ConsoleView(this, output);
		consoleController = new ConsoleController(this, input, command);
	}

	private void run(){
		consoleView.welcome();
		while(true){
			boolean executed = consoleController.parseNext();
			if (!executed){
				Set<String> commands = consoleController.getCommands();
				consoleView.showCommands(commands);
			}
		}
	}

	public void tryLogIn(User user) {	
		boolean isValid = getDatabaseController().authenticated(user);

		if (isValid) {
			Person currentlyLoggedInPerson = getDatabaseController().retrieveUser(user.getUsername());    //Dzenan enda denne, se på denne om alt går mongoklikk!!!
			this.setCurrentUser(currentlyLoggedInPerson);
			getDatabaseController().updateLoginStatus(currentlyLoggedInUser, true);
			loggedIn = true;
			this.getConsoleView().showSucessfulLoginMessage(currentlyLoggedInPerson);
		}
		else {
			this.getConsoleView().showFailedLoginMessage(user);
			loggedIn = false;
		}
	}

	public boolean getLoggedIn(){
		return this.loggedIn;
	}
	public void logout() {
		loggedIn = false;
		this.getConsoleView().showCompletedLogout();
	}

	public static void main(String[] args) {
		new Application();
	}



}
