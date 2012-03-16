package model;

import java.sql.Time;
import java.util.Date;

/* TO-DO 
 * Må legge til en kalenderpeker! Også i konstruktøren
 * Metoder for å beregne endTime ut ifra startTime og appLength
 * +++
 */
public class Appointment implements Comparable<Appointment>{
	
	
	private Date startTime;
	private Date endTime;
	private Time appLength;
	
	private String description;
	private String place;
	private String title;
	

	
	public Appointment(Date startTime, Time appLength,String title, String description, String place){
	
	this.startTime = startTime;
	this.appLength = appLength;
	this. description = description;
	this.place = place;
	this.title = title;
	
	
	}
	
	/*
	 * Metoder
	public void editStartTime(){
			
	}
	
	public void editDate(){
		
	}
	*/
	
	@Override
	public int compareTo(Appointment other) {
		// TODO Auto-generated method stub
		return 0;
	}

}
