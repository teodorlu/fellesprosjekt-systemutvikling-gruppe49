package model;

import java.util.SortedSet;
import java.util.TreeSet;

public class UserCalendar {
	
	private SortedSet<Appointment> appointments;
	private Person owner;
	
	public UserCalendar(Person owner){
		this.owner = owner;	
		appointments = new TreeSet<Appointment>();
	}

	
	public void addAppointment(Appointment a){
		appointments.add(a);
		
	}
	
	
}