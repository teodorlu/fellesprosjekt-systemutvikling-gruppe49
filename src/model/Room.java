package model;

/* INFO
 * Alle variabler burde være lagt til
 * Metoder (om noen) er ikke lagt til
 */

public class Room {
	
	private boolean Available;
	private int capacity;
	private String location, roomId;
	private Meeting sender;
	
	public Room(String roomId, int capacity, String location, Meeting sender){
		this.roomId = roomId;
		this.capacity = capacity;
		this.Available = true;
		this.location = location;
		this.sender = sender;
		
	}
	public Room(String roomId, int capacity, String location){
		this.roomId = roomId;
		this.capacity = capacity;
		this.location = location;
		
		
	}
	
//	* Metodene vil vel måtte forandres en del på når vi finner ut hvordan 
//	 * vi skal gjøre det med input/output! Om de i det hele tatt skal være
//	 * med eller om det heller skal gjøres i SQL.
	 
	
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
	

	
}
