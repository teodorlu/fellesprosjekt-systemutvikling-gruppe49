package view;

import java.io.PrintStream;

import application.Application;
import application.ApplicationComponent;

public class ConsoleView extends ApplicationComponent{

	private PrintStream output;
	
	public ConsoleView(Application app, PrintStream output) {
		super(app);
		this.output = output;
	}
	
	public void present(String message){
		output.print(message);
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
			String line = String.format("| %25s |", command);
			output.println(line);
		}
		
		output.println("+---------------------------+");
	}
	
	
}
