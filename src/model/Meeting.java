package model;

import model.Time;
import java.util.ArrayList;
import java.util.Date;

public class Meeting extends Appointment {
	
	//Autogenerert fordi jeg var lat og ikke liker røde x'er, 
	private Date date;
	private Time appLength, startTime;
	private String title;
	private String description;
	private String place; // denne er vel null på meeting? 
	private ArrayList<String> participants;
	private Room room;
	private int ID;
	
	
	
	
	public Meeting( int ID, Date date, java.sql.Time startTime, java.sql.Time appLength, String title,
			String description, String place, ArrayList<String> participants, Room room) {
		super(ID, date, startTime, appLength, title, description, place);
		this.participants = participants;
		//changeRoom(room);
		this.room = room;
		//System.out.println(ID+"/");
		//System.out.println(this.ID +"*");
	}
	public Meeting( int ID, Date date, Time startTime, Time appLength, String title,
			String description, String place, ArrayList<String> participants, Room room) {
		super(ID, date, startTime, appLength, title, description, place);
		this.participants = participants;
		//changeRoom(room);
		this.room = room;
	}
	
/*	public void changeRoom(Room room){
		if (room.isRoomFree()){
			room.reserveRoom();
			this.room = room;
		}
		else
			System.out.println("This room is already reserved");
	}
	*/
	
	public Room getRoom(){
		return this.room;
	}
	
	public ArrayList<String> getParticipants(){
		return this.participants;
	}

	public void addParticipant(String username){
		if (participants.contains(username))
			System.out.println("This person is already in the meeting");
		
		else
			participants.add(username);
	}
	
	public void removeParticipant(String username){
		if (participants.contains(username))
			participants.remove(username);
		
		else
			System.out.println("This person is not in the meeting");
	}
	
	public void changeDate(Date newDate){
		this.date = newDate;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public Time getDuration(){
		return this.appLength;
	}
	
	public void showDescription(){
		System.out.println("" + this.description);
	}
	
	
}
