package view;

import java.io.PrintStream;
import java.util.Set;

public class ConsoleView {

	private PrintStream output;
	
	public ConsoleView(PrintStream output) {
		this.output = output;
	}
	
	public void print(String message){
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
