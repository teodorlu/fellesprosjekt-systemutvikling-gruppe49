package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import model.Person;
import model.User;

import application.Application;
import application.ApplicationComponent;

public class ConsoleController extends ApplicationComponent {
	
	private final InputStream input;
	private final BufferedReader reader;
	private final Map<String, Command> commands;
	private final CommandExecuter executer;
	
	public ConsoleController(Application app, InputStream input){
		super(app);
		
		this.input = input;
		InputStreamReader isr = new InputStreamReader(input);
		reader = new BufferedReader(isr);
		commands = new HashMap<String, Command>();
		executer = new CommandExecuter(this.getApplication()); 
		registerCommands();
	}
	
	public boolean parseNext(){
		boolean executedCommand = true;
		
		try {
			String[] command = reader.readLine().split(" ");
			String keyword = command[0];
			
			if (commands.containsKey(keyword))
				commands.get(keyword).execute(command);
			else
				executedCommand = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return executedCommand;
	}
	
	private void registerCommands() {
		
		commands.put("register", new Command() {
			void execute(String[] arguments) {
				executer.register(arguments);
			}
		});
		
		commands.put("summon", new Command() {
			void execute(String[] arguments) {
				executer.summon(arguments);
			}
		});
		
		commands.put("users", new Command() {
			void execute(String[] arguments){
				executer.user(arguments);
			}
		});
		
		commands.put("login", new Command() {
			void execute(String[] arguments) {
				executer.login(arguments);
			}
		});
		commands.put("unsummon", new Command() {
			void execute(String[] arguments) {
				executer.unsummon(arguments);
			}
		});
		
		commands.put("logout", new Command() {
			void execute(String[] arguments) {
				executer.logout(arguments);
			}
		});
		commands.put("user", new Command() {
			void execute(String[] arguments) {
				executer.user(arguments);
			}
		});
		
		commands.put("appointment", new Command() {    //Ikke sikker at denne funker
			void execute(String[] arguments) {
				try {
					executer.appointment(arguments);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		commands.put("edit", new Command() {
			void execute(String[] arguments) {
				executer.edit(arguments);
			}
		});
		
		commands.put("delete", new Command() {
			void execute(String[] arguments) {
				executer.delete(arguments);
			}
		});
		
		
	}

	public java.util.Set<String> getCommands() {
		return commands.keySet();
	}

}

abstract class Command {
	abstract void execute(String[] parameters);
}