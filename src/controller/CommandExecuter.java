package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommandExecuter {
	
	public static void register(String[] array){
		
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
						System.out.println(username +" "+ password+" "+ firstName+" "+" "+lastName);
						// Lag et personobjekt her og fjern linja over
					}
					else System.out.println("Feil input: Etternavn");
				}
				else System.out.println("Feil input: Fornavn");
			}
			else System.out.println("Feil input: Password");
		}
		else System.out.println("Feil input: Username");
	}
	
	public static void login(String[] array){
		List<String> input = Arrays.asList(array);
		int uIndex, pIndex;
		String username, password;
		
		if(input.contains("-u") && input.contains("-p")){
			uIndex = input.indexOf("-u");
			pIndex  =input.indexOf("-p");
			username = getProperty(array, uIndex+1);
			password = getProperty(array, pIndex+1);
			System.out.println("User: "+username + " Password: "+password);
			
			//Add LoginToDo, har bare henta brukernavn i username og pw i password
		}
	}
	
	public static void appointment(String[] array) throws ParseException{
		List<String> input = Arrays.asList(array);
		String title, desc = "", place = "", sStart, sEnd;
		Date date;
		
		int titleIndex, dateIndex, startIndex, endIndex, descIndex, placeIndex;
				
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
			System.out.println("Tittel: "+title+" Date: "+ date.toString()+" Start: "+ sStart+" End: "+ sEnd+" Desc: "+ desc+" Place: "+ place);
			//------------------------------------
		}
		else System.out.println("Har ikke med alle parameterne til appointment");
	}
	
	
	private static String getProperty(String[] array, int index){
		
		String word = array[index];
		if(word.charAt(0)=='-'){
			throw new IllegalArgumentException();
		}
		return word;		
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
		String[] appointmentTest = {"-title", "HumbugAvtale", "-date", "2012-12-04", "-s", "14:30:00", "-e", "15:30:00", 
										"-desc", "Heisann tullemøte", "-place", "Fjellet"};
		register(registertest);
		login(logintest);
		appointment(appointmentTest);
	}

}
