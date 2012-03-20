package model;

public class Time {
	
	private int Hours, Minutes;
	
	public Time(int Hours, int Minutes){
		this.Hours = 0;
		this.Minutes = 0;
		add(Hours, Minutes);
	}
	
	public void add(int Hours, int Minutes){
		this.Hours += Hours;
		this.Minutes += Minutes;
		
		if(this.Minutes >= 60){
			this.Hours += Math.floor(this.Minutes / 60);
			this.Minutes = this.Minutes % 60;
		}
		
		if(this.Hours >= 24){
			this.Hours = this.Hours % 24;
		}
	}
	
	public void newTime(int Hours, int Minutes){
		this.Hours = 0;
		this.Minutes = 0;
		add(Hours,Minutes);
	}
	
	public int returnHours(){
		return this.Hours;
	}
	
	public int returnMinutes(){
		return this.Minutes;
	}
	
	public String toString(){
		return (this.Hours+":"+this.Minutes);
		//Kan trengs å formateres, noe jeg er elendig til, Dz
	}
	
	public static boolean checkStartEndTimes(Time startTime, Time endTime){
		if(startTime.Hours < endTime.Hours) return true;
		else if(startTime.Minutes < endTime.Minutes) return true;
		else return false;
	}
	
	public static Time returnEndTime(Time startTime, Time duration){
		Time endTime = new Time(0,0);
		endTime.add(startTime.returnHours(), startTime.returnMinutes());
		endTime.add(duration.returnHours(), duration.returnMinutes());
		return endTime;
	}
	
	public static void main(String[] args) {
		 Time a = new Time(3,86);
		 a.newTime(5, 67);
		 System.out.println(a.toString());
		 
	}
	
}
