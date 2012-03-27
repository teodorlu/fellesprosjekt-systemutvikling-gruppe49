package model;

import java.util.ArrayList;

/* INFO
 * Konstruktøren er ikke ferdig
 * Metoder er ikke lagt til
 * Alle variabler burde være tilgjengelige
 */



public class Notification {
	
	private enum eType{Nytt, Oppdatert, Slettet};
	private enum eReply {Ja, Nei, Ubesvart};
	eType type;
	eReply reply;
	private Appointment sender;
	private ArrayList<Person> receivers;	
	private String text;
	
	public Notification(String text, eType type, eReply reply, Appointment sender, ArrayList<Person> receivers) {
		this.text = text;
		this.type = type;
		this.reply = reply;
		this.sender = sender;
		this.receivers = receivers;

	}

	public eType getType() {
		return type;
	}

	public void setType(eType type) {
		this.type = type;
	}

	public ArrayList<Person> getReceivers() {
		return receivers;
	}

	public void addReceivers(ArrayList<Person> receivers) {
		this.receivers.addAll(receivers);
	}
	
	public void deleteReceiver(Person person){
		if(this.receivers.contains(person)){
			this.receivers.remove(person);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public eReply getReply() {
		return reply;
	}

	public Appointment getSender() {
		return sender;
	}
	
	


}
