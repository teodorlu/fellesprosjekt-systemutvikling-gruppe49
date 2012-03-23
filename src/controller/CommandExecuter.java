package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.Person;
import model.Room;
import model.User;
import model.Time;
import model.Appointment;

import application.Application;
import application.ApplicationComponent;


public class CommandExecuter extends ApplicationComponent {
	
	private HashMap<String, String> doc;
	
	public CommandExecuter(Application app) {
		super(app);
		buildCommandDocumentation();
	}
	
	private void buildCommandDocumentation(){
		doc = new HashMap<String, String>();
		
		doc.put("register", "register -u <username> -p <password> -fn <firstName> -ln <lastName> -email <email>");
		doc.put("user", "user <brukernavn>");
		doc.put("login", "login <username> <password>");
		doc.put("appointment", "appointment -title <title> -date <date> -s <start> -e <end> [ -desc <description> -place <place> ]");
		doc.put("delete", "delete <id>");
		doc.put("edit", "edit <id> [ -title <title> -date <date> -s <start> -e <end> -desc <description> -place <place> ]");
		doc.put("summon", "summon <id> <username1> [ <username2> <username3> ... ]");
		doc.put("unsummon", "unsummon <id> <username1> [<username2> <username3> ... ]");
		doc.put("reply", "reply <id> ( Y/N )");
		doc.put("room", "room");
		doc.put("reserve", "reserve -a <appointmentID> ( -r <roomID> / -c <capacity> )");
		doc.put("calendar", "calendar [ -w <week> [ -y <year ] -u <username1> <username2> <username3> ... ]");
		doc.put("notifications", "notifications");
	}
	
	private boolean isValidInput(String[] input, String format){
		// Parse format
		List<String> mandatoryArguments = new LinkedList<String>();
		List<String> optionalArguments = new LinkedList<String>();
		Map<String, List<String>> children = new HashMap<String, List<String>>();
		Map<String, List<String>> mustContainOneOf = new HashMap<String, List<String>>();
		// TODO parse format
		
		boolean isValid = true;
		// TODO validate
		return isValid;
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
			
			endIndex = input.indexOf("-d");
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
			
			//ToDo må lage en appointment som blir sendt til databasen! (Husk dato og tid lenger oppe)
			
			
			System.out.println("Tittel: "+title+" Date: "+ date.toString()+" Start: "+ startTime.toString()+" Duration: "+ appLength.toString()+" Desc: "+ desc+" Place: "+ place);
			Appointment a = new Appointment(date, startTime, appLength, title, desc, place);
			this.getApplication().getCurrentlyLoggedInUser().getPersonalCalendar().addAppointment(a);
			this.getApplication().getDatabaseController().newAppointment(a);
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(_date);
		return date;
	}
	
