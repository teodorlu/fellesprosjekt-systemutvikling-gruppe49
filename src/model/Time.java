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
	
	
	public String toString(){
		return (this.Hours+":"+this.Minutes);
		//Kan trengs å formateres, noe jeg er elendig til, Dz
	}
	
	public static void main(String[] args) {
		 Time a = new Time(3,86);
		 a.newTime(5, 67);
		 System.out.println(a.toString());
		 
	}
	
}
