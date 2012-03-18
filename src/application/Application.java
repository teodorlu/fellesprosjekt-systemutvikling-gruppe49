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
	
	private Person currentUser;
	private Set<Person> otherCalendarOwners;
	
	private ConsoleController consoleController;
	private DatabaseController databaseController;
	
	private ConsoleView consoleView;
	
	
	
	public Person getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
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
		
		run();
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
	
	public static void main(String[] args) {
		new Application();
	}
	
}