	// TODO check if logged in
	public void delete(String[] array){		
		String IDstring = "-1";
		int ID;
		List<String> input = Arrays.asList(array);
		List<Appointment> appointments;
		int deleteIndex = input.indexOf("delete");
		if(input.size() > 1) IDstring = getProperty(array, deleteIndex+1);
		ID = Integer.parseInt(IDstring);
		
		
		
		
		if (this.getApplication().getDatabaseController().tryDeleteAppointment(ID))
				System.out.println("The appointment has been deleted");
			else{
				System.out.println("This is not a valid appointment ID");
				System.out.println("Mulige avtaler"); //hvordan finner jeg mulige avtaler? 
				appointments = this.getApplication().getDatabaseController().retrieveAppointments(this.getApplication().getCurrentlyLoggedInUser());
				for(int i = 0; i < appointments.size();i++){
					Appointment a = appointments.get(i);
					System.out.println("ID: "+a.getID()+" Tittle: "+a.getTitle());
				}
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
			System.out.println("Brukernavn: "+a.getUsername());
			System.out.println("Fornavn: "+a.getFirstName());
			System.out.println("Etternavn: "+a.getLastName());
			System.out.println("E-mail: "+a.getEmail());
		}
		
		else{
			//Skriv ut en liste med alle brukernavna
			usernames = this.getApplication().getDatabaseController().retriveUsernames();
			for(int i = 0; i < usernames.size(); i++){
				String name = usernames.get(i);
				System.out.println(name);
			}
		}
	}
	//Denne metoden har ikke sikring for at du er logget på, stygg output som kræsjer programmet om du ikke er!
	public void edit(String[] array){
		
		List<String> input = Arrays.asList(array);
		List<Appointment> appointments;
		Appointment localAppointment;
		int ID,editIndex;
		appointments = this.getApplication().getDatabaseController().retrieveAppointments(this.getApplication().getCurrentlyLoggedInUser());
		
		if(input.contains("edit") && input.size() == 1){
					
			for(int i = 0; i < appointments.size(); i++){
				Appointment a = appointments.get(i);
				System.out.println("ID: "+a.getID()+" Tittel: "+a.getTitle());   
			}
		}
		
		else if(input.contains("edit") && input.size() > 1){
			editIndex = input.indexOf("edit");
			ID = Integer.parseInt(getProperty(array, editIndex+1));
			for(int i = 0; i < appointments.size(); i++){
				localAppointment = appointments.get(i);
				
				if(localAppointment.getID()==ID){
					
					if(input.contains("-title")){
						int titleIndex = input.indexOf("-title");
						if(this.getApplication().getDatabaseController().editAppointment(ID, "Tittel", getProperty(array, titleIndex+1))) 
							System.out.println("Tittel ble endret til "+getProperty(array,titleIndex+1));
					}
						
										
					try {
					if(input.contains("-date")){
						int dateIndex = input.indexOf("-date");
						localAppointment.setDate(stringToDate(array, dateIndex+1));
						if(this.getApplication().getDatabaseController().editAppointment(ID, "Dato", getProperty(array, dateIndex+1))) 
							System.out.println("Dato ble endre til: "+stringToDate(array,dateIndex+1));
					}
					
					if(input.contains("-s")){
						int startIndex = input.indexOf("-s");
						localAppointment.setStartTime(getTimeProperty(input.get(startIndex+1)));
						if(this.getApplication().getDatabaseController().editAppointment(ID, "Starttid", getProperty(array, startIndex+1)))
							System.out.println("StartTid ble endre til: "+getTimeProperty(input.get(startIndex+1)));
					}
					
					if(input.contains("-d")){
						int durationIndex = input.indexOf("-d");
						localAppointment.setAppLength(getTimeProperty(input.get(durationIndex+1)));
						if(this.getApplication().getDatabaseController().editAppointment(ID, "Varighet", getProperty(array, durationIndex+1)))
							System.out.println("Varighet ble endre til: "+getTimeProperty(input.get(durationIndex+1)));
					}
					} catch (ParseException e) {
						e.getMessage();
						
					} catch (IllegalArgumentException e) {
						e.getMessage();
					}
					
					if(input.contains("-desc")){
						int descIndex = input.indexOf("-desc");
						if(this.getApplication().getDatabaseController().editAppointment(ID, "Beskrivelse", getProperty(array, descIndex+1)))
							System.out.println("Beskrivelse ble endret til: "+getProperty(array, descIndex+1));
					}
					
					if(input.contains("-place")){
						int placeIndex = input.indexOf("-place");
						if(this.getApplication().getDatabaseController().editAppointment(ID, "Sted", getProperty(array, placeIndex+1)))
							System.out.println("Sted ble endret til: "+getProperty(array,placeIndex+1));
					}
								
					break;
				}
				
				
				
			}
			
		}
		
		
		
	}
	
	public void rooms(){		
		//TODO add user check
		List<Room> roomList = getApplication().getDatabaseController().retrieveAllRooms();
		
		
	}
	
	
	public static void main(String args[]) throws ParseException{
		CommandExecuter ce = new CommandExecuter(null);
		String[] s = {"2012-03-23"};
		ce.stringToDate(s, 0);
	
	}
}
