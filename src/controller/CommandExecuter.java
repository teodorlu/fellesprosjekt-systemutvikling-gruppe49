package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.Person;
import model.User;

import application.Application;
import application.ApplicationComponent;

public class CommandExecuter extends ApplicationComponent {
	
	public CommandExecuter(Application app) {
		super(app);
	}

	public void register(String[] array){
		
		List<String> input = Arrays.asList(array);
		String username, password, firstName, lastName;
		
		if(input.contains("-u")){
			int uIndex = input.indexOf("-u");
			
			username = getProperty(array, uIndex+1);
			
			if(input.contains("-p")){
				int pIndex = input.indexOf("-p");
				password = getProperty(array, pIndex+1);

				if(input.contains("-fn")){
					int fnIndex = input.indexOf("-fn");
					firstName = getProperty(array, fnIndex+1);
				
					if(input.contains("-ln")){
						int lnIndex = input.indexOf("-ln");
						lastName = getProperty(array, lnIndex+1);
						Person u = new Person(username, password, firstName, lastName);
						this.getApplication().setCurrentUser(u);
						
						// System.out.println(username +" "+ password+" "+ firstName+" "+" "+lastName);
					}
					else System.out.println("Feil input: Etternavn"); //Feilmelding istedenfor
				}
				else System.out.println("Feil input: Fornavn");
			}
			else System.out.println("Feil input: Password");
		}
		else System.out.println("Feil input: Username");
	}
	
	public void login(String[] array){
		List<String> input = Arrays.asList(array);
		int uIndex, pIndex;
		String username, password;
		
		if(input.contains("-u") && input.contains("-p")){
			uIndex = input.indexOf("-u");
			pIndex  =input.indexOf("-p");
			username = getProperty(array, uIndex+1);
			password = getProperty(array, pIndex+1);
			System.out.println("User: "+username + " Password: "+password);
			
			User user = new User(username, password);
			this.getApplication().tryLogIn(user);
		}
	}
	
	public void appointment(String[] array) throws ParseException{
		List<String> input = Arrays.asList(array);
		String title, desc = "", place = "", sStart, sEnd;
		Date date;
		Time startTime = new Time(0,0);
		Time endTime = new Time(0,0);
		int titleIndex, dateIndex, startIndex, endIndex, descIndex, placeIndex, colonIndex, length;
				
		if(input.contains("-title") && input.contains("-date") && input.contains("-s")
				&& input.contains("-e")){
			
			titleIndex = input.indexOf("-title");
			title = getProperty(array, titleIndex+1);
			
			dateIndex = input.indexOf("-date");
			date = stringToDate(array, dateIndex+1); //Denne burde funke
			
			startIndex = input.indexOf("-s");
			sStart = getProperty(array, startIndex+1); //Må lage egen metode for Tid/Dato?
			
			endIndex = input.indexOf("-e");
			sEnd = getProperty(array, endIndex+1); //Må lage egen metode for Tid/Dato?
			
			startTime = getTimeProperty(sStart);
			endTime = getTimeProperty(sEnd);

			if(!Time.checkStartEndTimes(startTime, endTime)){
				System.out.println("Idiot, det slutter før det begynner!");
			}
			
			//Muligens en funksjon som sjekker at endtime kommer etter starttime
			if(input.contains("-desc")){
				descIndex = input.indexOf("-desc");
				desc = getProperty(array, descIndex + 1);
			}
			
			if(input.contains("-place")){
				placeIndex = input.indexOf("-place");
				place = getProperty(array, placeIndex + 1);
			}
			
			//ToDo må lage en appointment gjennom konstruktøren! (Husk dato og tid lenger oppe)
			System.out.println("Tittel: "+title+" Date: "+ date.toString()+" Start: "+ startTime.toString()+" End: "+ endTime.toString()+" Desc: "+ desc+" Place: "+ place);
			//------------------------------------
		}
		else System.out.println("Har ikke med alle parameterne til appointment");
	}
	
	
	private String getProperty(String[] array, int index){
		
		String word = array[index];
		if(index >= array.length) throw new IllegalArgumentException();
		if(word.charAt(0)=='-'){
			throw new IllegalArgumentException();
		}
		return word;		
	}
	
	private static Time getTimeProperty(String s){
		int length;
		Time time = null;
		if(s.contains(":")){
			length = s.length();
			if(length == 5){
				time = new Time((Integer.parseInt(s.substring(0, 2))), Integer.parseInt(s.substring(3, 5)));
			}
			
			if(length == 4){
				time = new Time((Integer.parseInt(s.substring(0, 1))), Integer.parseInt(s.substring(2, 4)));
			}
			
		}
		else throw new IllegalArgumentException();
		
		return time;
	}
	
	private static Date stringToDate(String[] array, int index) throws ParseException{
		String _date = array[index];
		DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
		Date date = df.parse(_date);
		return date;	
	}
	

	
	private static void checkStartAndEndTime(){
		//Check if starttime is less than Endtime. 
	}
	
	public static void main(String args[]) throws ParseException{
		String[] registertest = {"register", "-u", "dzenan", "-p", "mittpassord", "-fn", "firstName", "-ln", "lastName"};
		String[] logintest = {"login", "-u", "Brukernavnet", "-p", "passordet"};
		String[] appointmentTest = {"-title", "HumbugAvtale", "-date", "2012-12-04", "-s", "14:30", "-e", "15:30", 
										"-desc", "Heisann tullemøte", "-place", "Fjellet"};
		
		CommandExecuter ce = new CommandExecuter(null);
		ce.register(registertest);
		ce.login(logintest);
		ce.appointment(appointmentTest);
	}

}
