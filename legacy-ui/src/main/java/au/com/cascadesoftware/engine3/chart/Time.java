package au.com.cascadesoftware.engine3.chart;

import au.com.cascadesoftware.engine3.exception.MultiplierException;

public class Time {
	
	public final int days, hours, minutes, seconds;

	public Time(int days, int hours, int minutes, int seconds) {
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public Time(Date max, Date min) {
		Date smallest = min;
		Date largest = max;
		if(max.isBefore(smallest)){
			smallest = max;
			largest = min;
		}
		int day = 0;
		while(!smallest.addDay(++day).startOfDay().isAfter(largest.startOfDay()));
		day--;
		Time t = new Time(day, largest.hour, largest.minute, largest.second).sub(new Time(0, smallest.hour, smallest.minute, smallest.second));
		//System.out.println(day + ", " + t.days);
		this.days = t.days;
		this.hours = t.hours;
		this.minutes = t.minutes;
		this.seconds = t.seconds;
	}

	public Time sub(Time time) {
		int days = this.days - time.days;
		int hours = this.hours - time.hours;
		int minutes = this.minutes - time.minutes;
		int seconds = this.seconds - time.seconds;
		if(seconds < 0){
			seconds += 60;
			minutes--;
		}
		if(minutes < 0){
			minutes += 60;
			hours--;
		}
		if(hours < 0){
			hours += 24;
			days--;
		}
		return new Time(days, hours, minutes, seconds);
	}

	public Time mul(float f) {
		if(f < 0) throw new MultiplierException("f < 0");
			
		float dayf = this.days*f;
		float hourf = this.hours*f;
		float minutef = this.hours*f;
		float secondf = this.hours*f;
		
		int day = (int)dayf;
		float dayR = dayf % 1;
		hourf += dayR * 24;
		
		int hour = (int)hourf;
		float hourR = hourf % 1;
		minutef += hourR * 60;
		
		int minute = (int)minutef;
		float minuteR = minutef % 1;
		secondf += minuteR * 60;
		
		int second = (int)secondf;
		float secondR = secondf % 1;
		if(secondR >= 0.5f) second++;

		while(second > 59){
			second -= 60;
			minute++;
		}
		while(minute > 59){
			minute -= 60;
			hour++;
		}
		while(hour > 23){
			hour -= 24;
			day++;
		}
		
		return new Time(day, hour, minute, second);
	}

	public Time add(Time time) {
		return new Time(this.days + time.days, this.hours + time.hours, this.minutes + time.minutes, this.seconds + time.seconds);
	}
	
	@Deprecated
	public void display(){
		String sec = seconds + "";
		String min = minutes + "";
		if(seconds < 10) sec = "0" + sec;
		if(minutes < 10) min = "0" + min;
		System.out.println("Time: " + hours + ":" + min + ":" + sec + ", Days: " + days);
	}
	
	public static int getMaxDayAmount(int month, int year) {
		if(month == 3 || month == 5 || month == 8 || month == 10) return 30;
		if(month == 1){
			if(isLeapYear(year)) return 29;
			return 28;
		}
		return 31;
	}

	public static  boolean isLeapYear(int year) {
		if(year % 4 == 0){
			if(year % 100 == 0){
				if(year % 400 == 0) return true;
				return false;
			}
			return true;
		}
		return false;
	}

}
