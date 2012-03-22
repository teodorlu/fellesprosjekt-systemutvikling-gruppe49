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
	public boolean between(Time low, Time high){
		boolean isBetween = false;
		int lowM = low.returnMinutes();
		int lowH = low.returnHours();
		int highM = high.returnMinutes();
		int highH = high.returnHours();
		if (this.returnHours() == lowH){
			if(this.returnMinutes() > lowM)
				isBetween = true;
		
		}		
		if (this.returnHours() == highH){
			if(this.returnMinutes() < highM)
				isBetween = true;
		}
		if(this.returnHours() < highH && this.returnHours() > lowH){
			isBetween = true;
		}
		
		
		return isBetween;
	}
	public static void main(String[] args) {
		 Time b = new Time(13,0);
		 Time a = new Time(12,0);
		 Time c = new Time(13,30);
		 boolean d = b.between(a, c);
		 System.out.println(d);
		 
	}
	
}
