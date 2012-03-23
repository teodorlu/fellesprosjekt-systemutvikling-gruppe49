package model;

/*
 * Metodikk: databasekontrollerern henter et rom, som ikke kan redigeres i java.
 * Dersom man vil legge til et nytt rom, lager man et romobjekt som lagres i databasen.
 */

public class Room {
	
	private boolean isAvailable;
	private int capacity;
	private String location, roomId;
	
	public Room(String roomId, int capacity, String location, boolean isAvailable){
		this.roomId = roomId;
		this.capacity = capacity;
		this.location = location;
		this.isAvailable = isAvailable;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getLocation() {
		return location;
	}

	public String getRoomId() {
		return roomId;
	}
}