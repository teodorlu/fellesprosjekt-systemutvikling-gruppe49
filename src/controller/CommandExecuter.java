package controller;

import java.security.acl.NotOwnerException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import model.Notification;
import model.Person;
import model.ReplyStatus;
import model.Room;
import model.User;
import model.Time;
import model.Appointment;
import model.Meeting;

import application.Application;
import application.ApplicationComponent;


public class CommandExecuter extends ApplicationComponent {

	private HashMap<String, String> doc;
	List<Notification> notificationList;

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
		doc.put("unsummon", "unsummon <id> <username1> [ <username2> <username3> ... ]");
		doc.put("reply", "reply <id> ( Y/N )");
		doc.put("room", "room");
		doc.put("reserve", "reserve -a <appointmentID> ( -r <roomID> / -c <capacity> )");
		doc.put("calendar", "calendar [ -w <week> [ -y <year ] -u <username1> <username2> <username3> ... ]");
		doc.put("notifications", "notifications");
	}
	
	public List<Notification> getNotificationList(){
		return this.notificationList;
	}

	public void register(String[] array){

		List<String> input = Arrays.asList(array);
		String username, password, firstName, lastName, _email;

		if(input.size() == 1){
			this.getApplication().getConsoleView().showRegError("noInput");
			return;
		}
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
							if(this.getApplication().getDatabaseController().Save(u));
								this.getApplication().getConsoleView().showRegSuccess();
						}
						else this.getApplication().getConsoleView().showRegError("mail");


					}
					else this.getApplication().getConsoleView().showRegError("etternavn");
				}
				else this.getApplication().getConsoleView().showRegError("fornavn");
			}
			else this.getApplication().getConsoleView().showRegError("passord");
		}
		else this.getApplication().getConsoleView().showRegError("username");
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
			if(username == null || password == null){
				this.getApplication().getConsoleView().showLoginOptions();
				return;
			}
			User user = new User(username, password);
			this.getApplication().tryLogIn(user);
		}
		else
			this.getApplication().getConsoleView().showLoginOptions();
	}


	public void logout(String[] array) {
		if (isLoggedIn()) {
			User user = this.getApplication().getCurrentlyLoggedInUser();
			this.getApplication().getDatabaseController()
			.updateLoginStatus(user, false);
			this.getApplication().setCurrentUser(null);
			this.getApplication().logout();
		}
	}

	public void appointment(String[] array) throws ParseException{
		if(isLoggedIn()){
			List<String> input = Arrays.asList(array);
			String title = "", desc = "", place = "", sStart, sEnd;
			Date date;
			Time startTime = new Time(0,0);
			Time appLength = new Time(0,0);
			int titleIndex, dateIndex, startIndex, endIndex, descIndex, placeIndex, colonIndex, length;

			if(input.contains("-title") && input.contains("-date") && input.contains("-s")
					&& input.contains("-d")){

				titleIndex = input.indexOf("-title");
				title = getProperty(array, titleIndex+1);

				dateIndex = input.indexOf("-date");
				date = stringToDate(array, dateIndex+1);
				if(date == null){
					this.getApplication().getConsoleView().showAppontmentInputError();
					return;
				}

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
				this.getApplication().getConsoleView().showAppontmentDetails(title, date.toString(), startTime.toString(), appLength.toString(), desc, place);
				Appointment a = new Appointment(date, startTime, appLength, title, desc, place);
				//this.getApplication().getCurrentlyLoggedInUser().getPersonalCalendar().addAppointment(a);
				this.getApplication().getDatabaseController().newAppointment(a);
				//------------------------------------
			}
			else this.getApplication().getConsoleView().showAppontmentInputError();
		}
	}


	private String getProperty(String[] array, int index){


		if(index >= array.length){
			return null;
		}
		String word = array[index];
		if(word.charAt(0)=='-'){
			return null;
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

			else if(length == 4){
				time = new Time((Integer.parseInt(s.substring(0, 1))), Integer.parseInt(s.substring(2, 4)));
			}

		}
		else throw new IllegalArgumentException();

		return time;
	}

	private static Date stringToDate(String[] array, int index) throws ParseException{
		String _date = array[index];
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(!validDate(_date, df))
			return null;
		Date date = df.parse(_date);
		return date;
	}

	private static boolean validDate(String date, DateFormat df) {
        df.setLenient(false);
        try {
            df.parse(date);
        } catch (ParseException e) {
            System.out.println("Date " + date + " is not valid according to " +
                    ((SimpleDateFormat) df).toPattern() + " pattern.");
            return false;
        }
		return true;
	}

	public void delete(String[] array){		
		if(isLoggedIn()){
			String IDstring = "-1";
			int ID;
			List<String> input = Arrays.asList(array);
			int deleteIndex = input.indexOf("delete");
			if(input.size() > 1) IDstring = getProperty(array, deleteIndex+1);
			ID = Integer.parseInt(IDstring);

			if (this.getApplication().getDatabaseController().tryDeleteAppointment(ID)){
				this.getApplication().getConsoleView().showApplicationDeleted();
			}
			else{
				this.getApplication().getConsoleView().showApplicationDoesNotExistError();
			}
		}
	}

	public void user(String[] array){
		if(isLoggedIn()){
			List<String> input = Arrays.asList(array);
			List<String> usernames;
			String username;
			int uIndex;
			
			if(input.contains("online")){
				this.getApplication().getConsoleView().showOnlineUsers();
			}

			else if(input.contains("user") && input.size() == 2){

				uIndex = input.indexOf("user");
				username = getProperty(array ,uIndex+1);
				this.getApplication().getConsoleView().showUser(username);
			} 

			else{
				this.getApplication().getConsoleView().showAllUsers();

			}	
		}
	}

	public void edit(String[] array){
		if(isLoggedIn()){

			List<String> input = Arrays.asList(array);
			List<Appointment> appointments;
			Appointment localAppointment;
			int ID,editIndex;
			appointments = this.getApplication().getDatabaseController().retrieveAppointments(this.getApplication().getCurrentlyLoggedInUser());

			if(input.contains("edit") && input.size() == 1){
				this.getApplication().getConsoleView().showAppointments(appointments);
			}

			else if(input.contains("edit") && input.size() > 1){
				editIndex = input.indexOf("edit");
				try{
					ID = Integer.parseInt(getProperty(array, editIndex+1));
					for(int i = 0; i < appointments.size(); i++){
						localAppointment = appointments.get(i);

						if(localAppointment.getID()==ID){

							if(input.contains("-title")){
								int titleIndex = input.indexOf("-title");
								if(this.getApplication().getDatabaseController().editAppointment(ID, "Tittel", getProperty(array, titleIndex+1)))
									this.getApplication().getConsoleView().showAppointmentTitleChange(getProperty(array,titleIndex+1));
							}


							try {
								if(input.contains("-date")){
									int dateIndex = input.indexOf("-date");
									localAppointment.setDate(stringToDate(array, dateIndex+1));
									if(this.getApplication().getDatabaseController().editAppointment(ID, "Dato", getProperty(array, dateIndex+1))) 
										this.getApplication().getConsoleView().showAppointmentDateChange(stringToDate(array,dateIndex+1));
								}

								if(input.contains("-s")){
									int startIndex = input.indexOf("-s");
									localAppointment.setStartTime(getTimeProperty(input.get(startIndex+1)));
									if(this.getApplication().getDatabaseController().editAppointment(ID, "Starttid", getProperty(array, startIndex+1)))
										this.getApplication().getConsoleView().showAppointmentStartTimeChange(getTimeProperty(input.get(startIndex+1)));
								}

								if(input.contains("-d")){
									int durationIndex = input.indexOf("-d");
									localAppointment.setAppLength(getTimeProperty(input.get(durationIndex+1)));
									if(this.getApplication().getDatabaseController().editAppointment(ID, "Varighet", getProperty(array, durationIndex+1)))
										this.getApplication().getConsoleView().showAppointmentLengthChange(getTimeProperty(input.get(durationIndex+1)));
								}
							} catch (ParseException e) {
								e.getMessage();

							} catch (IllegalArgumentException e) {
								e.getMessage();
							}

							if(input.contains("-desc")){
								int descIndex = input.indexOf("-desc");
								if(this.getApplication().getDatabaseController().editAppointment(ID, "Beskrivelse", getProperty(array, descIndex+1)))
									this.getApplication().getConsoleView().showAppointmentDescriptionChange(getProperty(array, descIndex+1));
							}

							else if(input.contains("-place")){
								int placeIndex = input.indexOf("-place");
								if(this.getApplication().getDatabaseController().editAppointment(ID, "Sted", getProperty(array, placeIndex+1)))
									this.getApplication().getConsoleView().showAppointmentPlaceChange(getProperty(array,placeIndex+1));
							}
							break;
						}		
					}
				}
				catch(NumberFormatException nFE) {
					this.getApplication().getConsoleView().showEditInputError();
				}
			}
		}



	}

	public void summon(String[] array){
		if(isLoggedIn()){

			List<String> input = Arrays.asList(array);
			int ID = -1;


			List<Appointment> allALL = this.getApplication().getDatabaseController().retrieveAppointments(this.getApplication().getCurrentlyLoggedInUser());
			allALL.addAll(this.getApplication().getDatabaseController().retrieveMeetings(this.getApplication().getCurrentlyLoggedInUser()));



			if(input.size()==1){
				this.getApplication().getConsoleView().showSummonHelp();
				return;
			}

			if(allALL.size() < 1) this.getApplication().getConsoleView().showNoMeetings();

			if(input.size() == 2){
				int IDIndex = input.indexOf("summon") + 1;
				try{
					ID = Integer.parseInt(getProperty(array,IDIndex));
				}catch (IllegalArgumentException e){
					this.getApplication().getConsoleView().showIllegalIntSummon();
				}
				

				for(int k = 0; k < allALL.size(); k++){
					Appointment localMeeting = allALL.get(k);
					
					if(localMeeting.getID()==ID){
						if(localMeeting instanceof Meeting)System.out.println(((Meeting) localMeeting).getParticipants());
						else System.out.println("Dette er ikke et møte");
					}
					
				}
			}

			else if(input.size() > 1){
				int IDIndex = input.indexOf("summon")+1;
				ID = Integer.parseInt(getProperty(array, IDIndex));


				List<String> usernames = this.getApplication().getDatabaseController().retrieveUsernames();

				for(int i = 0; i < allALL.size(); i++){
					Appointment localMeeting = allALL.get(i);				//Cast til meeting fra appointment
					if(localMeeting.getID()==ID){										//Funnet riktig Møte
						for(int j = IDIndex+1; j < input.size(); j++){					//Går gjennom alle navna du skrev inn
							
							if(localMeeting instanceof Meeting){
								Meeting localMe = (Meeting)localMeeting;
								
							if(!usernames.contains(getProperty(array,j))) this.getApplication().getConsoleView().showUserDoesNotExist(getProperty(array, j));
							

							if (localMe.getParticipants().contains(getProperty(array, j))){
								this.getApplication().getConsoleView().showAppointmentAlreadyContains(getProperty(array,j));
								continue;
							}
							}

							if(!usernames.contains(getProperty(array,j)))			//Sjekker om brukeren finnes i lista over brukere
								continue;

							this.getApplication()
							.getDatabaseController()
							.summonToMeeting(getProperty(array, j),
									localMeeting.getID()); // SummonToMeeting
							this.getApplication().getConsoleView().showAppointmentAddedPerson(getProperty(array,j)); // Output med navnet

							this.getApplication().getDatabaseController().editAppointment(localMeeting.getID(), "TYPE", "Møte");

						}
					}
				}



			}
		}
	}
	
	/*public List<Meeting> getListOfMeetingsFromAppointments(List<Appointment> list){
		List<Meeting> returnList = new List<Meeting>();
		
		for (int i = 0; i < list.size(); i++) {
			Appointment localVar = list.get(i);
			
			if(localVar instanceof Meeting){
				Meeting localMe = (Meeting)localVar;
				returnList.add(localMe);
			}
		}
		
		return returnList;
	}*/

	/*public void summon(String[] array){
		List<String> input = Arrays.asList(array);
		List<String> usernames = this.getApplication().getDatabaseController().retriveUsernames();
		List<Appointment> allMeetings;
		int summonIndex, ID;
		Meeting localMeeting;
		boolean fantMote = false;

		allMeetings = this.getApplication().getDatabaseController().retrieveMeetings(this.getApplication().getCurrentlyLoggedInUser());

		summonIndex = input.indexOf("summon");

		ID = Integer.parseInt(getProperty(array, summonIndex+1));
		for(int i = 0; i < allMeetings.size(); i++){


			localMeeting = (Meeting) allMeetings.get(i);
			if(localMeeting.getID()==ID){
				for(int j = summonIndex + 2; j < input.size(); j++){
					if(!localMeeting.getParticipants().contains(getProperty(array,j))){
						if(usernames.contains(getProperty(array,j))){
							//Lag ny notification med møteID og username
							System.out.println("Denne brukeren finnes og her skal det lages notification: "+usernames.get(j));
							fantMote = true;
						}
						else System.out.println("Brukernavn "+getProperty(array,j)+" finnes ikke!");
					}
					else System.out.println("Denne brukeren er allerede lagt til eller finnes ikke");
				}
//			break;
			}
		}
		if(fantMote == false) System.out.println("Fant ikke ditt møte");


	}
	 */


	public void notification(String[] array){
		if(isLoggedIn()){
			List<String> input = Arrays.asList(array);
			List<Notification> notificationList;
			if(input.size()==1){
				notificationList = this.getApplication().getDatabaseController().retrieveNotifications(this.getApplication().getCurrentlyLoggedInUser());
				this.getApplication().getConsoleView().showNotifications(notificationList);				
			} else if (input.contains("replies")){
				notificationList = this.getApplication().getDatabaseController().retrieveReplys();
				this.getApplication().getConsoleView().showNotificationsReplies(notificationList);
			}			
		}
	}

	public void reply(String[] array){
		if(isLoggedIn()){
			List<String> input = Arrays.asList(array);
			//		reply -id -y/n -reason

			List<Notification> notifications = this.getApplication().getDatabaseController().retrieveNotifications(this.getApplication().getCurrentlyLoggedInUser());

			String answer = "";

			if(input.contains("reply")){
				if(input.size() < 3){
					this.getApplication().getConsoleView().showNoReplyArgumentsError();
					return;
				}
				int idIndex = input.indexOf("-id");
				int ID = Integer.parseInt(getProperty(array, idIndex + 1));
				int reasonIndex = input.indexOf("-reason");
				for (int i = reasonIndex+1; i < input.size(); i++){
					if(getProperty(array, i) != null){
						answer += " "+getProperty(array, i);					
					}
				}

				for (int i = 0; i < notifications.size(); i++) {
					if(ID == notifications.get(i).getSender().getID()){
						if (input.contains("y") || input.contains("yes")){
							this.getApplication().getDatabaseController().replyToSummon(ID, 1, answer);
							notifications.get(i).setReplyStatus(ReplyStatus.JA);
						}
						else if(input.contains("n") || input.contains("no")){
							this.getApplication().getDatabaseController().replyToSummon(ID, 0, answer);
							notifications.get(i).setReplyStatus(ReplyStatus.NEI);
						}
						else 
							this.getApplication().getConsoleView().showNoReplyArgumentsError();
					}
				}
			}
		}
	}



	@SuppressWarnings("deprecation")
	public void rooms(){		
		//TODO add user check
		List<Room> roomList = getApplication().getDatabaseController().retrieveAllRooms();
		int roomListSize = roomList.size();
		Date date = new Date();
		StringBuilder returnStatement = new StringBuilder();
		boolean availableRoomCheck = false;
		for (int i=0; i<roomListSize; i++){
			if(getApplication().getDatabaseController().isRoomAvailable(roomList.get(i).getRoomId(),
					(java.sql.Date) date, new Time(date.getHours(),date.getMinutes()), new Time(02,00))){
				returnStatement.append(roomList.get(i).getRoomId() + " ");
				availableRoomCheck = true;
			}
		}
		if (availableRoomCheck)
			System.out.println("Disse rommene er ledige: " +returnStatement);
		else
			System.out.println("Det er desverre ingen rom ledige");

	}

	public void unsummon(String[] array){
		if(isLoggedIn()==true){
		
		List<String> input = Arrays.asList(array);
		
		List<Meeting> allMeetings = this.getApplication().getDatabaseController().retrieveMeetings(this.getApplication().getCurrentlyLoggedInUser());  //Laster inn en liste med meetings
		
		
		
		if(input.size()==1) System.out.println(doc.get("unsummon"));
		
		if(allMeetings.size() < 1) {
			this.getApplication().getConsoleView().showNoMeetings();
		
		
		}else if(input.size() > 1){
			int IDIndex = input.indexOf("unsummon")+1;
			int ID = Integer.parseInt(getProperty(array, IDIndex));
			
			
			List<String> usernames = this.getApplication().getDatabaseController().retrieveUsernames();
			
			for(int i = 0; i < allMeetings.size(); i++){
				Meeting localMeeting = (Meeting)allMeetings.get(i);				//Cast til meeting fra appointment
				if(localMeeting.getID()==ID){										//Funnet riktig Møte
					int slettet = 0;
					for(int j = IDIndex+1; j < input.size(); j++){					//Går gjennom alle navna du skrev inn
						
						if(!usernames.contains(getProperty(array,j))) this.getApplication().getConsoleView().showUserDoesNotExist(getProperty(array, j));
						
						if (!localMeeting.getParticipants().contains(getProperty(array, j))){
							this.getApplication().getConsoleView().showAppointmentDoesNotContain(getProperty(array,j));
							continue;
						}
						
					
						if(!usernames.contains(getProperty(array,j)))			//Sjekker om brukeren finnes i lista over brukere
							continue;

						this.getApplication()
								.getDatabaseController()
								.unsummonToMeeting(getProperty(array, j),
										localMeeting.getID()); // unsummonToMeeting
									this.getApplication().getConsoleView().showAppointmentRemovedPerson(getProperty(array,j)); // Output med navnet
						slettet++;
					}
					if(slettet > 0){
						int medlemmer = this.getApplication().getDatabaseController().retrieveNumOfParticipants(localMeeting.getID());
						if(medlemmer == 0){
							this.getApplication().getDatabaseController().editAppointment(localMeeting.getID(), "TYPE", "Avtale");
							this.getApplication().getConsoleView().showAppointmentChangeToApp();

						}
					}
				}
			}
			
			
			
		}
		}
	}
	public void reserve(String[] array){
		if(isLoggedIn()==true){
			List<String> input = Arrays.asList(array);
			int appIDIndex = input.indexOf("-a") + 1;
			String appID = input.get(appIDIndex);
			int applicationID = -1;
			String RoomID = "-1";
			String needCapacity = "-1";
			int capacity = -1;
			if(input.contains("-r")){
				int RoomIDIndex = input.indexOf("-r") + 1;
				RoomID = input.get(RoomIDIndex);
			}else if(input.contains("-c")){
				int capacityIndex = input.indexOf("-c")+1;
				needCapacity = input.get(capacityIndex);
				try{
					capacity = Integer.parseInt(needCapacity);
				}catch(NumberFormatException e){
					e.printStackTrace();
				}
				
				
				
				return;
				
				
				
			}else{
				System.out.println(doc.get("reserve"));
				return;
			}
			try{
				applicationID = Integer.parseInt(appID);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
			List<Room> allRooms = this.getApplication().getDatabaseController().retrieveAllRooms();
			List<String> allRoomIDs = new ArrayList<String>();
			for(int i=0; i < allRooms.size();i++){
				allRoomIDs.add(allRooms.get(i).getRoomId());
			}
			if(allRoomIDs.contains(RoomID)){
				Appointment thisApp = this.getApplication().getDatabaseController().retrieveAnAppointment(applicationID);
				java.sql.Date a = new java.sql.Date(thisApp.getDate().getTime());
				a.setMonth(a.getMonth()+1);
				a.setYear(a.getYear()+1900);
				if(this.getApplication().getDatabaseController().isRoomAvailable(RoomID, 
						a ,
						thisApp.getStartTime(), 
						thisApp.getAppLength()  )){
					this.getApplication().getDatabaseController().reserveRoomWithID(applicationID, RoomID);
					this.getApplication().getConsoleView().showAppointmentRoomReserved(RoomID);
				}else{
					this.getApplication().getConsoleView().showAppointmentRoomAlreadyReserved(RoomID);
				}
			}else{
				this.getApplication().getConsoleView().showAppointmentRoomDoesNotExist(RoomID);
			}
			
		}
	}

	
	public void calendar(String[] array){
		List<String> input = Arrays.asList(array);
		
		if(input.size() == 1){
			
		}
		
		
	}
	
	public void mycal(String[] array) {
		if (isLoggedIn()) {
			List<Appointment> allAppointmentsAndMeetings = this
					.getApplication()
					.getDatabaseController()
					.retrieveMeetingsAndAppointments(
							this.getApplication().getCurrentlyLoggedInUser());
			this.getApplication().getConsoleView().showAllMeetingsAndApps(allAppointmentsAndMeetings);
		}
	}
	
	private boolean isLoggedIn(){
		if(this.getApplication().getLoggedIn())
			return true;
		this.getApplication().getConsoleView().showNotLoggedIn();
		return false;


	}

	public void exit(String[] arguments) {
		if (this.getApplication().getLoggedIn()) {
			this.getApplication().logout();
		}
		System.exit(0);
	}


}

