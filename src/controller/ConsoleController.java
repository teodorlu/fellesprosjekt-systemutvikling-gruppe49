package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleController {
	
	private final InputStream input;
	private final BufferedReader reader;
	private final Map<String, Command> commands;
	
	public ConsoleController(InputStream input){
		this.input = input;
		InputStreamReader isr = new InputStreamReader(input);
		reader = new BufferedReader(isr);
		commands = new HashMap<String, Command>();
		registerCommands();
	}
	
	public void parseNext(){
		try {
			String[] command = reader.readLine().split(" ");
			String keyword = command[0];
			commands.get(keyword).execute(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private void registerCommands() {
		Object register = new Object(){
			void execute(String[] parameters){
				
			}};
		commands.put("register", (Command) register);
	}

}

interface Command {
	void execute(String[] parameters);
}