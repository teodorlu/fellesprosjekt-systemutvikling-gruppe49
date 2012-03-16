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
			void execute(String[] arguments){
				CommandExecuter.register(arguments);
			}};
		commands.put("register", (Command) register);
		
		Object login = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("login", (Command) login);
		
		Object appointment = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("appointment", (Command) appointment);
		
		Object delete = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("delete", (Command) appointment);
		
		Object edit = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("edit", (Command) appointment);
		
		Object summon = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("summon", (Command) appointment);
		
		Object unsummon = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("unsummon", (Command) appointment);
		
		Object reply = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("reply", (Command) appointment);
		
		Object room = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("room", (Command) appointment);
		
		Object reserve = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("reserve", (Command) appointment);
		
		Object calendar = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("calendar", (Command) appointment);
		
		Object notifications = new Object(){
			void execute(String[] arguments){
				
			}};
		commands.put("notifications", (Command) appointment);
		
	}

}

interface Command {
	void execute(String[] parameters);
}