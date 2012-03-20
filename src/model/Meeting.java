package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Meeting extends Appointment {
	
	//Autogenerert fordi jeg var lat og ikke liker røde x'er, 
	private Date startTime;
	private Time appLength;
	private String title; // Står det at vi skal ha med title? Er ikke det en del av description?
	private String description;
	private String place; // denne er vel null på meeting? 
	private ArrayList<Person> participants;
	private Room room;
	
	
	
	
	public Meeting(Date startTime, Time appLength, String title,
			String description, String place, ArrayList<Person> participants, Room room) {
		super(startTime, appLength, title, description, place);
		this.participants = participants;
		changeRoom(room);
		// TODO Auto-generated constructor stub
	}
	
	public void changeRoom(Room room){
		if (room.isRoomFree()){
			room.reserveRoom();
			this.room = room;
		}
		else
			System.out.println("This room is already reserved");
	}
	
	public Room getRoom(){
		return this.room;
	}
	
	public ArrayList<Person> getParticipants(){
		return this.participants;
	}

	public void addParticipant(Person person){
		if (participants.contains(person))
			System.out.println("This person is already in the meeting");
		
		else
			participants.add(person);
	}
	
	public void removeParticipant(Person person){
		if (participants.contains(person))
			participants.remove(person);
		
		else
			System.out.println("This person is not in the meeting");
	}
	
	public void changeDate(Date newDate){
		this.startTime = newDate;
	}
	
	public Date getDate(){
		return this.startTime;
	}
	
	public Time getTime(){
		return this.appLength;
	}
	
	public void showDescription(){
		System.out.println("" + this.description);
	}
	

	
}
