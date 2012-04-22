package com.delegator;

public class Clock {
	int seconds;
	int hours;
	int minutes;
	final boolean ASCENDING;
	final int targetTime;
	boolean finished = false;
	final int targetMinutes; //optimization for ascending...
	
	
	public Clock (int targetTime, boolean ascending){
		this.targetTime = targetTime;
		this.ASCENDING = ascending;
		if (!ASCENDING){
			hours = targetTime/60;
			minutes = targetTime%60;
			seconds = 0;
		}
		this.targetMinutes = targetTime%60;
	}
	
	public String tickUp(){
		if (seconds < 60)
			seconds++;
		else if (minutes < 60){ 
			minutes++;
			seconds = 0;
		}
		else{
			hours++;
			seconds = minutes = 0;
		}
		if(hours == targetTime/60 && minutes == targetMinutes && seconds == 0)
			finished = true;
		return this.toString();
	}	
	
	public String tickDown(){
		if (seconds > 0)
			seconds--;
		else if (minutes > 0){ 
			minutes--;
			seconds = 59;
		}
		else{
			hours--;
			seconds = minutes = 59;
		}
		if (hours == 0 && minutes == 0 && seconds == 0){
			finished = true;
		}
		return this.toString();
	}	
	
	@Override
	public String toString(){
		String s;
		s = String.format("%02d:%02d:%02d", hours, minutes, seconds);
	    return s;
	}

	public int getElapsedTime() {
		return Math.abs(targetTime - minutes - hours*60);
	}

}
