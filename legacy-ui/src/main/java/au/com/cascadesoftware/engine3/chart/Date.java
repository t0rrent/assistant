package au.com.cascadesoftware.engine3.chart;

import java.io.Serializable;
import java.time.DateTimeException;

@Warning(enabled = true)
public class Date implements Serializable {
	
	private static final long serialVersionUID = 7060463032944856804L;
	
	public final int day, month, year, hour, minute, second;

	public Date(int day, int month, int year){
		this(day, month, year, 0, 0, 0);
	}

	public Date(int day, int month, int year, int hour, int minute, int second){
		this.day = day;
		this.month = month;
		this.year = year;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		if(second >= 60 || minute >= 60 || hour >= 24 || month > 12 || year < 0 || day > getLastDayOfMonth(month, year)){
			display();
			throw new DateTimeException("Invalid date time");
		}
	}

	public Date(int day, int month, int year, Time time){
		this(day, month, year, time.hours, time.minutes, time.seconds);
		if(this.getClass().getAnnotation(Warning.class).enabled() && time.days > 0) System.err.println("WARNING: time is more than 1 day, date result may be unexpected");
	}
	
	public Date(Date date, int hour, int minute, int second){
		this(date.day, date.month, date.year, hour, minute, second);
	}

	public Date(String value) {
		// FORMAT: d-m-y h:m:s
		this(Integer.parseInt(value.split(" ")[0].split("-")[0]), Integer.parseInt(value.split(" ")[0].split("-")[1]), Integer.parseInt(value.split(" ")[0].split("-")[2]),
				Integer.parseInt(value.split(" ")[1].split(":")[0]), Integer.parseInt(value.split(" ")[1].split(":")[1]), Integer.parseInt(value.split(" ")[1].split(":")[2]));
	}

	@Override
	public boolean equals(Object obj){
		if(super.equals(obj)) return true;
		if(!(obj instanceof Date)) return false;
		Date d2 = (Date) obj;
		return d2.day == day && d2.hour == hour && d2.minute == minute && d2.month == month && d2.second == second && d2.year == year;
	}

	//ugly
	public boolean isBefore(Date date2) {
		if(date2.year >= year){
			if(date2.year == year){
				if(date2.month >= month){
					if(date2.month == month){
						if(date2.day >= day){
							if(date2.day == day){
								if(date2.hour >= hour){
									if(date2.hour == hour){
										if(date2.minute >= minute){
											if(date2.minute == minute){
												if(date2.second > second){
													return true;
												}
											}
											else return true;
										}
									}
									else return true;
								}
							}
							else return true;
						}
					}
					else return true;
				}
			}
			else return true;
		}
		return false;
	}
	
	public boolean isAfter(Date date2) {
		if(date2.year <= year){
			if(date2.year == year){
				if(date2.month <= month){
					if(date2.month == month){
						if(date2.day <= day){
							if(date2.day == day){
								if(date2.hour <= hour){
									if(date2.hour == hour){
										if(date2.minute <= minute){
											if(date2.minute == minute){
												if(date2.second < second){
													return true;
												}
											}
											else return true;
										}
									}
									else return true;
								}
							}
							else return true;
						}
					}
					else return true;
				}
			}
			else return true;
		}
		return false;
	}
	
	public Date startOfDay() {
		return new Date(day, month, year);
	}
	
	public Date startOfMonth() {
		return new Date(1, month, year);
	}
	
	public Date startOfYear() {
		return new Date(1, 1, year);
	}

	public Date startOfMinute() {
		return new Date(day, month, year, hour, minute, 0);
	}

	public Date startOfHour() {
		return new Date(day, month, year, hour, 0, 0);
	}
	
	public Date nextDay() {
		return addDay(1);
	}
	
	public Date addDay(int days) {
		int day = this.day + days;
		int month = this.month;
		int year = this.year;
		for(int m; (m = getLastDayOfMonth(month, year)) < day;){
			day -= m;
			month++;
			if(month > 12){
				month -= 12;
				year++;
			}
		}
		while(day < 1){
			int a = month - 1;
			int b = year;
			if(a < 1){
				a += 12;
				b--;
			}
			int m = getLastDayOfMonth(a, b);
			day += m;
			month--;
			if(month < 1){
				month += 12;
				year--;
			}
		}
		return new Date(day, month, year, hour, minute, second);
	}
	
	public Date addMonth(int months){
		int month = this.month + months;
		int year = this.year;
		while(month > 12){
			year++;
			month -= 12;
		}
		while(month < 1){
			year--;
			month += 12;
		}
		if(year < 0){
			return new Date(1,1,0);
		}
		return new Date(day, month, year, hour, minute, second);
	}
	
