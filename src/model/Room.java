package model;

/* INFO
 * Alle variabler burde v�re lagt til
 * Metoder (om noen) er ikke lagt til
 */

public class Room {
	
	private int roomId;
	private boolean Available;
	private int capacity;
	private String location;
	private Meeting sender;
	
	public Room(int roomId, int capacity, String location, Meeting sender){
		this.roomId = roomId;
		this.capacity = capacity;
		this.Available = true;
		this.location = location;
		this.sender = sender;
		
	}
	
	/* Metodene vil vel m�tte forandres en del p� n�r vi finner ut hvordan 
	 * vi skal gj�re det med input/output! Om de i det hele tatt skal v�re
	 * med eller om det heller skal gj�res i SQL.
	 
	
	public boolean isRoomFree(){
		if(Available==true)return true;
		else return false;
	}
	
	
	public void reserveRoom(){
		Available = false;
	}
	
	public void openRoom(){
		Available = true;
	}
	
	public void editLocation(String location){
		this.location = location;
	}
	
	public void editCapacity(int capacity){
		this.capacity = capacity;
	}
	
	*/
	
}
