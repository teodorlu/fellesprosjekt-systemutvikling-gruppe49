package application;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import model.*;
import view.*;
import controller.*;

public class Application {
	
	private Person currentUser;
	private List<Person> otherCalendarOwners;
	
	private ConsoleController consoleController;
	private DatabaseController databaseController;
	
	private ConsoleView consoleView;
	
	private Application(){
		// Assign settings here.
		InputStream input = System.in;
		PrintStream output = (PrintStream) System.out;
		
		consoleController = new ConsoleController(input);
		consoleView = new ConsoleView(output);
		
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