	public Date addYear(int years){
		int year = this.year + years;
		if(year < 0){
			return new Date(1,1,0);
		}
		return new Date(day, month, year, hour, minute, second);
	}
	
	public Date addHour(int hours){
		int hour = this.hour + hours;
		int year = this.year;
		int month = this.month;
		int day = this.day;
		while(hour < 0){
			Date d2 = new Date(day, month, year, 0, 0, 0).addDay(-1);
			year = d2.year;
			month = d2.month;
			day = d2.day;
			hour += 24;
		}
		if(hour > 23){
			Date d2 = new Date(day, month, year, 0, 0, 0).addDay(hour/24);
			year = d2.year;
			month = d2.month;
			day = d2.day;
			hour %= 24;
		}
		return new Date(day, month, year, hour, minute, second);
	}
	
	public Date addMinute(int minutes){
		int minute = this.minute + minutes;
		int hour = this.hour;
		int year = this.year;
		int month = this.month;
		int day = this.day;
		while(minute < 0){
			Date d2 = new Date(day, month, year, hour, 0, 0).addHour(-1);
			year = d2.year;
			month = d2.month;
			day = d2.day;
			hour = d2.hour;
			minute += 60;
		}
		if(minute > 59){
			Date d2 = new Date(day, month, year, hour, 0, 0).addHour(minute/60);
			year = d2.year;
			month = d2.month;
			day = d2.day;
			hour = d2.hour;
			minute %= 60;
		}
		return new Date(day, month, year, hour, minute, second);
	}
	
	public Date addSecond(int seconds){
		int second = this.second + seconds;
		int minute = this.minute;
		int hour = this.hour;
		int year = this.year;
		int month = this.month;
		int day = this.day;
		if(second > 59){
			Date d2 = new Date(day, month, year, hour, minute, 0).addMinute(second/60);
			year = d2.year;
			month = d2.month;
			day = d2.day;
			hour = d2.hour;
			minute = d2.minute;
			second %= 60;
		}
		while(second < 0){
			Date d2 = new Date(day, month, year, hour, minute, 0).addMinute(-1);
			year = d2.year;
			month = d2.month;
			day = d2.day;
			hour = d2.hour;
			minute = d2.minute;
			second += 60;
		}
		return new Date(day, month, year, hour, minute, second);
	}
	
	public Day getDayOfWeek() {
		int Y = year;
		int M = month;
		int D = day;
		if(M < 3){
	        M += 12;
	        Y--;
	    }
	    int C = Y/100;
		int K = Y - 100*C;
		int S = (int) (Math.floor(2.6 * M - 5.39) + Math.floor(K/4.0) + Math.floor(C/4.0) + D + K - (2*C));
	    int w = (int) (S - (7*Math.floor(S/7.0)));
	    w--;
	    if(w < 0) w += 7;
	    w %= 7;
	    return Day.values()[w];
	}
	
	@Override
	public String toString(){
		String sec = second + "";
		String min = minute + "";
		if(second < 10) sec = "0" + sec;
		if(minute < 10) min = "0" + min;
		return "Time: " + hour + ":" + min + ":" + sec + ", Date: " + day + "/" + month + "/" + year;
	}

	@Deprecated
	public void display(){
		System.out.println(toString());
	}
	
	public static int getLastDayOfMonth(int month, int year) {
		if(month == 4 || month == 6 || month == 9 || month == 11) return 30;
		if(month == 2){
			if(isLeapYear(year)) return 29;
			return 28;
		}
		return 31;
	}

	public static boolean isLeapYear(int year) {
		if(year % 4 == 0){
			if(year % 100 == 0){
				if(year % 400 == 0) return true;
				return false;
			}
			return true;
		}
		return false;
	}
	
	public Date startOfWeek() {
		Day day = this.getDayOfWeek();
		return this.addDay(-day.ordinal());
	}
	
	
	
	@Warning(enabled = true)
	public class ZeroIndexed extends Date{
		
		private static final long serialVersionUID = -3060017277952382060L;

		public ZeroIndexed(int day, int month, int year){
			this(day, month, year, 0, 0, 0);
		}

		public ZeroIndexed(int day, int month, int year, int hour, int minute, int second){
			super(day + 1, month + 1, year, hour, minute, second);
		}

		public ZeroIndexed(int day, int month, int year, Time time){
			this(day, month, year, time.hours, time.minutes, time.seconds);
			if(this.getClass().getAnnotation(Warning.class).enabled() && time.days > 0) System.err.println("WARNING: time is more than 1 day, date result may be unexpected");
		}
		
		public ZeroIndexed(Date date, int hour, int minute, int second){
			this(date.day, date.month, date.year, hour, minute, second);
		}
	}
	
}
