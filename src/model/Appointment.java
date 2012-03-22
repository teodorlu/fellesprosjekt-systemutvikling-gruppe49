package model;

import model.Time;
import java.util.Date;

/* TO-DO 
 * Må legge til en kalenderpeker! Også i konstruktøren
 * Metoder for å beregne endTime ut ifra startTime og appLength
 * +++
 */
public class Appointment implements Comparable<Appointment> {

	private Date date;
	private Time appLength, startTime, endTime;

	private String description, place, title;
	private int ID;

	public Appointment(int ID, Date date, Time startTime, Time appLength, String title,
			String description, String place) {

		this.ID = ID;
		this.date = date;
		this.startTime = startTime;
		this.appLength = appLength;
		this.description = description;
		this.place = place;
		this.title = title;
		this.endTime = Time.returnEndTime(this.startTime, this.appLength);
		
	}
	
	public Appointment(Date date, Time startTime, Time appLength, String title,
			String description, String place) {

		this.date = date;
		this.startTime = startTime;
		this.appLength = appLength;
		this.description = description;
		this.place = place;
		this.title = title;
		this.endTime = Time.returnEndTime(this.startTime, this.appLength);
	}
	
	@SuppressWarnings("deprecation")
	public Appointment(int ID, java.sql.Date sqldate, java.sql.Time sqlStartTime,
			java.sql.Time sqlAppLength, String title, String description, String place) {
		this.ID = ID;
		this.date = sqldate;
		this.startTime = new Time(sqlStartTime.getHours(), sqlStartTime.getMinutes());
		this.appLength = new Time(sqlAppLength.getHours(), sqlAppLength.getMinutes());
		this.description = description;
		this.place = place;
		this.title = title;
		this.endTime = Time.returnEndTime(this.startTime, this.appLength);
		
	}

	/*
	 * Metoder public void editStartTime(){
	 * 
	 * }
	 * 
	 * public void editDate(){
	 * 
	 * }
	 */
	


	@Override
	public int compareTo(Appointment other) {
		return this.date.compareTo(other.date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getAppLength() {
		return appLength;
	}

	public void setAppLength(Time appLength) {
		this.appLength = appLength;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getID() {
		return ID;
	}
	

}
