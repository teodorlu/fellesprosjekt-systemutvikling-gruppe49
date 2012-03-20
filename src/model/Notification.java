package model;

import java.util.ArrayList;

/* INFO
 * Konstruktøren er ikke ferdig
 * Metoder er ikke lagt til
 * Alle variabler burde være tilgjengelige
 */



public class Notification {
	
	private enum type {Nytt, Oppdatert, Slettet};
	private enum reply {Ja, Nei, Ubesvart};
	private Appointment sender;
	private ArrayList<Person> receivers;	
	private String text;
	
	public Notification(String text, Appointment sender, ArrayList<Person> receivers) {
		this.text = text;
		this.type = type;
		this.reply = reply;
		this.sender = sender;
		this.receivers = receivers;

	}
	


}
