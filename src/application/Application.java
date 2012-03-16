package application;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import model.*;
import view.*;
import controller.*;

public class Application {
	
	private Person currentUser;
	private List<Person> otherCalendarOwners;
	
	private ConsoleController consoleController;
	private DatabaseController databaseController;
	
	private ConsoleView consoleView;
	
	public Application(){
		// Assign settings here.
		InputStream input = System.in;
		PrintStream output = (PrintStream) System.out;
		
		consoleController = new ConsoleController(input);
		consoleView = new ConsoleView(output);
		
		
	}
	
	public static void main(String[] args) {
		new Application();
	}
	
}
