package model;

import java.sql.Time;
import java.util.Date;

/* TO-DO 
 * M� legge til en kalenderpeker! Ogs� i konstrukt�ren
 * Metoder for � beregne endTime ut ifra startTime og appLength
 * +++
 */
public class Appointment implements Comparable<Appointment> {

	private Date startTime;
	private Time appLength;

	private String description, place, title;

	public Appointment(Date startTime, Time appLength, String title,
			String description, String place) {

		this.startTime = startTime;
		this.appLength = appLength;
		this.description = description;
		this.place = place;
		this.title = title;

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
		return this.startTime.compareTo(other.startTime);
	}

}
