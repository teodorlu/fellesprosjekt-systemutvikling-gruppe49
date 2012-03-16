package view;

import java.io.PrintStream;

public class ConsoleView {

	private PrintStream output;
	
	public ConsoleView(PrintStream output) {
		this.output = output;
	}
	
	public void print(String message){
		output.print(message);
	}
	
}
