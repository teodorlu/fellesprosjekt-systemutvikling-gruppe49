package model;

import java.util.ArrayList;

/* INFO
 * Konstruktøren er ikke ferdig
 * Metoder er ikke lagt til
 * Alle variabler burde være tilgjengelige
 */



public class Notification {
	

	NotificationType type;
	ReplyStatus reply;
	private Appointment sender;
	private User receiver;	
	private String text;
	
	public Notification(String text, NotificationType type, ReplyStatus reply, Appointment sender, User receiver) {
		this.text = text;
		this.type = type;
		this.reply = reply;
		this.sender = sender;
		this.receiver = receiver;

	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public User getReceivers() {
		return receiver;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ReplyStatus getReply() {
		return reply;
	}

	public Appointment getSender() {
		return sender;
	}
	
	


}
