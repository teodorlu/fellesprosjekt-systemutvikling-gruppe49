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

}
