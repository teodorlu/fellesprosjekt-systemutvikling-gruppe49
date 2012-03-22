package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.Person;
import model.User;
import model.Time;
import model.Appointment;

import application.Application;
import application.ApplicationComponent;


public class CommandExecuter extends ApplicationComponent {
	
	public CommandExecuter(Application app) {
		super(app);
	}

	public void register(String[] array){
		
		List<String> input = Arrays.asList(array);
		String username, password, firstName, lastName, _email;
		
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
						if(input.contains("-email")){
							int emailIndex = input.indexOf("-email");
							_email = getProperty(array, emailIndex +1);
							Person u = new Person(username, password, firstName, lastName, _email);
														
							System.out.println(_email);
							
							this.getApplication().getDatabaseController().Save(u);
						}
						else System.out.println("Feil input: Email");
						
						
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
		String title = "", desc = "", place = "", sStart, sEnd;
		Date date;
		Time startTime = new Time(0,0);
		Time appLength = new Time(0,0);
		int titleIndex, dateIndex, startIndex, endIndex, descIndex, placeIndex, colonIndex, length;
				
		if(input.contains("-title") && input.contains("-date") && input.contains("-s")
				&& input.contains("-e")){
			
			titleIndex = input.indexOf("-title");
			title = getProperty(array, titleIndex+1);
			
			dateIndex = input.indexOf("-date");
			date = stringToDate(array, dateIndex+1); 
			
			startIndex = input.indexOf("-s");
			sStart = getProperty(array, startIndex+1); 
			
			endIndex = input.indexOf("-e");
			sEnd = getProperty(array, endIndex+1); 
			
			startTime = getTimeProperty(sStart);
			appLength = getTimeProperty(sEnd);

					
			if(input.contains("-desc")){
				descIndex = input.indexOf("-desc");
				desc = getProperty(array, descIndex + 1);
			}
			
			if(input.contains("-place")){
				placeIndex = input.indexOf("-place");
				place = getProperty(array, placeIndex + 1);
			}
			
			//ToDo må lage en appointment gjennom konstruktøren! (Husk dato og tid lenger oppe)
			
			
			System.out.println("Tittel: "+title+" Date: "+ date.toString()+" Start: "+ startTime.toString()+" End: "+ appLength.toString()+" Desc: "+ desc+" Place: "+ place);
			Appointment a = new Appointment(date, startTime, appLength, title, desc, place);
			this.getApplication().getCurrentlyLoggedInUser().getPersonalCalendar().addAppointment(a);
			//------------------------------------
		}
		else System.out.println("Har ikke med alle parameterne til appointment");
	}
	
	
	private String getProperty(String[] array, int index){
		
		
		if(index >= array.length){
			
			throw new IllegalArgumentException();
		}
		String word = array[index];
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
	


	
	public void delete(String[] array){
		String ID = "";
		List<String> input = Arrays.asList(array);
		int deleteIndex = input.indexOf("delete");
		ID = getProperty(array, deleteIndex+1);
		if (DatabaseController.deleteID(ID))
				System.out.println("The appointment has been deleted");
			else{
				System.out.println("This is not a valid appointment ID");
				System.out.println("Mulige avtaler"); //hvordan finner jeg mulige avtaler?
			}
	}
	
	public void user(String[] array){
		List<String> input = Arrays.asList(array);
		List<String> usernames;
		String username;
		int uIndex;
		
		if(input.contains("user") && input.size() == 2){
			
			uIndex = input.indexOf("user");
			username = getProperty(array ,uIndex+1);
			Person a;
			a = this.getApplication().getDatabaseController().retriveUser(username);
			System.out.println("Brukernavn: "+a.getUsername()+" Fornavn:"+a.getFirstName()+" Etternavn:"+a.getLastName()+" E-mail"+a.getEmail());
			
		}
		
		else{
			//Skriv ut en liste med alle brukernavna
			usernames = this.getApplication().getDatabaseController().retriveUsernames();
			for(int i = 0; i < usernames.size()-1; i++){
				System.out.println(usernames.indexOf(i));
			}
		}
	}
	
	public void edit(String[] array){
		List<String> input = Arrays.asList(array);
		List<Appointment> appointments;
		
		if(input.contains("edit") && input.size() == 1){
			appointments = this.getApplication().getDatabaseController().retrieveAppointments(this.getApplication().getCurrentlyLoggedInUser());
			
			for(int i = 0; i < appointments.size()-1; i++){
				System.out.println(appointments.indexOf(i));   //Gjør sånn at den bare printer ut AvtaleID og AvtaleTittel
			}
		}
		
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
