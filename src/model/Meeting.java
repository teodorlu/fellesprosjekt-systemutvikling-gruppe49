package model;

import java.sql.Time;
import java.util.Date;

public class Meeting extends Appointment {
	
	//Autogenerert fordi jeg var lat og ikke liker røde x'er
	
	
	public Meeting(Date startTime, Time appLength, String title,
			String description, String place) {
		super(startTime, appLength, title, description, place);
		// TODO Auto-generated constructor stub
	}

}
