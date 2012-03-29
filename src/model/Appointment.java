package model;

import model.Time;
import java.util.Date;

public class Appointment implements Comparable<Appointment> {

	private Date date;
	private Time duration, startTime;

	private String description, place, title;
	private int ID;

	public Appointment(int ID, Date date, Time startTime, Time duration, String title,
			String description, String place) {

		this.ID = ID;
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		this.description = description;
		this.place = place;
		this.title = title;
	}
	
	@SuppressWarnings("deprecation")
	public Appointment(int ID, Date sqldate, java.sql.Time sqlStartTime,
			java.sql.Time duration, String title, String description, String place) {
		this.ID = ID;
		this.date = sqldate;
		this.startTime = new Time(sqlStartTime.getHours(), sqlStartTime.getMinutes());
		this.duration = new Time(duration.getHours(), duration.getMinutes());
		this.description = description;
		this.place = place;
		this.title = title;
	}
	
	public Appointment(Date date, Time startTime, Time duration, String title,
			String description, String place) {
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		this.description = description;
		this.place = place;
		this.title = title;
	}

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

	public Time getDuration() {
		return duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
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
